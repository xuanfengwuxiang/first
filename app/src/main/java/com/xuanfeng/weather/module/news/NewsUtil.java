package com.xuanfeng.weather.module.news;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuanfeng.weather.R;
import com.xuanfeng.xflibrary.utils.ImageUtil;

/**
 * Created by xuanfengwuxiang on 2018/8/2.
 */

public class NewsUtil {
    private NewsUtil() {
    }


    public static void setTittleBar(Context context, TextView mTvTittle, ImageView mIvLeft) {
        mTvTittle.setText("画板");
        mIvLeft.setImageBitmap(ImageUtil.getColorBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_back), 128f, 128f, 128f, 128f));
    }
}
