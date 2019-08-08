package com.paulniu.camera_video;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Coder: niupuyue
 * Date: 2019/8/8
 * Time: 17:00
 * Desc: 视频录制
 * Version:
 */
public class VideoMediaRecordActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private SurfaceView video_surfaceview;
    private TextView tv_startandstop;
    private TextView tv_switch_camera;

    // 是否正在录制
    private boolean isRecording = false;
    // 当前摄像头的种类,前置/后置  默认打开后置摄像头
    private int cameraPosition = Camera.CameraInfo.CAMERA_FACING_BACK;
    // 摄像头是否已经开启
    private boolean isOpenCamera = false;

    // 屏幕高度
    private int screenHeight;
    // 屏幕宽度
    private int screenWidth;
    // 视频分辨率高度
    private int videoHeight = 0;
    // 视频分辨率宽度
    private int videoWidth = 0;
    // 照片分辨率
    public Camera.Size pictureSize;
    // 预览分辨率
    private Camera.Size previewSize;
    // SurfaceView操作对象
    private SurfaceHolder holder;
    // 视频录制对象
    private MediaRecorder mMediaRecorder;
    // Camera对象
    private Camera camera;
    //
    private String FilePath = Environment
            .getExternalStorageDirectory().getPath() + File.separator + "ninggegetest" + File.separator;
    // 视频录制文件
    private File mVecordFile;

    private MediaRecorder.OnErrorListener onErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Log.e("NPL", "errorMessage = " + what);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);

        video_surfaceview = findViewById(R.id.video_surfaceview);
        tv_startandstop = findViewById(R.id.tv_startandstop);
        tv_switch_camera = findViewById(R.id.tv_switch_camera);

        tv_startandstop.setOnClickListener(this);
        tv_switch_camera.setOnClickListener(this);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        holder = video_surfaceview.getHolder();
        holder.addCallback(this);
    }

    /**
     * 开始录制视频
     */
    private void startRecord() {
        createVideoRecordDir();
        initRecorder();
        isRecording = true;
        tv_startandstop.setText("结束");
    }

    /**
     * 创建语音录制的目录
     */
    private void createVideoRecordDir() {
        try {
            File sampleDir = new File(FilePath);
            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }
            File vecordDir = sampleDir;
            // 创建文件
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日_HH_mm_ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            mVecordFile = File.createTempFile(str, ".mp4", vecordDir);//mp4格式
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 初始化Recorder对象
     */
    private void initRecorder() {
        try {
            // 相机解锁
            camera.unlock();
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.reset();
            if (camera != null) {
                mMediaRecorder.setCamera(camera);
            }
            // 添加媒体录制错误监听
            mMediaRecorder.setOnErrorListener(onErrorListener);
            // 设置视频录制中视频资源和语音资源的来源
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            //后置摄像头是旋转90度
            if (cameraPosition == 0) {
                mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
            } else {
                //前置摄像头是旋转270度
                mMediaRecorder.setOrientationHint(270);
            }
            // 设置语音和视频的输出格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            // 设置视频的分辨率
            mMediaRecorder.setVideoSize(videoWidth, videoHeight);
            // 设置帧频率
            mMediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
            // 设置录制视频的帧率，必须放在帧频率和分辨率之后，否则会报错
            mMediaRecorder.setVideoFrameRate(50);
            // 设置视频输出路径
            mMediaRecorder.setOutputFile(mVecordFile.getAbsolutePath());
            // 开始录制
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 停止录制
     */
    private void stoprecord() {
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setPreviewDisplay(null);
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            mMediaRecorder = null;
            mMediaRecorder = new MediaRecorder();
        }
        mMediaRecorder.release();
        mMediaRecorder = null;
        isRecording = false;
        tv_startandstop.setText("开始");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // SurfaceView被创建
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
            // 获取摄像头的个数
            int i = Camera.getNumberOfCameras();
            if (i == 1) {
                // 只有一个摄像头
                camera = Camera.open();
            } else {
                camera = Camera.open(cameraPosition);
            }
            isOpenCamera = true;
            setCameraParams();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // SurfaceView 被销毁
        try {
            stoprecord();
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setCameraParams() {
        try {
            camera.setPreviewDisplay(holder);
            Camera.Parameters parameters = camera.getParameters();
            // 获取当前手机支持的刷新方式
            List<String> flashes = parameters.getSupportedFlashModes();
            // 获取默认的flushMode
            String flash = parameters.getFlashMode();
            // 查看摄像头flashmode是否存在
            if (flashes != null && Camera.Parameters.FLASH_MODE_OFF.equals(flash)) {
                // 需要关闭摄像头flashmode
                if (flashes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                }
            }
            // 计算SurfaceView预览的分辨率尺寸
            float precent = calPreviewPrecent();
            List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
            //因为有些手机videoSizes不提供  比如华为mate7  那样的话就只能拿照相机预览数据了 当然预览数据尺寸不知道能不能当视频录制尺寸
            List<Camera.Size> videoSizes = camera.getParameters().getSupportedVideoSizes();
            previewSize = getPreviewMaxSize(supportedPreviewSizes, precent);
            //
            if (videoSizes != null) {
                for (Camera.Size _s : videoSizes) {
                    float videoS = (float) _s.width / _s.height;
                    if (videoS == precent && _s.width < previewSize.width && _s.height < previewSize.height) {
                        videoWidth = _s.width;
                        videoHeight = _s.height;
                        continue;
                    }
                }
            }
            if (videoHeight == 0 && videoWidth == 0) {
                // 如果没有拿到数据，就去当前的预览数据
                videoWidth = previewSize.width;
                videoHeight = previewSize.height;
            }
            // 获取摄像头支持的分辨率
            List<Camera.Size> supportPictureSizes = parameters.getSupportedPictureSizes();
            pictureSize = findSizeFromList(supportPictureSizes, previewSize);
            if (pictureSize == null) {
                pictureSize = getPictureMaxSize(supportPictureSizes, previewSize);
            }
            // 设置图片的分辨率，需要在摄像头支持的范围之内选择
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            // 设置预览尺寸，需要在摄像头支持的范围之内
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            parameters.setJpegQuality(70);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(90);
            camera.startPreview();
            camera.cancelAutoFocus();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 计算高度和宽度的比值
     *
     * @return
     */
    private float calPreviewPrecent() {
        float d = screenHeight;
        return d / screenWidth;
    }

    private Camera.Size findSizeFromList(List<Camera.Size> supportedPictureSizes, Camera.Size size) {
        Camera.Size s = null;
        if (supportedPictureSizes != null && !supportedPictureSizes.isEmpty()) {
            for (Camera.Size su : supportedPictureSizes) {
                if (size.width == su.width && size.height == su.height) {
                    s = su;
                    break;
                }
            }
        }
        return s;
    }

    // 根据摄像头的获取与屏幕分辨率最为接近的一个分辨率
    private Camera.Size getPictureMaxSize(List<Camera.Size> l, Camera.Size size) {
        Camera.Size s = null;
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i).width >= size.width && l.get(i).height >= size.width
                    && l.get(i).height != l.get(i).width) {
                if (s == null) {
                    s = l.get(i);
                } else {
                    if (s.height * s.width > l.get(i).width * l.get(i).height) {
                        s = l.get(i);
                    }
                }
            }
        }
        return s;
    }

    /**
     * 根据摄像头的获取与屏幕分辨率最为接近的一个分辨率
     *
     * @param supportSize
     * @return
     */
    private Camera.Size getPreviewMaxSize(List<Camera.Size> supportSize, float precent) {
        int idx_best = 0;
        int best_width = 0;
        float best_diff = 100.0f;
        for (int i = 0; i < supportSize.size(); i++) {
            int w = supportSize.get(i).width;
            int h = supportSize.get(i).height;
            if (w * h < screenHeight * screenWidth)
                continue;
            float previewPercent = (float) w / h;
            float diff = Math.abs(previewPercent - precent);
            if (diff < best_diff) {
                idx_best = i;
                best_diff = diff;
                best_width = w;
            } else if (diff == best_diff && w > best_width) {
                idx_best = i;
                best_diff = diff;
                best_width = w;
            }
        }
        return supportSize.get(idx_best);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_switch_camera:
                // 切换摄像头  前置/后置
                if (isRecording) {
                    stoprecord();
                }
                if (cameraPosition == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    cameraPosition = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    cameraPosition = Camera.CameraInfo.CAMERA_FACING_BACK;
                }

                if (isOpenCamera) {
                    // 停止摄像头的预览
                    camera.stopPreview();
                    // 释放摄像头资源
                    camera.release();
                    // 将摄像头置为null
                    camera = null;
                    camera = Camera.open(cameraPosition);
                    setCameraParams();
                }
                break;
            case R.id.tv_startandstop:
                // 录制视频
                if (!isRecording) {
                    startRecord();
                } else {
                    stoprecord();
                }
                break;
        }
    }
}
