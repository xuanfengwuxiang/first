package com.xuanfeng.testcomponent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.xuanfeng.testcomponent.utils.ActionUtil;


public class TestTouchLinearLayout extends LinearLayout {
    int count = 0;

    public TestTouchLinearLayout(Context context) {
        super(context);
    }

    public TestTouchLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestTouchLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return false;
            case MotionEvent.ACTION_MOVE:
                if (count <3) {
                    count++;
                    return false;

                } else {
                    count++;
                    return true;
                }


            case MotionEvent.ACTION_UP:

                break;
        }
        Log.i("testTouch","拦截了");
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("testTouch", "ViewGroup获取事件:" + ActionUtil.getActionName(event.getAction()));

//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                if (count <3) {
//                    count++;
//                    return true;
//
//                } else {
//                    count++;
//                    return false;
//                }
//
//
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
//        return true;
       return true;
    }
}
