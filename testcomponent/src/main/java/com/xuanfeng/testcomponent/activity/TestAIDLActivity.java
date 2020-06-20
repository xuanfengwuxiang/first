package com.xuanfeng.testcomponent.activity;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.xuanfeng.xflibrary.mvp.BaseActivity;
import com.xuanfeng.xflibrary.mvp.BasePresenter;
import com.xuanfeng.testcomponent.R;
import com.xuanfeng.testcomponent.databinding.ActivityTestAidlBinding;


//AIDL测试界面
public class TestAIDLActivity extends BaseActivity<BasePresenter, ViewModel, ActivityTestAidlBinding> {


    @Override
    public int getLayoutId() {
        return R.layout.activity_test_aidl;
    }

    @Override
    public BasePresenter initPresenter() {
        mBinding.setListener(this);
        return null;
    }

    @Override
    public void initData(Bundle bundle) {
        mBinding.tvTittle.setText("AIDL客户端");
    }

    @Override
    public int getStatusBarColorResId() {
        return R.color.baseThemeColor;
    }

    public void onClick(View view) {
        int i = view.getId();//发送消息到服务端
        if (i == R.id.iv_left) {
            finish();
        } else if (i == R.id.dtv_switch) {
            if (mBinding.first.getVisibility() == View.VISIBLE) {
                mBinding.first.setVisibility(View.GONE);
                mBinding.second.setVisibility(View.VISIBLE);
            } else {
                mBinding.first.setVisibility(View.VISIBLE);
                mBinding.second.setVisibility(View.GONE);
            }
        } else if (i == R.id.tab) {
            if (mBinding.clRoot.getVisibility() == View.VISIBLE) {
                mBinding.clRoot.setVisibility(View.GONE);

            } else {
                mBinding.clRoot.setVisibility(View.VISIBLE);
                mBinding.first.setVisibility(View.GONE);
                mBinding.second.setVisibility(View.VISIBLE);
            }
        }
    }
}
