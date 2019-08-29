package com.paulniu.media_opengl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.paulniu.media_opengl.demo03.MyOpenGLTriangleActivity;
import com.paulniu.media_opengl.demo1.OpenGLSimpleActivity;
import com.paulniu.media_opengl.demo2.OpenGLSimple2Activity;

public class MainActivity extends AppCompatActivity {

    private Button btn_01;
    private Button btn_02;
    private Button btn_03;

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
    }
}
