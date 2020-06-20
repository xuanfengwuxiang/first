package com.xuanfeng.weather.module.media.callback;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

/**
 * Created by xuanfengwuxiang on 2017/12/21.
 * Camera里的，人脸识别，监听器
 */

public class GoogleDetectListenerImpl implements Camera.FaceDetectionListener {
    private Handler mHandler;///用于向主线程发送信息

    public GoogleDetectListenerImpl(Context mContext, Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        if (faces!=null){
            Message msg = mHandler.obtainMessage();
            msg.what = 2;
            msg.obj = faces;
            msg.sendToTarget();
        }

    }
}
