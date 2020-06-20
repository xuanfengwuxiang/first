package com.xuanfeng.testcomponent.activity;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.xuanfeng.xflibrary.mvp.BaseActivity;
import com.xuanfeng.xflibrary.mvp.BasePresenter;
import com.xuanfeng.testcomponent.R;
import com.xuanfeng.testcomponent.databinding.ActivityTestShareAnimBinding;


public class TestShareAnimActivity extends BaseActivity<BasePresenter, ViewModel, ActivityTestShareAnimBinding> implements View.OnClickListener {


    @Override
    public int getLayoutId() {
        return R.layout.activity_test_share_anim;
    }

    @Override
    public BasePresenter initPresenter() {
        mBinding.setListener(this);
        return null;
    }

    @Override
    public void initData(Bundle bundle) {
        mBinding.tvTittle.setText("share转场动画");
    }

    @Override
    public int getStatusBarColorResId() {
        return R.color.white;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_left) {
            finishAfterTransition();
        }
    }

}
