package com.xuanfeng.weather.module.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.xuanfeng.weather.R;
import com.xuanfeng.weather.databinding.ActivitySelectCityBinding;
import com.xuanfeng.weather.module.weather.utils.WeatherUtil;
import com.xuanfeng.xflibrary.mvp.BaseActivity;
import com.xuanfeng.xflibrary.mvp.BasePresenter;

public class SelectCityActivity extends BaseActivity<BasePresenter, ViewModel, ActivitySelectCityBinding> implements View.OnClickListener {

    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(Bundle bundle) {
        mBinding.includeTittle.tvTittle.setText("切换城市");
        Intent intent = getIntent();
        if (intent != null) {
            double lontitude = intent.getDoubleExtra(LONGITUDE, 0);
            double latitude = intent.getDoubleExtra(LATITUDE, 0);
            WeatherUtil.updateMapPosition(latitude, lontitude, mBinding.mapView);
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left://退出
                finish();
                break;
            case R.id.tv_search://搜索
                WeatherUtil.searchFromBaidu(this, mBinding.etInput, poiListener);
                break;
            default:
                break;
        }
    }


    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {

        public void onGetPoiResult(PoiResult result) {
            WeatherUtil.updateMapPosition(result, mBinding.mapView);
        }

        public void onGetPoiDetailResult(PoiDetailResult result) {
            //do nothing
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            //do nothing
        }
    };


    @Override
    public void onDestroy() {
        if (mBinding.mapView != null) {
            mBinding.mapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.mapView.onPause();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_city;
    }

    @Override
    public BasePresenter initPresenter() {
        mBinding.setSelectCityActivity(this);
        return null;
    }

    @Override
    public int getStatusBarColorResId() {
        return R.color.baseThemeColor;
    }


}
