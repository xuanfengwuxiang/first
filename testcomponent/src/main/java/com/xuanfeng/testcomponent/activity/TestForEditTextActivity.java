package com.xuanfeng.testcomponent.activity;

import android.os.Bundle;

import com.xuanfeng.xflibrary.mvp.BaseActivity;
import com.xuanfeng.xflibrary.mvp.BasePresenter;
import com.xuanfeng.testcomponent.R;

public class TestForEditTextActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_test_for_edit_text;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    public void initData(Bundle bundle) {
        //do nothing
    }

    @Override
    public int getStatusBarColorResId() {
        return R.color.white;
    }


}
