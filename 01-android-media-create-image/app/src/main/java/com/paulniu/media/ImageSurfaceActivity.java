package com.paulniu.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2019-08-05
 * Time: 22:49
 * Desc:
 * Version:
 */
public class ImageSurfaceActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceView surfaceView;
    private Thread mThread = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    Bitmap mBitmap = BitmapFactory.decodeResource(ImageSurfaceActivity.this.getResources(), R.mipmap.ic_launcher1);
                    SurfaceHolder holder = surfaceView.getHolder();
                    Canvas canvas = holder.lockCanvas();// 获取画布
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 声明一个画笔
                    Rect rect = new Rect(0, 0, mBitmap.getHeight(), mBitmap.getWidth());
                    Rect desRect = getDesRect(mBitmap);
                    canvas.drawBitmap(mBitmap, rect, desRect, paint);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
    );

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (!mThread.isInterrupted()) {
            mThread.interrupt();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        initSurfaceView();
    }

    private void initSurfaceView() {
        surfaceView.getHolder().addCallback(this);
    }

    private Rect getDesRect(Bitmap bitmap) {
        int bimapHeight = bitmap.getHeight();
        int bimapWidth = bitmap.getWidth();
        int viewWidth = surfaceView.getWidth();
        int viewHeight = surfaceView.getHeight();
        float bimapRatio = (float) bimapWidth / (float) bimapHeight; // 宽高比
        float screenRatio = (float) viewWidth / (float) viewHeight;
        int factWidth;
        int factHeight;
        int x1, y1, x2, y2;
        if (bimapRatio > screenRatio) {
            factWidth = viewWidth;
            factHeight = (int) (factWidth / bimapRatio);
            x1 = 0;
            y1 = (viewHeight - factHeight) / 2;
        } else if (bimapRatio < screenRatio) {
            factHeight = viewHeight;
            factWidth = (int) (factHeight * bimapRatio);
            x1 = (viewWidth - factWidth) / 2;
            y1 = 0;
        } else {
            factWidth = bimapWidth;
            factHeight = bimapHeight;
            x1 = 0;
            y1 = 0;
        }
        x2 = x1 + factWidth;
        y2 = y1 + factHeight;
        return new Rect(x1, y1, x2, y2);
    }

}
