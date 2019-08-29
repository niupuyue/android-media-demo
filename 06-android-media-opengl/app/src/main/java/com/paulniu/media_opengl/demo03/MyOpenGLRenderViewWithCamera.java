package com.paulniu.media_opengl.demo03;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

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
 * Time: 15:10
 * Desc:
 * Version:
 */
public class MyOpenGLRenderViewWithCamera implements GLSurfaceView.Renderer {

    private Context context;

    private int program;

    private int positionHandler;
    private int colorHandler;
    private int matrixHandler;

    private float[] viewMatrix = new float[16];
    private float[] projectMatrix = new float[16];
    private float[] mvpMatrix = new float[16];

    private float triangleCoords[] = {
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };

    private FloatBuffer vertexBuffer;

    public MyOpenGLRenderViewWithCamera(Context context) {
        this.context = context;
        // 申请底层存储控件
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        String vertexSource = ShaderUtils.readRawText(context, R.raw.vertex_shader_3);
        String fragmentSource = ShaderUtils.readRawText(context, R.raw.fragment_shader_3);
        program = ShaderUtils.createProgram(vertexSource, fragmentSource);
        if (program > 0) {
            positionHandler = GLES20.glGetAttribLocation(program, "vPosition");
            colorHandler = GLES20.glGetUniformLocation(program, "af_Color");
            matrixHandler = GLES20.glGetUniformLocation(program, "vMatrix");
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // 计算宽高比
        float ratio = (float) width / height;
        // 设置透视投影
        Matrix.frustumM(projectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        // 设置相机的位置
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 7f, 0f, 0f, 0f, 0f, 1f, 0f);
        // 计算变换矩阵
        Matrix.multiplyMM(mvpMatrix, 0, projectMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f);

        GLES20.glUseProgram(program);
        // 设置填充颜色
        GLES20.glUniform4f(colorHandler, 1f, 1f, 0f, 1f);
        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glUniformMatrix4fv(matrixHandler,1,false,mvpMatrix,0);
        GLES20.glVertexAttribPointer(positionHandler, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
