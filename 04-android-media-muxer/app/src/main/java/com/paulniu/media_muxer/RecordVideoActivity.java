package com.paulniu.media_muxer;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Calendar;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置为全屏展示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_record_video);

        iv_start_stop_record = findViewById(R.id.iv_start_stop_record);
        iv_start_stop_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                if (!TextUtils.isEmpty(sdpath)){
                    File dir = new File(sdpath + "paulniu");
                    if (!dir.exists()){
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
        }else {
            if (mStartedFlag){
                try {
                    mRecorder.stop();
                    if (timerTask != null){

                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
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

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
