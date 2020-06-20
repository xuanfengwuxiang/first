package com.xuanfeng.weather.module.weather.widget;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuanfeng.xflibrary.utils.AnimUtil;
import com.xuanfeng.weather.R;
import com.xuanfeng.weather.module.weather.widget.WeatherRecyclerView.WeatherBean.DataBean.ForecastBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuanfengwuxiang on 2018/5/3.
 * 图文竖直的RecyclerView
 */

public class WeatherRecyclerView extends FrameLayout {
    public static final int VERTICAL = LinearLayout.VERTICAL;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private List<ForecastBean> mForecastList;
    private WeatherAdapter mWeatherAdapter;

    public WeatherRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public WeatherRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        initViews();
        initData();
    }

    private void initViews() {
        View view = View.inflate(mContext, R.layout.layout_auto_fit_recyclerview, this);
        mRecyclerView = view.findViewById(R.id.rv_content);
    }

    private void initData() {
        initAdapter();
        setLayoutManager(VERTICAL);
    }


    private void initAdapter() {
        mForecastList = new ArrayList<>();
        mWeatherAdapter = new WeatherAdapter(mContext, mForecastList);
        mRecyclerView.setAdapter(mWeatherAdapter);
    }

    //设置普通列表
    public WeatherRecyclerView setLayoutManager(int orientation) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        return this;
    }

    public void setData(List<ForecastBean> forecastList) {
        mForecastList.clear();
        mForecastList.addAll(forecastList);
        mWeatherAdapter.notifyDataSetChanged();
        AnimUtil.runLayoutAnimation(mRecyclerView, R.anim.layout_animation_from_right);
    }

    public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
        private Context mContext;
        private List<ForecastBean> mList;

        public WeatherAdapter(Context context, List<ForecastBean> list) {
            mContext = context;
            mList = list;
        }


        @Override
        public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_weather, parent, false);
            return new WeatherViewHolder(view);
        }

        @Override
        public void onBindViewHolder(WeatherViewHolder holder, int position) {
            if (mList != null) {
                holder.mTvDate.setText(mList.get(position).getDate());
                holder.mTvLowTemp.setText(mList.get(position).getLow());
                holder.mTvHighTemp.setText(mList.get(position).getHigh());
                holder.mTvType.setText(mList.get(position).getType());
                holder.mTvWindOrientation.setText(mList.get(position).getFengxiang());
            }
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        class WeatherViewHolder extends RecyclerView.ViewHolder {

            private final TextView mTvDate;
            private final TextView mTvLowTemp;
            private final TextView mTvHighTemp;
            private final TextView mTvWindOrientation;
            private final TextView mTvType;

            public WeatherViewHolder(View itemView) {
                super(itemView);
                mTvDate = (TextView) itemView.findViewById(R.id.tv_date);
                mTvLowTemp = (TextView) itemView.findViewById(R.id.tv_low_temp);
                mTvHighTemp = (TextView) itemView.findViewById(R.id.tv_high_temp);
                mTvWindOrientation = (TextView) itemView.findViewById(R.id.tv_wind_orientation);
                mTvType = (TextView) itemView.findViewById(R.id.tv_type);
            }
        }
    }



    public static class WeatherBean {

        private DataBean data;

        private int status;//获取的code

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        private String desc;//OK还是不OK

        public static class DataBean {
            public WeatherBean.DataBean.YesterdayBean getYesterday() {
                return yesterday;
            }

            public void setYesterday(WeatherBean.DataBean.YesterdayBean yesterday) {
                this.yesterday = yesterday;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getAqi() {
                return aqi;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }

            public String getGanmao() {
                return ganmao;
            }

            public void setGanmao(String ganmao) {
                this.ganmao = ganmao;
            }

            public String getWendu() {
                return wendu;
            }

            public void setWendu(String wendu) {
                this.wendu = wendu;
            }

            public List<WeatherBean.DataBean.ForecastBean> getForecast() {
                return forecast;
            }

            public void setForecast(List<WeatherBean.DataBean.ForecastBean> forecast) {
                this.forecast = forecast;
            }

            /**
             * date : 20日星期四
             * high : 高温 37℃
             * fx : 西南风
             * low : 低温 28℃
             * fl : 4-5级
             * type : 多云
             */

            private WeatherBean.DataBean.YesterdayBean yesterday;//昨天
            private String city;
            private String aqi;
            private String ganmao;
            private String wendu;
            /**
             * date : 21日星期五
             * high : 高温 38℃
             * fengli : 4-5级
             * low : 低温 29℃
             * fengxiang : 西南风
             * type : 晴
             */

            private List<WeatherBean.DataBean.ForecastBean> forecast;

            public static class YesterdayBean {
                private String date;
                private String high;
                private String fx;
                private String low;
                private String fl;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getHigh() {
                    return high;
                }

                public void setHigh(String high) {
                    this.high = high;
                }

                public String getFx() {
                    return fx;
                }

                public void setFx(String fx) {
                    this.fx = fx;
                }

                public String getLow() {
                    return low;
                }

                public void setLow(String low) {
                    this.low = low;
                }

                public String getFl() {
                    return fl;
                }

                public void setFl(String fl) {
                    this.fl = fl;
                }

                private String type;
            }

            public static class ForecastBean {
                private String date;
                private String high;
                private String fengli;
                private String low;
                private String fengxiang;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getHigh() {
                    return high;
                }

                public void setHigh(String high) {
                    this.high = high;
                }

                public String getFengli() {
                    return fengli;
                }

                public void setFengli(String fengli) {
                    this.fengli = fengli;
                }

                public String getLow() {
                    return low;
                }

                public void setLow(String low) {
                    this.low = low;
                }

                public String getFengxiang() {
                    return fengxiang;
                }

                public void setFengxiang(String fengxiang) {
                    this.fengxiang = fengxiang;
                }

                private String type;

                public void onItemClick(View view) {//mvvm的点击事件
                    Toast.makeText(view.getContext(), "日期：" + date, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
