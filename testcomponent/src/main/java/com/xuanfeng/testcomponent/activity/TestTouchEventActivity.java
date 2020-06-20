package com.xuanfeng.testcomponent.activity;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import com.xuanfeng.xflibrary.mvp.BaseActivity;
import com.xuanfeng.xflibrary.mvp.BasePresenter;
import com.xuanfeng.testcomponent.R;
import com.xuanfeng.testcomponent.databinding.ActivityTestTouchEventBinding;


/**
 * 事件分发测试界面
 */
public class TestTouchEventActivity extends BaseActivity<BasePresenter, ViewModel,ActivityTestTouchEventBinding> {


    @Override
    public int getLayoutId() {
        return R.layout.activity_test_touch_event;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initData(Bundle bundle) {
        //此句无效：因为每次事件分发都会将标志位置为初始状态
        mBinding.button.getParent().requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public int getStatusBarColorResId() {
        return R.color.baseThemeColor;
    }

}
