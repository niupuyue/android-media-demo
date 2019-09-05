package com.paulniu.media_opengl.demo10;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Coder: niupuyue
 * Date: 2019/9/5
 * Time: 18:56
 * Desc:
 * Version:
 */
public class MyOpenGLSufaceViewSphere extends GLSurfaceView {
    public MyOpenGLSufaceViewSphere(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new MyOpenGLRenderSphere(context));
    }
}
