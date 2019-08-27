package com.paulniu.media_opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Coder: niupuyue
 * Date: 2019/8/27
 * Time: 17:40
 * Desc: 第一个自定义的GLSurfaceView
 * Version:
 */
public class MyGLSurfaveView extends GLSurfaceView {

    private MyGLSurfaceViewRendered renderer;

    public MyGLSurfaveView(Context context) {
        super(context);
        init();
    }

    private void init() {
        // 设置我们使用OpenGL的版本  2
        setEGLContextClientVersion(2);
        renderer = new MyGLSurfaceViewRendered();
        setRenderer(renderer);
    }

}
