package com.paulniu.camera_video;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

/**
 * Coder: niupuyue
 * Date: 2019/8/7
 * Time: 19:00
 * Desc: 另外一种实现SurfaceView预览摄像头的方法
 * Version:
 */
public class SurfaceView2Activity extends AppCompatActivity {

    private SurfaceView surface_view;

    private Camera camera;

    private Camera.Size mPreviewSize;

    private ImageView icon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

        setContentView(R.layout.activity_surfaceview2);

        icon = findViewById(R.id.icon);

        surface_view = findViewById(R.id.surface_view);
        surface_view.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                camera.release();
            }
        });
        surface_view.setKeepScreenOn(true);

        camera = Camera.open();
        camera.setDisplayOrientation(90);

        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        camera.setParameters(parameters);
        // 通过PreviewCallback设置预览回调
        camera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                // 处理data
                // 获取尺寸，在进行转换的时候需要使用
                mPreviewSize = camera.getParameters().getPreviewSize();
                //取发YUVIMAGE
                YuvImage yuvImage = new YuvImage(
                        data,
                        ImageFormat.NV21,
                        mPreviewSize.width,
                        mPreviewSize.height,
                        null
                );
                // 声明字节输出流
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //yuvimage 转换成jpg格式
                yuvImage.compressToJpeg(new Rect(0, 0, mPreviewSize.width, mPreviewSize.height), 100, bos);// 80--JPG图片的质量[0-100],100最高
                byte[] imageBytes = bos.toByteArray();
                //将mImageBytes转换成bitmap
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;

                Bitmap mBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
                icon.setImageBitmap(mBitmap);
            }
        });
    }
}
