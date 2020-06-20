package com.xuanfeng.testcomponent;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.google.auto.service.AutoService;
import com.xuanfeng.xflibrary.component.IComponentInterface;
import com.xuanfeng.testcomponent.activity.TestActivity;

/**
 * 测试模块组件
 */

@AutoService(IComponentInterface.class)
public class TestComponent implements IComponentInterface {
    @Override
    public void init(Application application) {

    }

    @Override
    public boolean toRoutePage(Activity activity, String routeCode) {

        boolean handled = false;
        if ("666".equals(routeCode)) {
            Intent intent = new Intent(activity, TestActivity.class);
            activity.startActivity(intent);
            handled = true;
        }
        return handled;
    }
}
