package com.paulniu.media_opengl.demo06;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Coder: niupuyue
 * Date: 2019/8/29
 * Time: 17:36
 * Desc:
 * Version:
 */
public class MyOpenGLSurfaceViewOval extends GLSurfaceView {
    public MyOpenGLSurfaceViewOval(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new MyOpenGLRenderOval(context));
    }
}
