package com.wyw.bao.leancloud;

import android.content.Context;

import cn.leancloud.AVOSCloud;

/**
 * 初始化
 */
public class LeanCouldInit {
    public static Class openClazz;
    public static String couldClazzName;
    public static String couldObjectId;

    public static void init(Context context,
                            String leanCouldAppId,
                            String leanCouldAppKey,
                            Class openClazz,
                            String couldClazzName,
                            String couldObjectId) {
        LeanCouldInit.openClazz = openClazz;
        LeanCouldInit.couldClazzName = couldClazzName;
        LeanCouldInit.couldObjectId = couldObjectId;
        AVOSCloud.initialize(context,
                leanCouldAppId,
                leanCouldAppKey,
                "https://ktc638b2.lc-cn-n1-shared.com");
    }

}
