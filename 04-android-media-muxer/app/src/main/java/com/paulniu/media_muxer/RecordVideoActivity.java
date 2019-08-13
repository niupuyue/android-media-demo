package com.paulniu.media_muxer;

import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Coder: niupuyue
 * Date: 2019/8/13
 * Time: 9:56
 * Desc:
 * Version:
 */
public class RecordVideoActivity extends AppCompatActivity {

    private ImageView iv_start_stop_record;
    private SurfaceView recordSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
    }
}
