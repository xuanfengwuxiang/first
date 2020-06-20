package com.xuanfeng.testcomponent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.xuanfeng.testcomponent.utils.ActionUtil;


public class TestTouchView extends View {
    public TestTouchView(Context context) {
        super(context);
    }

    public TestTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    int count = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("testTouch", "View获取事件:" + ActionUtil.getActionName(event.getAction()));

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (count <3) {
                    count++;
                    return true;

                } else {
                    count++;
                    return false;
                }


            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }
}
