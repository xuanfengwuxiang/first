package com.xuanfeng.weather.module.loseweight;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.xuanfeng.xflibrary.mvp.BaseActivity;
import com.xuanfeng.xflibrary.mvp.BasePresenter;
import com.xuanfeng.weather.R;
import com.xuanfeng.weather.databinding.ActivityLoseWeightCalculatorBinding;

public class LoseWeightCalculatorActivity extends BaseActivity<BasePresenter, ViewModel,ActivityLoseWeightCalculatorBinding> implements View.OnClickListener {


    @Override
    public int getLayoutId() {
        return R.layout.activity_lose_weight_calculator;
    }

    @Override
    public BasePresenter initPresenter() {
        mBinding.setLoseWeightActivity(this);
        return null;
    }

    @Override
    public void initData(Bundle bundle) {
        mBinding.includeTittle.tvTittle.setText("减肥计算器");
    }

    @Override
    public int getStatusBarColorResId() {
        return R.color.baseThemeColor;
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                overridePendingTransition(R.anim.back_enter,R.anim.back_exit);
                break;

            case R.id.bt_get_rate://获取燃脂心率
                LoseWeightUtil.getHeartRate(this, mBinding.etAge, mBinding.etStaticHeartRate, mBinding.tvBestHeartRate);
                break;
            case R.id.bt_get_rmr://获取REE
                LoseWeightUtil.getREE(this, mBinding.tvSex, mBinding.etAge, mBinding.etWeight, mBinding.etHeight, mBinding.tvRee);
                break;
            case R.id.bt_get_bmr://获取REE
                LoseWeightUtil.getBmr(this, mBinding.tvSex, mBinding.etAge, mBinding.etWeight, mBinding.etHeight, mBinding.tvBmr);
                break;
            case R.id.tv_sex://设置性别
                LoseWeightUtil.setSex(this, mBinding.tvSex);
                break;
            default:
                break;
        }
    }


}
