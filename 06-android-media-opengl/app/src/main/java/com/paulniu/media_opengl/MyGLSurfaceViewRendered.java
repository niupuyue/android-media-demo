package com.paulniu.media_opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Coder: niupuyue
 * Date: 2019/8/27
 * Time: 18:29
 * Desc:
 * Version:
 */
public class MyGLSurfaceViewRendered implements GLSurfaceView.Renderer {

    private Triangle mTriangle;
    private Square mSquare;

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
//        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // 当MyGLSurfaceView被创建的时候，我们申明图形对象
        mTriangle = new Triangle();
        mSquare = new Square();
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

}
