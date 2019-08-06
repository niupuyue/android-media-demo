package com.paulniu.audio_collection;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paulniu.audio_collection.mediarecord.IMediaRecorder;

/**
 * Coder: niupuyue
 * Date: 2019/8/6
 * Time: 10:06
 * Desc: 使用mediarecord录制采集音频
 * Version:
 */
public class MediaRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnStartRecord, btnEndRecord, btnPlayAudio;
    private IMediaRecorder mediaRecorder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_record);

        btnStartRecord = findViewById(R.id.btnStartRecord);

        btnEndRecord = findViewById(R.id.btnEndRecord);

        btnPlayAudio = findViewById(R.id.btnPlayAudio);

        btnStartRecord.setOnClickListener(this);
        btnEndRecord.setOnClickListener(this);
        btnPlayAudio.setOnClickListener(this);

        mediaRecorder = IMediaRecorder.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartRecord:
                mediaRecorder.startRecorder();
                break;
            case R.id.btnEndRecord:
                mediaRecorder.stopRecorder();
                break;
            case R.id.btnPlayAudio:
                mediaRecorder.playRecorder();
                break;
        }
    }
}
