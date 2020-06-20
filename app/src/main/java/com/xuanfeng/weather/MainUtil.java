package com.xuanfeng.weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.xuanfeng.xflibrary.utils.ImageUtil;
import com.xuanfeng.xflibrary.utils.ToastUtil;
import com.xuanfeng.xflibrary.widget.NoScrollViewPager;
import com.xuanfeng.weather.module.media.fragment.MediaFragment;
import com.xuanfeng.weather.module.news.NewsFragment;
import com.xuanfeng.weather.module.weather.fragment.WeatherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuanfengwuxiang on 2018/8/1.
 */

public class MainUtil {
    private static final String TAG = "MainUtil";

    private MainUtil() {
    }

    private static long mExitTime = 0;
    private static final int DOUBLE_CLICK_TIME = 2000;//双击退出时间
    private static final int PAGE_LIMIT = 2;//屏外缓存书

    //退出app
    public static void exitApp(Activity activity) {
        if ((System.currentTimeMillis() - mExitTime) > DOUBLE_CLICK_TIME) {
            ToastUtil.showToast(activity, "再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            activity.finish();
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                Process.killProcess(Process.myPid());//从操作系统中结束掉当前程序的进程
            } catch (Exception e) {
                Process.killProcess(Process.myPid());
                Log.e(TAG, e.toString());
            }
        }
    }

    //设置导航按钮监听
    public static void setCheckListener(RadioGroup mRbParent, final Context context, final NoScrollViewPager mVpMain,
                                        final LinearLayout mActivityMain) {
        mRbParent.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_one:
                    mVpMain.setCurrentItem(0, false);
                    ImageUtil.loadImage(context, R.drawable.ic_sunny_day, mActivityMain);
                    break;
                case R.id.rb_two:
                    mVpMain.setCurrentItem(1, false);
                    mActivityMain.setBackgroundColor(context.getResources().getColor(R.color.baseThemeColor));
                    break;
                case R.id.rb_three:
                    mVpMain.setCurrentItem(2, false);
                    mActivityMain.setBackgroundColor(context.getResources().getColor(R.color.baseThemeColor));
                    break;
                default:
                    break;
            }
        });
    }

    //设置viewpager的适配器
    public static void setViewPagerAdapter(AppCompatActivity activity, NoScrollViewPager mVpMain) {
        List<Fragment> list = new ArrayList<>();
        list.add(new WeatherFragment());
        list.add(new MediaFragment());
        list.add(new NewsFragment());
        FragmentPagerAdapter fragmentPagerAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), list);
        mVpMain.setAdapter(fragmentPagerAdapter);
        mVpMain.setOffscreenPageLimit(PAGE_LIMIT);
    }

}
