package com.paulniu.media_opengl.demo05;

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
import javax.microedition.khronos.opengles.GL10;

/**
 * Coder: niupuyue
 * Date: 2019/8/29
 * Time: 16:20
 * Desc:
 * Version:
 */
public class MyOpenGLRenderSquare implements GLSurfaceView.Renderer {

    private Context context;
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer indexBuffer;

    private int positionHandler;
    private int colorHandler;
    private int matrixHandler;

    private int program;

    private float[] projectMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] mvpMatrix = new float[16];

    private float[] squareCoords = {
            -0.5f, 0.5f, 0.0f, // top left
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f, // bottom right
            0.5f, 0.5f, 0.0f  // top right
    };
    private float[] colorCoords = {
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
    };
    private short[] indexCoords = {
            0, 1, 2, 0, 2, 3
    };

    public MyOpenGLRenderSquare(Context context) {
        this.context = context;

        // 设置顶点数据
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
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
        String vertexSource = ShaderUtils.readRawText(context, R.raw.vertex_shader_5);
        String fragmentSource = ShaderUtils.readRawText(context, R.raw.fragment_shader_5);
        program = ShaderUtils.createProgram(vertexSource, fragmentSource);
        if (program > 0) {
            positionHandler = GLES20.glGetAttribLocation(program, "vPosition");
            colorHandler = GLES20.glGetAttribLocation(program, "aColor");
            matrixHandler = GLES20.glGetUniformLocation(program, "vMatrix");
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // 计算宽高比
        float ratio = (float) width / height;
        Matrix.frustumM(projectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        // 设置相机的位置
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 7f, 0f, 0f, 0f, 0f, 1f, 0f);
        // 计算变换矩阵
        Matrix.multiplyMM(mvpMatrix, 0, projectMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f);

        GLES20.glUseProgram(program);

        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glVertexAttribPointer(positionHandler, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);

        GLES20.glEnableVertexAttribArray(colorHandler);
        GLES20.glVertexAttribPointer(colorHandler, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

        GLES20.glUniformMatrix4fv(matrixHandler, 1, false, mvpMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCoords.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
