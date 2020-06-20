package com.xuanfeng.weather.module.news.activity;

import android.os.Bundle;

import com.xuanfeng.weather.R;
import com.xuanfeng.weather.widget.SurfaceViewL;
import com.xuanfeng.xflibrary.mvp.BaseActivity;
import com.xuanfeng.xflibrary.mvp.BasePresenter;

public class SurfaceViewActivity extends BaseActivity {


    private SurfaceViewL mSurfaceViewL;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initData(Bundle bundle) {
        mSurfaceViewL = findViewById(R.id.surfaceView);
    }

    @Override
    public int getStatusBarColorResId() {
        return 0;
    }


}
