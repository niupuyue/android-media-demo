package com.paulniu.media_opengl.demo2;

import android.content.Context;
import android.graphics.Shader;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.paulniu.media_opengl.R;
import com.paulniu.media_opengl.demo1.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Coder: niupuyue
 * Date: 2019/8/29
 * Time: 10:51
 * Desc:
 * Version:
 */
public class MyOpenGLRendered2 implements GLSurfaceView.Renderer {

    private Context context;
    // 设置三角形三个顶点
    private float triangleCoords[] = {
            0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };

    public MyOpenGLRendered2(Context context) {
        this.context = context;
        // 申请底层存储空间
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        // 设置排列方法
        bb.order(ByteOrder.nativeOrder());
        // 将byteBuffer转换成FloatBuffer
        vertexBuffer = bb.asFloatBuffer();
        // 将顶点数据加载到floatBuffer中
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
    }

    private FloatBuffer vertexBuffer;
    private int program;
    private int positionHandler;
    private int colorHandler;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        String vertexSource = ShaderUtils.readRawText(context, R.raw.vertex_shader_2);
        String fragmentSource = ShaderUtils.readRawText(context, R.raw.fragment_shader_2);
        program = ShaderUtils.createProgram(vertexSource, fragmentSource);
        if (program > 0) {
            positionHandler = GLES20.glGetAttribLocation(program, "av_Position");
            colorHandler = GLES20.glGetUniformLocation(program, "af_Color");
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        // 设置视图窗口视图
        GLES20.glViewport(0, 0, i, i1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // 设置背景颜色
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        GLES20.glUseProgram(program);
        GLES20.glUniform4f(colorHandler, 1.0f, 1.0f, 0f, 1f);
        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glVertexAttribPointer(positionHandler, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
