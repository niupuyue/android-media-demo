package com.paulniu.media_opengl.demo03;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Coder: niupuyue
 * Date: 2019/8/29
 * Time: 15:07
 * Desc: 使用相机创建一个三角形，并且为三角形上颜色
 * Version:
 */
public class MyOpenGLTriangleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyOpenGLSurfaceViewWithCamera(this));
    }
}
