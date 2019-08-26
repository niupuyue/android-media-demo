package com.paulniu.media_muxer;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Coder: niupuyue
 * Date: 2019/8/13
 * Time: 9:56
 * Desc:
 * Version:
 */
public class RecordVideoActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private ImageView iv_start_stop_record;
    private SurfaceView recordSurfaceView;

    private boolean mStartedFlag = false;
    private SurfaceHolder mSurfaceHolder;

    private MediaRecorder mRecorder;
    private Camera camera;
    private String path;
    private TimerTask timerTask;
    private int recordTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置为全屏展示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_record_video);

        iv_start_stop_record = findViewById(R.id.iv_start_stop_record);
        iv_start_stop_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecordVideo();
            }
        });

        recordSurfaceView = findViewById(R.id.recordSurfaceView);
        mSurfaceHolder = recordSurfaceView.getHolder();
        // 设置屏幕分辨率
        mSurfaceHolder.setFixedSize(640, 480);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
        SurfaceHolder holder = recordSurfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void startRecordVideo() {
        if (!mStartedFlag) {
            // 开始录制视频
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
            }
            try {
                // 解锁Camera
                // 设置输出格式为mpeg，此格式音频编码格式必须为AAC，否则网页无法播放
                camera.unlock();
                mRecorder.setCamera(camera);
                mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                mRecorder.setVideoSize(640, 480);
                mRecorder.setVideoEncodingBitRate(600 * 1024);
                mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

                // 设置输出地址
                String sdpath = getSDPath();
                if (!TextUtils.isEmpty(sdpath)) {
                    File dir = new File(sdpath + "/paulniu");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    path = dir + "/" + getDate() + ".mp4";
                    mRecorder.setOutputFile(path);
                    mRecorder.setOrientationHint(90);
                    mRecorder.prepare();
                    mRecorder.start();
                    // 开始计时
                    startRecordTimer();
                    mStartedFlag = true;
                    iv_start_stop_record.setImageResource(R.mipmap.stop_circle_line);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            // 停止录制
            if (mStartedFlag) {
                try {
                    mRecorder.stop();
                    if (timerTask != null) {

                    }
                    mRecorder.reset();
                    Toast.makeText(RecordVideoActivity.this,"录制完成，视频地址为"+path,Toast.LENGTH_LONG).show();
                    finish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                mStartedFlag = false;
            }
        }
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }
        return null;
    }

    public static String getDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);           // 获取年份
        int month = ca.get(Calendar.MONTH);         // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
        int minute = ca.get(Calendar.MINUTE);       // 分
        int hour = ca.get(Calendar.HOUR);           // 小时
        int second = ca.get(Calendar.SECOND);       // 秒
        String date = "" + year + (month + 1) + day + hour + minute + second;
        Log.d("npl", "date:" + date);
        return date;
    }

    /**
     * 开始一个计时器
     */
    private void startRecordTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recordTime++;
                        int m = recordTime / 60;
                        int s = recordTime % 60;
                        String strm = String.valueOf(m);
                        String strs = String.valueOf(s);
                        if (m < 10) {
                            strm = "0" + m;
                        }
                        if (s < 10) {
                            strs = "-" + s;
                        }
                        // 将计时信息展现出来 TODO

                    }
                });
            }
        };
        Timer recordTimer = new Timer(true);
        recordTimer.schedule(timerTask, 0, 1000);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // 将这个surfaceHolder赋值给在oncreate中创建的surfaceHolder
        this.mSurfaceHolder = surfaceHolder;
        startPreView(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // 将当前的surfaceHolder赋值给oncreate中创建的surfaceHolder
        this.mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // surfaceDestoryed被调用的时候将对象置为null
        mSurfaceHolder = null;
        recordSurfaceView = null;
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }

    /**
     * 开始预览
     */
    private void startPreView(SurfaceHolder holder) {
        try {
            if (camera == null) {
                // 默认打开后置摄像头
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
            }
            if (mRecorder != null) {
                camera.setDisplayOrientation(90);
                camera.setPreviewDisplay(holder);
                Camera.Parameters parameters = camera.getParameters();
                // Camera自动对焦
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes != null) {
                    for (String mode : focusModes) {
                        mode.contains("continuous-video");
                        parameters.setFocusMode("continuous-video");
                    }
                }
                camera.setParameters(parameters);
                camera.startPreview();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        recordTime = 0;
        mStartedFlag = false;
        iv_start_stop_record.setImageResource(R.mipmap.record_circle_line);
        if (timerTask != null) {
            timerTask.cancel();
        }
        // 如果正在使用MediaRecorder，需要释放
        releaseMediaRecorder();
        // 释放摄像头
        releaseCamera();
    }

    private void releaseMediaRecorder() {
        if (mRecorder != null) {
            // 清除recorder设置
            mRecorder.reset();
            // 释放recorder对象
            mRecorder.release();
            mRecorder = null;
            // 为后续使用锁定摄像头
            camera.lock();
        }
    }

    private void releaseCamera() {
        if (camera != null) {
            // 释放摄像头资源
            camera.release();
            camera = null;
        }
    }
}
