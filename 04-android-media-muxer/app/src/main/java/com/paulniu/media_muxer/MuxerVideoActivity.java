package com.paulniu.media_muxer;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.ByteBuffer;

/**
 * Coder: niupuyue
 * Date: 2019/8/26
 * Time: 15:05
 * Desc: 合成视频文件
 * Version:
 */
public class MuxerVideoActivity extends AppCompatActivity {

    private Button muxer_video;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muxer_video);
        muxer_video = findViewById(R.id.muxer_video);
        muxer_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                muxerVideo();
            }
        });
    }

    // 将前两步的音频和视频合成新的文件
    private void muxerVideo() {
        try {
            MediaExtractor videoExtractor = new MediaExtractor();
            videoExtractor.setDataSource(RecordVideoActivity.getSDPath() + "/paulniu/temp_video.mp4");
            MediaFormat videoFormat = null;
            int videoTrackIndex = -1;
            int videoTrackCount = videoExtractor.getTrackCount();
            for (int i = 0; i < videoTrackCount; i++) {
                videoFormat = videoExtractor.getTrackFormat(i);
                String mimeType = videoFormat.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("video/")) {
                    videoTrackIndex = i;
                    break;
                }
            }
            MediaExtractor audioExtractor = new MediaExtractor();
            audioExtractor.setDataSource(RecordVideoActivity.getSDPath() + "/paulniu/temp_audio");
            MediaFormat audioFormat = null;
            int audioTrackIndex = -1;
            int audioTrackCount = audioExtractor.getTrackCount();
            for (int i = 0; i < audioTrackCount; i++) {
                audioFormat = audioExtractor.getTrackFormat(i);
                String mimeType = audioFormat.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("audio/")) {
                    audioTrackIndex = i;
                    break;
                }
            }

            videoExtractor.selectTrack(videoTrackIndex);
            audioExtractor.selectTrack(audioTrackIndex);

            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();

            MediaMuxer mediaMuxer = new MediaMuxer(RecordVideoActivity.getSDPath() + "/paulniu/temp.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int writeVidoeTrackIndex = mediaMuxer.addTrack(videoFormat);
            int writeAudioTrackIndex = mediaMuxer.addTrack(audioFormat);
            mediaMuxer.start();

            ByteBuffer byteBuffer = ByteBuffer.allocate(500 * 1024);
            long sampleTime = 0;
            {
                videoExtractor.readSampleData(byteBuffer, 0);
                if (videoExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC) {
                    videoExtractor.advance();
                }
                videoExtractor.readSampleData(byteBuffer, 0);
                long secondTime = videoExtractor.getSampleTime();
                videoExtractor.advance();
                long thirdTime = videoExtractor.getSampleTime();
                sampleTime = Math.abs(thirdTime - secondTime);
            }
            videoExtractor.unselectTrack(videoTrackIndex);
            videoExtractor.selectTrack(videoTrackIndex);

            long stampTimeAudio = 0;
            ByteBuffer audioByteBuffer = ByteBuffer.allocate(500 * 1024);
            {
                // 获取帧之间的间隔时间
                audioExtractor.readSampleData(audioByteBuffer, 0);
                if (audioExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC) {
                    audioExtractor.advance();
                }
                audioExtractor.readSampleData(audioByteBuffer, 0);
                long secondTime = audioExtractor.getSampleTime();
                audioExtractor.advance();
                long thirdTime = audioExtractor.getSampleTime();
                stampTimeAudio = Math.abs(thirdTime - secondTime);
            }
            audioExtractor.unselectTrack(audioTrackIndex);
            audioExtractor.selectTrack(audioTrackIndex);

            while (true) {
                int readVideoSampleSize = videoExtractor.readSampleData(byteBuffer, 0);
                if (readVideoSampleSize < 0) {
                    break;
                }
                videoBufferInfo.size = readVideoSampleSize;
                videoBufferInfo.presentationTimeUs += sampleTime;
                videoBufferInfo.offset = 0;
                videoBufferInfo.flags = videoExtractor.getSampleFlags();
                mediaMuxer.writeSampleData(writeVidoeTrackIndex, byteBuffer, videoBufferInfo);
                videoExtractor.advance();
            }

            while (true) {
                int readAudioSampleSize = audioExtractor.readSampleData(audioByteBuffer, 0);
                if (readAudioSampleSize < 0) {
                    break;
                }

                audioBufferInfo.size = readAudioSampleSize;
                audioBufferInfo.presentationTimeUs += stampTimeAudio;
                audioBufferInfo.offset = 0;
                audioBufferInfo.flags = audioExtractor.getSampleFlags();
                mediaMuxer.writeSampleData(writeAudioTrackIndex, audioByteBuffer, audioBufferInfo);
                audioExtractor.advance();
            }

            mediaMuxer.stop();
            mediaMuxer.release();
            audioExtractor.release();
            videoExtractor.release();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
