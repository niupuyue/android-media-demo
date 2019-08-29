package com.paulniu.media_opengl.demo2;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Coder: niupuyue
 * Date: 2019/8/29
 * Time: 10:50
 * Desc:
 * Version:
 */
public class MyOpenGLSurfaceView2 extends GLSurfaceView {
    private Context context;
    private MyOpenGLRendered2 rendered;

    public MyOpenGLSurfaceView2(Context context) {
        super(context);
        this.context = context;
        setEGLContextClientVersion(2);
        rendered = new MyOpenGLRendered2(this.context);
        setRenderer(rendered);
    }
}
