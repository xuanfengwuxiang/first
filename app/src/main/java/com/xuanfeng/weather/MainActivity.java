package com.xuanfeng.weather;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import com.xuanfeng.xflibrary.mvp.BaseActivity;
import com.xuanfeng.xflibrary.mvp.BasePresenter;
import com.xuanfeng.xflibrary.utils.StatusBarUtil;
import com.xuanfeng.weather.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<BasePresenter, ViewModel, ActivityMainBinding> {


    @Override
    public void onBackPressed() {
        MainUtil.exitApp(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initData(Bundle bundle) {
        MainUtil.setCheckListener(mBinding.rbParent, this, mBinding.vpMain, mBinding.activityMain);
        MainUtil.setViewPagerAdapter(this, mBinding.vpMain);
        mBinding.rbOne.setChecked(true);
        StatusBarUtil.setTranslucent4_4(this);
    }

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }
}
