package com.xuanfeng.weather.module.weather.view;

import com.xuanfeng.xflibrary.mvp.BaseView;
import com.xuanfeng.weather.module.weather.widget.WeatherRecyclerView.WeatherBean.DataBean.ForecastBean;

import java.util.List;

/**
 * Created by xuanfengwuxiang on 2017/12/13.
 */

public interface WeatherView extends BaseView{
    void onGetWeatherSuccess(List<ForecastBean> mForecastBeanList);

    void onGetWeatherError(String msg);
}
