package com.xuanfeng.weather.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuanfengwuxiang on 2018/4/12.
 */

public class DoodleImageView extends ImageView {

    //全局变量
    private Paint mPaint;
    private Path mPath;
    private float mDrawSize;
    private String mColorString;

    //缓存变量
    private List<PathDrawingInfo> mDrawingInfoList;//每画一笔的信息集合
    private Bitmap mBufferBitmap;//中间变量
    private Canvas mBufferCanvas;//中间变量
    private float mLastX;
    private float mLastY;

    //常量
    private static final int MAX_CACHE_STEP = 20;


    public DoodleImageView(Context context) {
        this(context, null);
    }

    public DoodleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoodleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDrawingCacheEnabled(true);
        initData();
    }

    private void initData() {
        //初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawSize = 10;
        mPaint.setStrokeWidth(mDrawSize);
        mColorString = "#FF4081";
        mPaint.setColor(Color.parseColor(mColorString));
    }

    //暴露的方法区
    public void setPenColor(int color) {
        mPaint.setColor(color);
    }

    public void setPenAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    public void setPenSize(float drawSize) {
        mPaint.setStrokeWidth(drawSize);
    }

    public void unDo() {
        int size = mDrawingInfoList == null ? 0 : mDrawingInfoList.size();
        if (size > 0) {
            mDrawingInfoList.remove(mDrawingInfoList.size() - 1);
            reDraw();
        }
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = getDrawingCache();
        Bitmap result = Bitmap.createBitmap(bitmap);
        destroyDrawingCache();
        return result;
    }


    //重新绘制
    private void reDraw() {
        if (mDrawingInfoList != null) {
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            for (PathDrawingInfo info : mDrawingInfoList) {
                mBufferCanvas.drawPath(info.path, info.paint);
            }
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBufferBitmap != null) {
            canvas.drawBitmap(mBufferBitmap, 0, 0, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN://设置起始点
                mLastX = x;
                mLastY = y;
                if (mPath == null) {
                    mPath = new Path();
                }
                mPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE://设置终点，画到中间的bitmap
                mPath.quadTo(mLastX, mLastY, (mLastX + x) / 2, (mLastY + y) / 2); //这里终点设为两点的中心点的目的在于使绘制的曲线更平滑，如果终点直接设置为x,y，效果和lineto是一样的,实际是折线效果
                if (mBufferBitmap == null) {
                    initBuffer();
                }
                mBufferCanvas.drawPath(mPath, mPaint);
                invalidate();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP://保存画线
                saveDrawingPath();
                mPath.reset();
                break;
        }
        return true;
    }

    //保存所有画线
    private void saveDrawingPath() {
        if (mDrawingInfoList == null) {
            mDrawingInfoList = new ArrayList<>();
        }else if (mDrawingInfoList.size() == MAX_CACHE_STEP) {
            mDrawingInfoList.remove(0);
        }
        PathDrawingInfo pathDrawingInfo = new PathDrawingInfo();
        pathDrawingInfo.path = new Path(mPath);
        pathDrawingInfo.paint = new Paint(mPaint);
        mDrawingInfoList.add(pathDrawingInfo);
    }

    //初始化缓存画板，缓存位图
    private void initBuffer() {
        mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);
    }

    //缓存对象
    private class PathDrawingInfo {
        Paint paint;
        Path path;
    }
}
