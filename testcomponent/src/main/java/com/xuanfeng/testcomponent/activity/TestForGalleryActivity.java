package com.xuanfeng.testcomponent.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.PagerAdapter;

import com.xuanfeng.xflibrary.mvp.BaseActivity;
import com.xuanfeng.xflibrary.mvp.BasePresenter;
import com.xuanfeng.testcomponent.R;
import com.xuanfeng.testcomponent.databinding.ActivityTestForGalleryBinding;
import com.xuanfeng.testcomponent.ZoomOutPageTransformer;
import com.xuanfeng.testcomponent.adapter.ViewPagerPhotoTestAdapter;


import java.util.ArrayList;
import java.util.List;



/**
 * 此界面实现，ViewPager一屏展示3张图片
 */

public class TestForGalleryActivity extends BaseActivity<BasePresenter, ViewModel,ActivityTestForGalleryBinding> {




    private void setViewPager() {
        List<Integer> photoUrls = new ArrayList<>();
        photoUrls.add(R.drawable.ic_scenery1);
        photoUrls.add(R.drawable.ic_scenery2);
        photoUrls.add(R.drawable.ic_scenery3);
        photoUrls.add(R.drawable.ic_scenery1);
        photoUrls.add(R.drawable.ic_scenery2);
        photoUrls.add(R.drawable.ic_scenery3);
        PagerAdapter pagerAdapter = new ViewPagerPhotoTestAdapter(this, photoUrls);
        mBinding.vpPhoto.setAdapter(pagerAdapter);
        mBinding.vpPhoto.setPageMargin(40);//设置页与页之间的间距
        mBinding.vpPhoto.setPageTransformer(true, new ZoomOutPageTransformer(this));//设置缩放动画
        mBinding.vpPhoto.setOffscreenPageLimit(3);//设置幕后item的缓存数目。功能是预加载的那个图片可以直接缩小。不然的话，图片不会自动缩小
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_for_gallery;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    public void initListener() {
        mBinding.container.setOnTouchListener(new View.OnTouchListener() {//将父类的touch事件分发至viewPager，否则只能滑动中间的一个view对象
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mBinding.vpPhoto.dispatchTouchEvent(event);
            }
        });
    }

    @Override
    public void initData(Bundle bundle) {
        initListener();
        setViewPager();
    }

    @Override
    public int getStatusBarColorResId() {
        return R.color.white;
    }


}
