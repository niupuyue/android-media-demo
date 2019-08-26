package com.paulniu.media_muxer;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.ByteBuffer;

/**
 * Coder: niupuyue
 * Date: 2019/8/26
 * Time: 14:27
 * Desc: 将视频中的音频文件抽离出来
 * Version:
 */
public class FormatAudioActivity extends AppCompatActivity {

    private Button formatAudio_Extractor;

    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_format_audio);
        formatAudio_Extractor = findViewById(R.id.formatAudio_Extractor);
        formatAudio_Extractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formatAudio();
            }
        });
        path = RecordVideoActivity.getSDPath() + "/paulniu/2019826114146.mp4";
    }

    private void formatAudio() {
        MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(path);
            int trackCount = mediaExtractor.getTrackCount();
            int audioIndex = -1;
            for (int i = 0; i < trackCount; i++) {
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                String type = trackFormat.getString(MediaFormat.KEY_MIME);
                if (type.startsWith("audio/")) {
                    audioIndex = i;
                }
            }
            mediaExtractor.selectTrack(audioIndex);
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(audioIndex);
            MediaMuxer mediaMuxer = new MediaMuxer(RecordVideoActivity.getSDPath() + "/paulniu/temp_audio", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int writeAudio = mediaMuxer.addTrack(trackFormat);
            mediaMuxer.start();
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 500);
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            long sampleTime = 0;
            mediaExtractor.readSampleData(buffer, 0);
            if (mediaExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC) {
                mediaExtractor.advance();
            }
            mediaExtractor.readSampleData(buffer, 0);
            long firstTime = mediaExtractor.getSampleTime();
            mediaExtractor.advance();
            long secondTime = mediaExtractor.getSampleTime();
            sampleTime = Math.abs(secondTime - firstTime);

            mediaExtractor.unselectTrack(audioIndex);
            mediaExtractor.selectTrack(audioIndex);
            while (true) {
                int readSize = mediaExtractor.readSampleData(buffer, 0);
                if (readSize < 0) {
                    break;
                }
                mediaExtractor.advance();
                bufferInfo.size = readSize;
                bufferInfo.flags = mediaExtractor.getSampleFlags();
                bufferInfo.offset = 0;
                bufferInfo.presentationTimeUs += sampleTime;
                mediaMuxer.writeSampleData(writeAudio, buffer, bufferInfo);
            }
            mediaMuxer.stop();
            mediaMuxer.release();
            mediaExtractor.release();
            Toast.makeText(FormatAudioActivity.this, "抽离出音频文件成功", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
