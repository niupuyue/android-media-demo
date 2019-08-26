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
 * Time: 11:49
 * Desc: 解析视频中的视频文件，不包含音频文件
 * Version:
 */
public class FormatVideoActivity extends AppCompatActivity {

    private Button formatVideo_extractor;

    private String path = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_format_video);

        formatVideo_extractor = findViewById(R.id.formatVideo_extractor);
        formatVideo_extractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extracVidoe();
            }
        });

        // 获取系统目录并且赋值给当前的数据
        path = RecordVideoActivity.getSDPath() + "/paulniu/2019826114146.mp4";
    }

    private void extracVidoe() {
        MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(path);
            // 获取通道的个数
            int trackCount = mediaExtractor.getTrackCount();
            int videoIndex = -1;
            for (int i = 0; i < trackCount; i++) {
                // 这个trackformat可以获取视频的宽高，视频的通道(音频视频),还可以获取帧率
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                String typeMimb = trackFormat.getString(MediaFormat.KEY_MIME);
                if (typeMimb.startsWith("video/")) {
                    // 获取到音频的信号通道
                    videoIndex = i;
                    break;
                }
            }
            // 设置音频通道信号
            mediaExtractor.selectTrack(videoIndex);
            // 再次拿到视频通道的format
            MediaFormat videoFormat = mediaExtractor.getTrackFormat(videoIndex);
            // 初始化视频合成器
            MediaMuxer mediaMuxer = new MediaMuxer(RecordVideoActivity.getSDPath()+"/paulniu/temp_video.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            // 添加合成器通道
            mediaMuxer.addTrack(videoFormat);
            ByteBuffer buffer = ByteBuffer.allocate(500 * 1024);

            mediaMuxer.start();
            // 获取视频帧率
            long videoSameTime = 0;
            mediaExtractor.readSampleData(buffer, 0);
            // 解析的时候我们跳过I帧，获取P帧(视频一般都是由个别I帧和多个P帧组成的)
            if (mediaExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC) {
                mediaExtractor.advance();
            }
            mediaExtractor.readSampleData(buffer, 0);
            long firstTime = mediaExtractor.getSampleTime();
            // 获取下一帧
            mediaExtractor.advance();

            mediaExtractor.readSampleData(buffer, 0);
            long secondTime = mediaExtractor.getSampleTime();
            videoSameTime = Math.abs(secondTime - firstTime);
            // 重新设置通道读取文件
            mediaExtractor.unselectTrack(videoIndex);
            mediaExtractor.selectTrack(videoIndex);
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            while (true) {
                int readSamSize = mediaExtractor.readSampleData(buffer, 0);
                if (readSamSize < 0) {
                    break;
                }
                mediaExtractor.advance();
                bufferInfo.flags = mediaExtractor.getSampleFlags();
                bufferInfo.size = readSamSize;
                bufferInfo.offset = 0;
                bufferInfo.presentationTimeUs += videoSameTime;
                mediaMuxer.writeSampleData(videoIndex, buffer, bufferInfo);
            }
            mediaMuxer.stop();
            mediaMuxer.release();
            mediaExtractor.release();
            Toast.makeText(FormatVideoActivity.this, "解析数据成功", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
