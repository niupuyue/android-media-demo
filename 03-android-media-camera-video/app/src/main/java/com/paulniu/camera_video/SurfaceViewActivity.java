package com.paulniu.camera_video;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Coder: niupuyue
 * Date: 2019/8/6
 * Time: 18:33
 * Desc: 通过SurFaceView预览Camera
 * Version:
 */
public class SurfaceViewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SurfaceViewActivity.class.getSimpleName();

    // SurfaceView预览相机
    private SurfaceView surfaceview;
    // 获取当前相机的id(我们要使用前置相机)
    private String mCameraId;
    private Size mPreviewSize;
    private HandlerThread mCameraThread;
    private Handler mCameraHandler;
    // 摄像头驱动
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CaptureRequest mCaptureRequest;
    private CameraCaptureSession session;
    private SurfaceHolder holder;

    private boolean isSurfaceViewCreate = false;
    private int surfaceViewHolderWidth;
    private int surfaceViewHolderHeight;

    private boolean isFontCamera = true;

    private Button btn_surfaceview_font;
    private Button btn_surfaceview_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surfaceview);
        surfaceview = findViewById(R.id.surfaceview);

        btn_surfaceview_font = findViewById(R.id.btn_surfaceview_font);
        btn_surfaceview_back = findViewById(R.id.btn_surfaceview_back);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initCameraThread();

        holder = surfaceview.getHolder();
        surfaceview.setZOrderMediaOverlay(true);
        // 设置半透明
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(mSurfaceHolderCallback);
    }

    private void initCameraThread() {
        mCameraThread = new HandlerThread("CameraSufaceViewThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }

    private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
//            surfaceViewHolderWidth = surfaceHolder.getSurfaceFrame().width();
//            surfaceViewHolderHeight = surfaceHolder.getSurfaceFrame().height();
//            isSurfaceViewCreate = true;
            // 设置摄像头的基本配合信息，宽高跟SurfaceView的尺寸相关
            setupCamerar(surfaceHolder.getSurfaceFrame().width(), surfaceHolder.getSurfaceFrame().height());
            openCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };

    /**
     * 设置Camera的基本配置信息
     *
     * @param width
     * @param height
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupCamerar(int width, int height) {
        // 获取摄像头管理者CameraManager
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            // 遍历所有的摄像头
            for (String cameraId : cameraManager.getCameraIdList()) {
                // CameraCharacteristics 该属性用来描述摄像头驱动信息
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                // 获取摄像头相对于屏幕的方向
                Integer facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                // 设置打开后置摄像头
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT)
                    continue;

                // 获取StreamConfigurationMap，他是管理摄像头支持的所有输出格式和尺寸
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                assert map != null;
                mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                mCameraId = cameraId;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 选择sizeMap中大于并最接近width和height的size
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size left, Size right) {
                    return Long.signum(left.getWidth() * left.getHeight() - right.getWidth() * right.getHeight());
                }
            });
        }
        return sizeMap[0];
    }

    /**
     * 打开摄像头
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            Log.e(TAG, "cameraID = " + mCameraId);
            cameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, mCameraHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 当摄像机的状态发生改变的时候触发该监听
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice cameraDevice) {
            SurfaceViewActivity.this.cameraDevice = cameraDevice;
            startPreView();
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            if (SurfaceViewActivity.this.cameraDevice != null) {
                SurfaceViewActivity.this.cameraDevice.close();
                cameraDevice.close();
                SurfaceViewActivity.this.cameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice cameraDevice, int i) {
            if (SurfaceViewActivity.this.cameraDevice != null) {
                SurfaceViewActivity.this.cameraDevice.close();
                cameraDevice.close();
                SurfaceViewActivity.this.cameraDevice = null;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startPreView() {
        try {
            Surface surface = holder.getSurface();
            mCaptureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            if (surface != null) {

                mCaptureRequestBuilder.addTarget(surface);
            } else {
                Log.e(TAG, "surface为空");
            }

            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    mCaptureRequest = mCaptureRequestBuilder.build();
                    session = cameraCaptureSession;
                    try {
                        session.setRepeatingRequest(mCaptureRequest, null, mCameraHandler);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                }
            }, mCameraHandler);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (session != null) {
            session.close();
            session = null;
        }
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_surfaceview_font:
                // 打开后置摄像头
                isFontCamera = true;
                // 设置摄像头的基本配合信息，宽高跟SurfaceView的尺寸相关
                setupCamerar(surfaceViewHolderWidth, surfaceViewHolderHeight);
                openCamera();
                break;
            case R.id.btn_surfaceview_back:
                // 打开前置摄像头
                isFontCamera = false;
                // 设置摄像头的基本配合信息，宽高跟SurfaceView的尺寸相关
                setupCamerar(surfaceViewHolderWidth, surfaceViewHolderHeight);
                openCamera();
                break;
        }
    }
}
