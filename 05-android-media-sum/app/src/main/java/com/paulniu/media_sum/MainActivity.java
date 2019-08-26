package com.paulniu.media_sum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private List<String> permissionList = new ArrayList<>();
    private static final int ACCESS_FINE_ERROR_CODE = 0x0245;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private SurfaceView svRecordVideoSurfaceView;
    private RelativeLayout rlRecordVideoTopContainer;
    private TextView tvRecordVideoTitle;
    private ImageView ivRecordVidoStartAndStop;
    private TextView tvRecordVideoTime;
    private ImageView ivRecordVideoAddBG;
    private ImageView ivRecordVideoRotate;
    //////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////
    private SurfaceHolder mSurfaceHolder;
    private MediaRecorder mRecorder;
    private Camera mCamera;
    private Timer timer;
    //////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////
    private boolean mStartedFlag = false;
    private String saveVideoPath = null;
    private static int timeCount = 0;
    /////////////////////////////////////////////////////////////////////////////////////////////

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            timeCount++;
            // 设置显示录制时间
            showRecordVideoTime(timeCount);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_main);

        // 申请权限
        askPermission(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, ACCESS_FINE_ERROR_CODE);

        initSurfaceView();
    }

    /**
     * 申请权限
     */
    private void askPermission(String[] permissions, int permissionCode) {
        permissionList.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            // 根据当前未授权的权限，开始申请权限
            if (permissionList.isEmpty()) {
            } else {
                permissions = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(this, permissions, permissionCode);
            }
        }
    }

    /**
     * 初始化surfaceView
     */
    private void initSurfaceView() {
        initView();
        initListener();
        mSurfaceHolder = svRecordVideoSurfaceView.getHolder();
        // 设置屏幕分辨率
        mSurfaceHolder.setFixedSize(640, 480);
        // 设置surfaceholder的类型
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 给surfaceholder设置监听事件
        mSurfaceHolder.addCallback(this);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        svRecordVideoSurfaceView = findViewById(R.id.svRecordVideoSurfaceView);
        rlRecordVideoTopContainer = findViewById(R.id.rlRecordVideoTopContainer);
        tvRecordVideoTitle = findViewById(R.id.tvRecordVideoTitle);
        ivRecordVidoStartAndStop = findViewById(R.id.ivRecordVidoStartAndStop);
        tvRecordVideoTime = findViewById(R.id.tvRecordVideoTime);
        ivRecordVideoAddBG = findViewById(R.id.ivRecordVideoAddBG);
        ivRecordVideoRotate = findViewById(R.id.ivRecordVideoRotate);
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        if (null != ivRecordVidoStartAndStop) {
            ivRecordVidoStartAndStop.setOnClickListener(this);
        }
        if (null != ivRecordVideoAddBG) {
            ivRecordVideoAddBG.setOnClickListener(this);
        }
        if (null != ivRecordVideoRotate) {
            ivRecordVideoRotate.setOnClickListener(this);
        }
    }

    /**
     * 录制视频
     */
    private void recordVideo() {
        try {
            if (null == mRecorder) {
                mRecorder = new MediaRecorder();
            }
            // 解锁camera
            mCamera.unlock();
            // 设置输出格式为mpeg，此格式音频编码格式必须为AAC，否则音频无法播放
            mRecorder.setCamera(mCamera);
            // 设置录制时的数据来源--摄像头
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // 设置录制时的数据来源--麦克风
            mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            // 设置输出格式
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // 设置视频输出格式
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            // 设置音频输出格式
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            // 设置视频输出大小(跟分辨率相关)
            mRecorder.setVideoSize(640, 480);
            // 设置视频的采样率
            mRecorder.setVideoEncodingBitRate(600 * 1024);
            // 设置预览时的surface
            mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

            // 输出文件地址
            String savePath = FileUtils.getVideoSavePath();
            if (!TextUtils.isEmpty(savePath)) {
                File dir = new File(savePath + "/paulniu");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                saveVideoPath = dir + "/" + getDate() + ".mp4";
                // 设置保存的文件地址
                mRecorder.setOutputFile(saveVideoPath);
                // 设置相机旋转角度，因为相机默认是水平拍摄的
                mRecorder.setOrientationHint(90);
                // 开始录制
                mRecorder.prepare();
                mRecorder.start();
                // 开始计时，并且将计时的时间动态显示到屏幕上
                startRecordTime();
                // 设置计时文本可见
                tvRecordVideoTime.setVisibility(View.VISIBLE);
                // 设置图标更改
                ivRecordVidoStartAndStop.setImageResource(R.mipmap.stop);
                // 设置标识为true
                mStartedFlag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 停止录制视频
     */
    private void stopRecordVideo() {
        try {
            if (mStartedFlag) {
                if (mRecorder != null) {
                    mRecorder.stop();
                    mRecorder.reset();
                }
                if (timerTask != null) {
                    timerTask.cancel();
                }
                if (timer != null) {
                    timer.cancel();
                }
                Toast.makeText(MainActivity.this, "视频录制完成，保存路径为" + FileUtils.getVideoSavePath() + "; 录制时长为" + timeCount + "秒", Toast.LENGTH_LONG).show();
                // 设置计时文本不可用
                tvRecordVideoTime.setVisibility(View.GONE);
                // 修改图片
                ivRecordVidoStartAndStop.setImageResource(R.mipmap.start);
                // 设置标识为false
                mStartedFlag = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 开始计时
     */
    private void startRecordTime() {
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 设置视频录制保存之后文件名
     *
     * @return
     */
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
     * 设置显示录制时间
     */
    private void showRecordVideoTime(int timeCount) {
        // 先格式化时间样式
        int s = timeCount % 60;
        int m = timeCount / 60;
        String time = "";
        if (m < 10) {
            if (s < 10) {
                time = "00:0" + s;
            } else {
                time = "00:" + s;
            }
        } else {
            if (s < 10) {
                time = "0" + m + ":0" + s;
            } else {
                time = s + ":" + s;
            }
        }
        final String finalTime = time;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvRecordVideoTime.setText(finalTime);
            }
        });
    }

    /**
     * 开始预览
     */
    private void startPreview(SurfaceHolder holder) {
        try {
            if (mCamera == null) {
                // 设置默认打开后置摄像头
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
            }
            if (null != mRecorder) {
                // 设置默认摄像头旋转角度
                mCamera.setDisplayOrientation(90);
                // 设置摄像头预览的对象
                mCamera.setPreviewDisplay(holder);
                Camera.Parameters parameters = mCamera.getParameters();
                // 设置camera自动对焦
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes != null) {
                    for (String mode : focusModes) {
                        mode.contains("continuous-video");
                        parameters.setFocusMode("continuous-video");
                    }
                }
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 为上一个视频添加bgm
     */
    private void addBGM(){
        
    }

    /**
     * 当surfaceview被创建的时候
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mSurfaceHolder = surfaceHolder;
        startPreview(surfaceHolder);
    }

    /**
     * 当SurfaceView被改变的时候
     *
     * @param surfaceHolder
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        this.mSurfaceHolder = surfaceHolder;
    }

    /**
     * 当surfaceview被销毁的时候
     *
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = null;
        svRecordVideoSurfaceView = null;
        if (null != mRecorder) {
            mRecorder.release();
            mRecorder = null;
        }
    }

    /**
     * 权限声明完成之后的回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 没有赋予的权限个数
        int unGrantCount = 0;
        for (int i = 0; i < grantResults.length; i++) {
            boolean showRequest = ActivityCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED;
            if (showRequest) {
                unGrantCount++;
            }
        }
        if (unGrantCount > 0) {

        } else {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeCount = 0;
        mStartedFlag = false;
        if (null != ivRecordVidoStartAndStop) {
            ivRecordVidoStartAndStop.setImageResource(R.mipmap.start);
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (mRecorder != null) {
            // 清除recorder设置
            mRecorder.reset();
            // 释放recorder对象
            mRecorder.release();
            mRecorder = null;
            // 为后续使用锁定摄像头
            mCamera.lock();
        }
        if (mCamera != null) {
            // 释放摄像头资源
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivRecordVidoStartAndStop:
                // 点击开始和停止按钮
                if (!mStartedFlag) {
                    recordVideo();
                } else {
                    stopRecordVideo();
                }
                break;
            case R.id.ivRecordVideoAddBG:
                // 选择为上一个视频添加bgm
                addBGM();
                break;
            case R.id.ivRecordVideoRotate:
                // 翻转相机
                break;
        }
    }

}
