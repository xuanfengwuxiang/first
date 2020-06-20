package com.xuanfeng.weather.module.weather.presenter;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xuanfeng.weather.constant.HttpConstant;
import com.xuanfeng.weather.module.weather.view.WeatherView;
import com.xuanfeng.weather.module.weather.widget.WeatherRecyclerView.WeatherBean;
import com.xuanfeng.xflibrary.http.HttpResponse;
import com.xuanfeng.xflibrary.http.httpmgr.HttpManager;
import com.xuanfeng.xflibrary.mvp.BasePresenter;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by xuanfengwuxiang on 2017/12/13.
 */

public class WeatherPresenter implements BasePresenter<WeatherView, ViewModel>, DefaultLifecycleObserver {

    private WeatherView mWeatherView;

    @Override
    public void attachView(WeatherView weatherView,ViewModel viewModel) {
        mWeatherView = weatherView;
    }

    @Override
    public void detachView() {

    }

    public void getWeather(LifecycleOwner lifecycleOwner, String city) {
        if(TextUtils.isEmpty(city)){
            return;
        }
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("city", city);
        mWeatherView.showProgress();
        HttpManager.getInstance().getJO(HttpConstant.WEATHER_URL, params, new HttpResponse<JsonObject>() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                mWeatherView.hideProgress();
                if (jsonObject == null) {
                    onError(new Throwable("返回了空数据"));
                    return;
                }
                WeatherBean weatherBean = new Gson().fromJson(jsonObject.toString(), WeatherBean.class);
                if (weatherBean != null) {
                    WeatherBean.DataBean dataBean = weatherBean.getData();
                    if (dataBean != null) {
                        List<WeatherBean.DataBean.ForecastBean> list = dataBean.getForecast();
                        if (list != null && !list.isEmpty()) {
                            mWeatherView.onGetWeatherSuccess(list);
                        }
                    }
                } else {
                    onError(new Throwable("返回了空数据"));
                }
            }

            @Override
            public void onError(Throwable e) {
                mWeatherView.hideProgress();
            }

            @Override
            public void onComplete() {
                mWeatherView.hideProgress();
            }
        });
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.i("WeatherPresenter", "onResume了");
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Log.i("WeatherPresenter", "onDestroy走了");
    }
}
