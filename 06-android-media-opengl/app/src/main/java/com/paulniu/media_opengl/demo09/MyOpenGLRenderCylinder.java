package com.paulniu.media_opengl.demo09;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Coder: niupuyue
 * Date: 2019/9/5
 * Time: 18:56
 * Desc: 圆柱体
 * Version:
 */
public class MyOpenGLRenderCylinder implements GLSurfaceView.Renderer {

    private Context mContext;

    public MyOpenGLRenderCylinder(Context context){
        this.mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }
}
