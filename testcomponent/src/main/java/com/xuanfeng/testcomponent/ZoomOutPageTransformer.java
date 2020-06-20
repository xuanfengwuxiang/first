package com.xuanfeng.testcomponent;

import android.content.Context;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * 这个是Viewpager的滑动动画：缩放动画。
 */

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final int FLAG_DEFAULT_TRANSLATE = 150;
    private static final float FLAG_DEFAULT_OFFSET = 0.2f;
    private int maxTranslateOffsetX;
    private ViewPager viewPager;

    public ZoomOutPageTransformer(Context context) {
        maxTranslateOffsetX = dp2px(context, FLAG_DEFAULT_TRANSLATE);
    }

    public void transformPage(View view, float position) {
        if (viewPager == null) {
            viewPager = (ViewPager) view.getParent();
        }
        int leftInScreen = view.getLeft() - viewPager.getScrollX();
        int centerXInViewPager = leftInScreen + view.getMeasuredWidth() / 2;
        int offsetX = centerXInViewPager - viewPager.getMeasuredWidth() / 2;
        float offsetRate = (float) offsetX * FLAG_DEFAULT_OFFSET / viewPager.getMeasuredWidth();
        float scaleFactor = 1 - Math.abs(offsetRate);
        if (scaleFactor > 0) {
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setTranslationX(-maxTranslateOffsetX * offsetRate);
        }
    }

    /**
     * dp和像素转换
     */
    private int dp2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

}
