package com.paulniu.media_opengl.demo06;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Coder: niupuyue
 * Date: 2019/8/29
 * Time: 17:34
 * Desc:
 * Version:
 */
public class MyOpenGLOvalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyOpenGLSurfaceViewOval(this));
    }
}
