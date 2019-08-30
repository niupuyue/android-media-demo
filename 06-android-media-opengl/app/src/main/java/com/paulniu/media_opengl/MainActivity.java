package com.paulniu.media_opengl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.paulniu.media_opengl.demo03.MyOpenGLTriangleActivity;
import com.paulniu.media_opengl.demo04.MyOpenGLColorsTRiangleActivity;
import com.paulniu.media_opengl.demo05.MyOpenGLSquareActivity;
import com.paulniu.media_opengl.demo06.MyOpenGLOvalActivity;
import com.paulniu.media_opengl.demo07.MyOpenGLCubeActivity;
import com.paulniu.media_opengl.demo08.MyOpenGLConeActivity;
import com.paulniu.media_opengl.demo09.MyOpenGLCylinderActivity;
import com.paulniu.media_opengl.demo1.OpenGLSimpleActivity;
import com.paulniu.media_opengl.demo10.MyOpenGLSphereActivity;
import com.paulniu.media_opengl.demo11.MyOpenGLSphere2Activity;
import com.paulniu.media_opengl.demo2.OpenGLSimple2Activity;

public class MainActivity extends AppCompatActivity {

    private Button btn_01;
    private Button btn_02;
    private Button btn_03;
    private Button btn_04;
    private Button btn_05;
    private Button btn_06;
    private Button btn_07;
    private Button btn_08;
    private Button btn_09;
    private Button btn_10;
    private Button btn_11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_01 = findViewById(R.id.btn_01);
        btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OpenGLSimpleActivity.class);
                startActivity(intent);
            }
        });

        btn_02 = findViewById(R.id.btn_02);
        btn_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OpenGLSimple2Activity.class);
                startActivity(intent);
            }
        });

        btn_03 = findViewById(R.id.btn_03);
        btn_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyOpenGLTriangleActivity.class);
                startActivity(intent);
            }
        });

        btn_04 = findViewById(R.id.btn_04);
        btn_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyOpenGLColorsTRiangleActivity.class);
                startActivity(intent);
            }
        });

        btn_05 = findViewById(R.id.btn_05);
        btn_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyOpenGLSquareActivity.class);
                startActivity(intent);
            }
        });

        btn_06 = findViewById(R.id.btn_06);
        btn_06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyOpenGLOvalActivity.class);
                startActivity(intent);
            }
        });

        btn_07 = findViewById(R.id.btn_07);
        btn_07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyOpenGLCubeActivity.class);
                startActivity(intent);
            }
        });

        btn_08 = findViewById(R.id.btn_08);
        btn_08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyOpenGLConeActivity.class);
                startActivity(intent);
            }
        });

        btn_09 = findViewById(R.id.btn_09);
        btn_09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyOpenGLCylinderActivity.class);
                startActivity(intent);
            }
        });

        btn_10 = findViewById(R.id.btn_10);
        btn_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyOpenGLSphereActivity.class);
                startActivity(intent);
            }
        });

        btn_11 = findViewById(R.id.btn_11);
        btn_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyOpenGLSphere2Activity.class);
                startActivity(intent);
            }
        });
    }
}
