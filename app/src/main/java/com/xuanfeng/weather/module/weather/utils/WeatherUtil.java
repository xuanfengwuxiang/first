package com.xuanfeng.weather.module.weather.utils;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.xuanfeng.xflibrary.utils.ToastUtil;
import com.xuanfeng.weather.R;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xuanfengwuxiang on 2018/8/1.
 * 天气模块的业务工具
 */

public class WeatherUtil {
    private static final String TAG = "WeatherUtil";

    private WeatherUtil() {
    }

    //POI搜索地点
    public static void searchFromBaidu(Context context, EditText mEtInput, OnGetPoiSearchResultListener poiListener) {
        if (context == null || mEtInput == null || poiListener == null) {
            return;
        }
        String key = mEtInput.getText().toString();
        if (TextUtils.isEmpty(key)) {
            ToastUtil.showToast(context, "请输入关键词");
            return;
        }
        PoiSearch mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(key)
                .keyword("省")
                .pageNum(10));
    }

    public static void updateMapPosition(PoiResult result, MapView mMapView) {
        if (result == null || mMapView == null) {
            return;
        }
        List<PoiInfo> list = result.getAllPoi();
        if (list != null && !list.isEmpty()) {
            PoiInfo poiInfo = list.get(0);
            if (poiInfo != null) {
                LatLng latLng = poiInfo.location;
                if (latLng != null) {
                    updateMapPosition(latLng.latitude, latLng.longitude, mMapView);
                }
            }
        }
    }

    //设置百度地图的位置
    public static void updateMapPosition(double latitude, double longitude, MapView mMapView) {
        // 开启定位图层
        try {
            BaiduMap mBaiduMap = mMapView.getMap();
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
            //创建一个图层选项
            LatLng latlng = new LatLng(latitude, longitude);
            OverlayOptions options = new MarkerOptions().position(latlng).icon(bitmapDescriptor);
            mBaiduMap.addOverlay(options);
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(latlng)
                    .zoom(12)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    //获取定位对象
    public static void getLocationClient(Context mContext, BDLocationListener mLocationListener, Consumer consumer) {

        Observable.create(new ObservableOnSubscribe<LocationClient>() {

            @Override
            public void subscribe(ObservableEmitter<LocationClient> emitter) throws Exception {
                LocationClient mLocationClient = new LocationClient(mContext.getApplicationContext());
                LocationClientOption option = new LocationClientOption();
                option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
                //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

                option.setCoorType("bd09ll");
                //可选，默认gcj02，设置返回的定位结果坐标系

                int span = 0;
                option.setScanSpan(span);
                //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

                option.setIsNeedAddress(true);
                //可选，设置是否需要地址信息，默认不需要

                option.setAddrType("all");

                option.setOpenGps(true);
                //可选，默认false,设置是否使用gps

                option.setLocationNotify(true);
                //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

                option.setIsNeedLocationDescribe(true);
                //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

                option.setIsNeedLocationPoiList(true);
                //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

                option.setIgnoreKillProcess(false);
                //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

                option.SetIgnoreCacheException(false);
                //可选，默认false，设置是否收集CRASH信息，默认收集

                option.setEnableSimulateGps(false);
                //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

                mLocationClient.setLocOption(option);
                mLocationClient.registerLocationListener(mLocationListener);
                mLocationClient.start();
                emitter.onNext(mLocationClient);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);


    }

    //设置一个textview内的每个字大小颜色
    public static void setTittleSizeAndColor(Context context, TextView textView) {
        String text = "最近5天的天气预报表情";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 1, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6600")), 2, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(100), 2, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFff00")), 3, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#00ff00")), 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6600")), 5, 6, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#33FF33")), 6, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(100), 6, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_cool);
        drawable.setBounds(0, 0, 100, 100);
        ImageSpan imageSpan = new ImageSpan(drawable);
        spannableString.setSpan(imageSpan, text.length() - 2, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    public static void setTittleBar(Context context, TextView mTvLeft, ImageView mIvLeft, RelativeLayout mRlHeader) {
        mTvLeft.setVisibility(View.VISIBLE);
        mTvLeft.setTextColor(Color.WHITE);
        Drawable img = context.getResources().getDrawable(R.drawable.ic_add);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        mTvLeft.setCompoundDrawables(img, null, null, null);
        mIvLeft.setVisibility(View.GONE);
        mRlHeader.setBackgroundColor(context.getResources().getColor(R.color.color_transparent));
    }

    //做圆形动画
    public static void doAnim(View view) {
        if (view == null) {
            return;
        }
        Animator anim = ViewAnimationUtils.createCircularReveal(view, 0, 0, 0, (float) Math.hypot(view.getWidth(), view.getHeight()));
        anim.setDuration(2000);
        anim.start();
    }

}
