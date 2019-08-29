package com.paulniu.media_opengl.demo04;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Coder: niupuyue
 * Date: 2019/8/29
 * Time: 15:39
 * Desc:
 * Version:
 */
public class MyOpenGLSurfaceViewColors extends GLSurfaceView {
    public MyOpenGLSurfaceViewColors(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new MyOpenGLRenderViewColors(context));
    }
}
