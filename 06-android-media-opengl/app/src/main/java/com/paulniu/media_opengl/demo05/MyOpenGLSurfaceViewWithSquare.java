package com.paulniu.media_opengl.demo05;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Coder: niupuyue
 * Date: 2019/8/29
 * Time: 16:19
 * Desc:
 * Version:
 */
public class MyOpenGLSurfaceViewWithSquare extends GLSurfaceView {
    public MyOpenGLSurfaceViewWithSquare(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new MyOpenGLRenderSquare(context));
    }
}
