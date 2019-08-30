package com.paulniu.media_opengl.demo07;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Coder: niupuyue
 * Date: 2019/8/30
 * Time: 10:12
 * Desc: 绘制立方体
 * Version:
 */
public class MyOpenGLCubeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyOpenGLSurfaceViewCube(this));
    }
}
