package com.paulniu.media_opengl.demo07;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.paulniu.media_opengl.R;
import com.paulniu.media_opengl.demo1.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * Coder: niupuyue
 * Date: 2019/8/30
 * Time: 10:38
 * Desc:
 * Version:
 */
public class MyOpenGLRenderCube implements GLSurfaceView.Renderer {

    private Context context;

    private int program;

    private int positionHandler;
    private int colorHandler;
    private int matrixHandler;

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer indexBuffer;

    private float[] projectMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] mvpMatrix = new float[16];

    private float[] colorCoords = {
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
    };

    private float[] vertexCoords = {
            -0.5f, 0.5f, 1f,
            -0.5f, -0.5f, 1f,
            0.5f, -0.5f, 1f,
            0.5f, 0.5f, 1f,
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f
    };

    private static short[] indexCoords = {
            0, 1, 2, 0, 2, 3,// 上面
            0, 4, 5, 0, 5, 1,// 左面
            0, 4, 7, 0, 7, 3,// 后面
            4, 5, 7, 4, 5, 6,// 底面
            1, 2, 5, 2, 5, 6,// 前面
            2, 6, 7, 2, 3, 7// 右面
    };
    private float[] projectMatrix1;

    public MyOpenGLRenderCube(Context context) {
        this.context = context;

        // 设置顶点
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertexCoords);
        vertexBuffer.position(0);

        // 设置颜色
        ByteBuffer cc = ByteBuffer.allocateDirect(colorCoords.length * 4);
        cc.order(ByteOrder.nativeOrder());
        colorBuffer = cc.asFloatBuffer();
        colorBuffer.put(colorCoords);
        colorBuffer.position(0);

        // 设置索引
        ByteBuffer dd = ByteBuffer.allocateDirect(indexCoords.length * 2);
        dd.order(ByteOrder.nativeOrder());
        indexBuffer = dd.asShortBuffer();
        indexBuffer.put(indexCoords);
        indexBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        String vertexSource = ShaderUtils.readRawText(context, R.raw.vertex_shader_7);
        String fragmentSource = ShaderUtils.readRawText(context, R.raw.fragment_shader_7);
        program = ShaderUtils.createProgram(vertexSource, fragmentSource);
        if (program > 0) {
            positionHandler = GLES20.glGetAttribLocation(program, "position");
            colorHandler = GLES20.glGetAttribLocation(program, "color");
            matrixHandler = GLES20.glGetUniformLocation(program, "matrix");
        }
        // 开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        float ratio = (float) i / i1;
        Matrix.frustumM(projectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        Matrix.setLookAtM(viewMatrix, 0, 5.0f, 5.0f, 10.0f, 0f, 0f, 0f, 0f, 1f, 0f);
        Matrix.multiplyMM(mvpMatrix, 0, projectMatrix1, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f);

        GLES20.glUseProgram(program);

        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glVertexAttribPointer(positionHandler, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        GLES20.glEnableVertexAttribArray(colorHandler);
        GLES20.glVertexAttribPointer(colorHandler, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

        GLES20.glUniformMatrix4fv(matrixHandler, 1, false, mvpMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCoords.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
    }
}
