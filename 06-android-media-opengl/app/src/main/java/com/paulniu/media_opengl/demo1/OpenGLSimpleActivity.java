package com.paulniu.media_opengl.demo1;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paulniu.media_opengl.demo1.MyGLSurfaveView;

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
