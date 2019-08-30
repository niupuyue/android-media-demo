package com.paulniu.media_opengl.demo07;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Coder: niupuyue
 * Date: 2019/8/30
 * Time: 10:39
 * Desc:
 * Version:
 */
public class MyOpenGLSurfaceViewCube extends GLSurfaceView {
    public MyOpenGLSurfaceViewCube(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new MyOpenGLRenderCube(context));
    }
}
