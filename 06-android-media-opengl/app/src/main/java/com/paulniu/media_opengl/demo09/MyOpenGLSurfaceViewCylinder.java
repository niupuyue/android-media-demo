package com.paulniu.media_opengl.demo09;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Coder: niupuyue
 * Date: 2019/9/5
 * Time: 18:55
 * Desc: 绘制圆柱体
 * Version:
 */
public class MyOpenGLSurfaceViewCylinder extends GLSurfaceView {
    public MyOpenGLSurfaceViewCylinder(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new MyOpenGLRenderCylinder(context));
    }
}
