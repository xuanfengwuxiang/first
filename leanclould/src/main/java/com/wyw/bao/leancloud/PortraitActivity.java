package com.wyw.bao.leancloud;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.lang.reflect.Field;

public class PortraitActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            fixOrientation();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
