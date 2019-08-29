package com.paulniu.media_opengl.demo04;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Coder: niupuyue
 * Date: 2019/8/29
 * Time: 15:39
 * Desc: 彩色的三角形
 * Version:
 */
public class MyOpenGLColorsTRiangleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyOpenGLSurfaceViewColors(this));
    }
}
