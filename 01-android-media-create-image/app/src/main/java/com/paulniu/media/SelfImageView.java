package com.paulniu.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2019-08-05
 * Time: 22:41
 * Desc: 自定义view，将图片资源绘制到当前View中
 * Version:
 */
public class SelfImageView extends View {

    private Paint mPaint;
    private Bitmap mBitmap;
    private Context context;

    private  Rect rect;
    private Rect descRect;

    public SelfImageView(Context context) {
        this(context, null);
    }

    public SelfImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelfImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher1);

        rect = new Rect(0, 0, mBitmap.getHeight(), mBitmap.getWidth());
        descRect = new Rect(100, 100, mBitmap.getHeight() + 100, mBitmap.getWidth() + 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, rect, descRect, mPaint);
    }
}
