package com.xuanfeng.weather.exception;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by xuanfengwuxiang on 2017/12/19.
 * 自定义全局异常捕获
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static ExceptionHandler mExceptionHandler;
    private static Application mApplication;

    private ExceptionHandler() {
    }

    //获取异常处理的单例
    public static synchronized ExceptionHandler getInstance(Application application) {

        if (mExceptionHandler == null) {
            mExceptionHandler = new ExceptionHandler();
            mApplication = application;
        }
        return mExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //不能忘记打印异常信息
        Log.e("uncaughtException",ex.toString());
        Toast.makeText(mApplication, "程序异常,10秒之后自动重启~", Toast.LENGTH_SHORT).show();
        restartActivity();
        // kill current process
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private long restartTime = 10000;//重新启动应用程序指定Activity间隔时间.  毫秒.  1000ms = 1s

    /**
     * 重新启动应用程序
     */
    private void restartActivity() {
        //创建用于启动的 Intent , 与对应的数据
        Intent intent = mApplication.getPackageManager().getLaunchIntentForPackage(mApplication.getPackageName());
        intent.putExtra("autoStart", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mApplication, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //获取闹钟管理器 , 用于定时执行我们的启动任务
        AlarmManager mgr = (AlarmManager) mApplication.getSystemService(Context.ALARM_SERVICE);
        //设置执行PendingIntent的时间是当前时间+restart_time 参数的值
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + restartTime, pendingIntent);
    }
}
