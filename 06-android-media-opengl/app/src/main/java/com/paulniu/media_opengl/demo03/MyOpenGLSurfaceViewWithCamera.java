package com.paulniu.media_opengl.demo03;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Coder: niupuyue
 * Date: 2019/8/29
 * Time: 15:09
 * Desc:
 * Version:
 */
public class MyOpenGLSurfaceViewWithCamera extends GLSurfaceView {

    private MyOpenGLRenderViewWithCamera render;

    public MyOpenGLSurfaceViewWithCamera(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(render = new MyOpenGLRenderViewWithCamera(context));
    }
}
