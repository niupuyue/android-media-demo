package com.paulniu.media_opengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Coder: niupuyue
 * Date: 2019/8/27
 * Time: 17:35
 * Desc: OpenGL简单示例
 * Version:
 */
public class OpenGLSimpleActivity extends AppCompatActivity {

    private MyGLSurfaveView surfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surfaceView = new MyGLSurfaveView(this);
        setContentView(surfaceView);
    }
}
