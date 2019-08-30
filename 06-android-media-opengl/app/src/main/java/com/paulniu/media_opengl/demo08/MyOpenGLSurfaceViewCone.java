package com.paulniu.media_opengl.demo08;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Coder: niupuyue
 * Date: 2019/8/30
 * Time: 13:59
 * Desc:
 * Version:
 */
public class MyOpenGLSurfaceViewCone extends GLSurfaceView {
    public MyOpenGLSurfaceViewCone(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new MyOpenGLRenderCone(context));
    }
}
