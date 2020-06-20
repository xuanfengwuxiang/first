package com.xuanfeng.weather.module.media.util;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuanfeng.xflibrary.magicindicator.MagicBean;
import com.xuanfeng.xflibrary.magicindicator.MagicUtil;
import com.xuanfeng.weather.R;
import com.xuanfeng.weather.ViewPagerAdapter;
import com.xuanfeng.weather.module.media.fragment.ChatFragment;
import com.xuanfeng.weather.module.media.fragment.FaceDetectorFragment;
import com.xuanfeng.weather.module.media.fragment.VideoFragment;
import com.xuanfeng.weather.module.media.fragment.PhotoShowFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuanfengwuxiang on 2018/8/20.
 */

public class MediaUtil {

    private MediaUtil() {
    }

    //初始化设置ViewPager的适配器指示器
    public static void initMagicIndicator(Context context, MagicIndicator mMagicIndicator, final ViewPager mViewPager) {
        List<MagicBean> titleList = new ArrayList<>();
        titleList.add(new MagicBean("图片", ""));
        titleList.add(new MagicBean("人脸识别", ""));
        titleList.add(new MagicBean("AI智能聊天", ""));
        int baseThemeColor = context.getResources().getColor(R.color.baseThemeColor);
        MagicUtil.setMagicAdapter(context, titleList, mMagicIndicator, baseThemeColor, baseThemeColor, mViewPager, (position, key) -> mViewPager.setCurrentItem(position));
    }

    //设置ViewPager的适配器
    public static void setViewPagerAdapter(Fragment fragment, ViewPager viewPager) {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new PhotoShowFragment());
        fragmentList.add(new FaceDetectorFragment());
        fragmentList.add(new ChatFragment());
        FragmentPagerAdapter fragmentPagerAdapter = new ViewPagerAdapter(fragment.getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
    }

    public static void setTittle(Context context, ImageView mIvLeft, RelativeLayout mRlHeader, TextView mTvTittle) {
        mIvLeft.setVisibility(View.GONE);
        mRlHeader.setBackgroundColor(context.getResources().getColor(R.color.baseThemeColor));
        mTvTittle.setText("多媒体");
    }
}
