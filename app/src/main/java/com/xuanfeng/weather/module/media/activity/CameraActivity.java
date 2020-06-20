package com.xuanfeng.weather.module.media.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.xuanfeng.xflibrary.mvp.BaseActivity;
import com.xuanfeng.xflibrary.mvp.BasePresenter;
import com.xuanfeng.xflibrary.utils.ToastUtil;
import com.xuanfeng.weather.MainActivity;
import com.xuanfeng.weather.R;
import com.xuanfeng.weather.databinding.ActivityCameraBinding;
import com.xuanfeng.weather.module.media.callback.GoogleDetectListenerImpl;
import com.xuanfeng.weather.utils.ImageUtil;
import com.xuanfeng.weather.widget.FaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 人脸识别,相机界面
 */
public class CameraActivity extends BaseActivity<BasePresenter, ViewModel, ActivityCameraBinding> implements FaceView.FaceViewListener , View.OnClickListener {

    private SurfaceHolder mSurfaceHolder;
    private int cameraId;
    private Camera mCamera;
    private MainHandler mainHandler = new MainHandler();
    byte[] photoBytes;
    private boolean isBack = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    public void initListener() {
        mBinding.setListener(this);
        mBinding.faceView.setFaceViewListener(this);
    }

    @Override
    public void initData(Bundle bundle) {
        initListener();
        initSurfaceView();
    }

    private void initSurfaceView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮
        mSurfaceHolder = mBinding.surfaceView.getHolder();
        cameraId = ImageUtil.findBackCamera();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mBinding.tvTurnCamera.setText("前置/后置------当前：后置");
        mSurfaceHolder.addCallback(new SurfaceViewCallBack(this));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_turn_camera://切换摄像头
                turnCamera();
                break;

            case R.id.tv_take_photo://拍照
                takePhoto();
                break;

            case R.id.tv_finish_photo://照片完成按钮
                takePhotoFinish();
                break;

            case R.id.tv_cancel_photo://照片取消按钮
                cancle();
                break;
            default:
                break;
        }
    }

    @Override
    public int getCameraId() {
        return cameraId;
    }

    private class MainHandler extends Handler {

        @Override
        public void handleMessage(final Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    startGoogleDetect();
                    break;
                case 2:
                    runOnUiThread(() -> {
                        Camera.Face[] faces = (Camera.Face[]) msg.obj;
                        mBinding.faceView.setFaces(faces);
                        ToastUtil.showToast(CameraActivity.this, "收到人脸识别的信息");
                    });

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

        //添加人脸监听器
        private void startGoogleDetect() {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.getMaxNumDetectedFaces() > 0) {
                if (mBinding.faceView != null) {
                    mBinding.faceView.clearFaces();
                    mBinding.faceView.setVisibility(View.VISIBLE);
                }
                mCamera.setFaceDetectionListener(new GoogleDetectListenerImpl(CameraActivity.this, mainHandler));
                mCamera.startFaceDetection();
            }
        }
    }

    public void takePhoto() {          //点击拍照的方法。
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                if (b) {        //如果焦点获取成功，拍照
                    mCamera.takePicture(null, null, pictureCallBack);  //pictureCallBack 为拍照的回掉。
                }
            }
        });
    }

    private Camera.PictureCallback pictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {     //bytes 即是拍照回来的内容，将内容写入本地即可
            photoBytes = bytes;
            showPhoto(photoBytes);
        }

        //显示拍出的图片
        private void showPhoto(byte[] photoBytes) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
            Matrix matrix = new Matrix();
            if (isBack) {  //后置摄像头
                matrix.setRotate(90);
            } else {
                matrix.setRotate(-90); //前置摄像头
            }
            Bitmap bit = bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (bit != null) {
                mBinding.ivPhotoResult.setImageBitmap(bit);
            }
            mBinding.rlPhotoResult.setVisibility(View.VISIBLE);
        }

    };


    /**
     * 摄像头的切换
     */
    public void turnCamera() {
        if (isBack) {
            cameraId = ImageUtil.findFrontCamera();  //前置摄像头id
            mBinding.tvTurnCamera.setText("前置/后置------当前：前置");
        } else {
            cameraId = ImageUtil.findBackCamera();  //后置
            mBinding.tvTurnCamera.setText("前置/后置------当前：后置");
        }
        isBack = !isBack;
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release(); // 释放照相机
        }
        mCamera = Camera.open(cameraId);
        ImageUtil.setCameraParams(mCamera, 1080, 1920);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
            mainHandler.sendEmptyMessage(1);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }
    }

    /**
     * 点击完成保存图片，并将路径回传给上个页面
     */
    public void takePhotoFinish() {          //完成
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/test2.jpg");
        if (file.exists()) {
            boolean result = file.delete();
            Log.e(getClass().getSimpleName(), "删除情况：" + result);
        }
        try (FileOutputStream fo = new FileOutputStream(file)
        ) {
            fo.write(photoBytes);
            Toast.makeText(this, "已保存图片", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);                         //完成拍照，回到mainActivity,并将图片的路径回传
            intent.putExtra("picPath", Environment.getExternalStorageDirectory().getPath() + "/test2.jpg");
            intent.putExtra("isBack", isBack);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }
    }

    /**
     * 取消，放弃该图片
     */
    public void cancle() {          //取消
        photoBytes = null;
        mBinding.rlPhotoResult.setVisibility(View.GONE);
        mCamera.startPreview();
    }


    public class SurfaceViewCallBack implements SurfaceHolder.Callback {

        public SurfaceViewCallBack(Context context) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                if (mCamera == null) {
                    mCamera = Camera.open(cameraId);
                    mCamera.setPreviewDisplay(holder);
                    ImageUtil.setCameraParams(mCamera, 1080, 1920);
                }
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), e.toString());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mCamera.startPreview();
            mainHandler.sendEmptyMessage(1);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mCamera != null) {
                mCamera.release(); // 释放照相机
                mCamera = null;
            }
        }
    }
}
