package com.paulniu.media;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2019-08-05
 * Time: 22:30
 * Desc: 两种方式实现，1：都通在XML文件中添加ImageView标签   2：通过在java代码中动态生成一个ImageView即可实现
 * Version:
 */
public class SimpleImageViewActivity extends Activity {

    private ImageView imageView;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_imageview);
        container = findViewById(R.id.container);

        ImageView image = new ImageView(this);
        image.setBackgroundResource(R.mipmap.ic_launcher);

    }
}
