package com.paulniu.media_opengl.demo1;

import android.content.Context;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Coder: niupuyue
 * Date: 2019/8/28
 * Time: 18:23
 * Desc:
 * Version:
 */
public class ShaderUtils {

    /**
     * 将glsl数据内容转换成String类型
     *
     * @param context
     * @param rawId
     * @return
     */
    public static String readRawText(Context context, int rawId) {
        InputStream is = context.getResources().openRawResource(rawId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public static int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compile = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compile, 0);
            if (compile[0] != GLES20.GL_TRUE) {
                // 生成shader失败
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == 0) {
            return 0;
        }
        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, fragmentShader);
            GLES20.glLinkProgram(program);
            int[] linsStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linsStatus, 0);
            if (linsStatus[0] != GLES20.GL_TRUE) {
                // 生成program社保
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    public static FloatBuffer fBuffer(float[] a) {
        FloatBuffer floatBuffer;
        // 先初始化buffer,数组的长度*4,因为一个float占4个字节
        ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
        // 数组排列用nativeOrder
        mbb.order(ByteOrder.nativeOrder());
        floatBuffer = mbb.asFloatBuffer();
        floatBuffer.put(a);
        floatBuffer.position(0);
        return floatBuffer;
    }

}
