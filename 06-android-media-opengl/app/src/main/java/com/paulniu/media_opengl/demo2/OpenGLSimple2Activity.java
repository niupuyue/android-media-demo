package com.paulniu.media_opengl.demo2;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Coder: niupuyue
 * Date: 2019/8/29
 * Time: 10:48
 * Desc:
 * Version:
 */
public class OpenGLSimple2Activity extends AppCompatActivity {

    private MyOpenGLSurfaceView2 glSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new MyOpenGLSurfaceView2(this);
        setContentView(glSurfaceView);
    }
}
