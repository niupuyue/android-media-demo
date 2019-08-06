package com.paulniu.audio_collection.mediarecord;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import com.paulniu.audio_collection.utils.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Coder: niupuyue
 * Date: 2019/8/6
 * Time: 16:29
 * Desc: 用于实现录音，暂停，继续，停止，播放
 * 统一将语音录制成wav格式
 * Version:
 */
public class IMediaRecorder {

    // 默认采样率
    private static final int RECORDER_SIMPLE_RATE = 8000;
    // 最大采样率
    private static final int RECORDER_SIMPLE_BIG_RATE = 67000;

    private static IMediaRecorder mIMediaRecorder;

    public static IMediaRecorder getInstance() {
        if (mIMediaRecorder == null) {
            synchronized (IMediaRecorder.class) {
                if (mIMediaRecorder == null) {
                    mIMediaRecorder = new IMediaRecorder();
                }
            }
        }
        return mIMediaRecorder;
    }

    // 默认输出文件
    private File audioFile;
    // 语音录制系统封装好的对象
    private MediaRecorder mediaRecorder;
    // 语音播放系统封装好的对象
    private MediaPlayer mediaPlayer;
    // 语音播放管理对象
    private AudioManager audioManager;
    // 声明一个带有缓存的线程池
    private ExecutorService thread = Executors.newCachedThreadPool();


    /**
     * 开始语音录制
     * 语音的录制放在子线程中
     */
    public void startRecorder() {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                audioFile = FileUtils.createMediaRecordCacheFile(new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA).format(new Date()));
                try {
                    if (mediaRecorder == null) {
                        mediaRecorder = new MediaRecorder();
                    }
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setAudioSamplingRate(RECORDER_SIMPLE_RATE);
                    mediaRecorder.setAudioEncodingBitRate(RECORDER_SIMPLE_BIG_RATE);
                    mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
                    mediaRecorder.prepare();

                    mediaRecorder.start();
                } catch (Exception ex) {
                    if (mediaRecorder != null) {
                        mediaRecorder.reset();
                        mediaRecorder.release();
                        mediaRecorder = null;
                    }
                    ex.printStackTrace();
                    return;
                }
            }
        });
    }

    /**
     * 停止录制语音
     */
    public void stopRecorder() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    /**
     * 播放语音
     */
    public void playRecorder() {
        if (audioFile != null && audioFile.exists()) {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            // 语音播放完成回调
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.e("npl", "语音播放完成");
                }
            });
            // 设置语音播放失败监听回调
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                    if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                        Log.e("NPL", "语音播放失败");
                    } else {
                        Log.e("NPL", "取消语音播放");
                    }
                    return false;
                }
            });
            if (!mediaPlayer.isPlaying()) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(audioFile.getAbsolutePath());
                    // 异步操作播放语音
                    mediaPlayer.prepareAsync();
                    // 语音播放准备完毕监听回调
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            // 开始播放
                            mediaPlayer.start();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
