package com.paulniu.media_opengl.demo1;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.paulniu.media_opengl.demo1.MyGLSurfaceViewRendered;

/**
 * Coder: niupuyue
 * Date: 2019/8/27
 * Time: 17:40
 * Desc: 第一个自定义的GLSurfaceView
 * Version:
 */
public class MyGLSurfaveView extends GLSurfaceView {

    private MyGLSurfaceViewRendered renderer;
    private Context context;

    public MyGLSurfaveView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        // 设置我们使用OpenGL的版本  2
        setEGLContextClientVersion(2);
        renderer = new MyGLSurfaceViewRendered(context);
        setRenderer(renderer);
    }

}
