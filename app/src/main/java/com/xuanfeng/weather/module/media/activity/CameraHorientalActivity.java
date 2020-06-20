package com.xuanfeng.weather.module.media.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
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
import com.xuanfeng.weather.MainActivity;
import com.xuanfeng.weather.R;
import com.xuanfeng.weather.databinding.ActivityCameraHorientalBinding;
import com.xuanfeng.weather.module.media.callback.GoogleDetectListenerImpl;
import com.xuanfeng.weather.utils.ImageUtil;
import com.xuanfeng.weather.widget.FaceView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 横屏预览人脸识别
 */
public class CameraHorientalActivity extends BaseActivity<BasePresenter, ViewModel, ActivityCameraHorientalBinding> implements FaceView.FaceViewListener, View.OnClickListener {

    private SurfaceHolder mSurfaceHolder;
    private int cameraId;
    private Camera mCamera;
    private MainHandler mainHandler = new MainHandler();
    byte[] photoBytes;
    private boolean isBack = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera_horiental;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }


    public void initListener() {
        mBinding.setListener(this);
        mBinding.faceView1.setFaceViewListener(this);
    }

    @Override
    public void initData(Bundle bundle) {
        initListener();
        initSurfaceView();
    }

    @Override
    public int getStatusBarColorResId() {
        return 0;
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
                        List<Camera.Face> matchedFaces = new ArrayList<>();
                        //以下算法，将屏幕坐标转换到camera的坐标系
                        boolean has = false;
                        int kuangLeft = mBinding.ivKuang.getLeft() - mBinding.faceView1.getWidth() / 2;
                        int kuangTop = mBinding.ivKuang.getTop() - mBinding.faceView1.getHeight() / 2;
                        int kuangRight = mBinding.ivKuang.getRight() - mBinding.faceView1.getWidth() / 2;
                        int kuangBottom = mBinding.ivKuang.getBottom() - mBinding.faceView1.getHeight() / 2;
                        for (int i = 0; i < faces.length; i++) {
                            Camera.Face face = faces[i];
                            int width = face.rect.right - face.rect.left;
                            int needWidth = mBinding.faceView1.getWidth() * width / 2000;
                            int cx = -face.rect.centerX();  //前置摄像头，镜像需加负
                            int cy = face.rect.centerY();
                            int faceLeft = (int) (mBinding.faceView1.getWidth() * cx / 2000f - needWidth / 2);
                            int faceTop = (int) (mBinding.faceView1.getHeight() * cy / 2000f - needWidth / 2);
                            int faceRight = (int) (mBinding.faceView1.getWidth() * cx / 2000f + needWidth / 2);
                            int faceBottom = (int) (mBinding.faceView1.getHeight() * cy / 2000f + needWidth / 2);
                            if (faceLeft > kuangLeft && faceTop > kuangTop && faceRight < kuangRight && faceBottom < kuangBottom) {
                                has = true;
                                matchedFaces.add(face);
                            }
                        }
                        if (!has) {
                            mBinding.faceView1.clearFaces();
                            return;
                        }

                        mBinding.faceView1.setFaces(matchedFaces.toArray(new Camera.Face[matchedFaces.size()]));
                        getCropFace(matchedFaces.toArray(new Camera.Face[matchedFaces.size()]));
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
                if (mBinding.faceView1 != null) {
                    mBinding.faceView1.clearFaces();
                    mBinding.faceView1.setVisibility(View.VISIBLE);


                }
                mCamera.setFaceDetectionListener(
                        new GoogleDetectListenerImpl(CameraHorientalActivity.this, mainHandler));
                mCamera.startFaceDetection();
            }
        }
    }


    private Matrix mMatrix = new Matrix();
    RectF mFaceOnScreenRect = new RectF();

    //从bitmap扣出人脸
    private void getCropFace(Camera.Face[] faces) {

        Camera.Face face = faces[0];
        if (mCameraData != null) {
            Camera.Size size = mCamera.getParameters().getPreviewSize();
            try {
                int bitmapWidth = size.width;
                int bitmapHeight = size.height;
                YuvImage image = new YuvImage(mCameraData, ImageFormat.NV21, bitmapWidth, bitmapHeight, null);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, bitmapWidth, bitmapHeight), 80, stream);

                Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());


                prepareMatrix(mMatrix, true, 0, mBinding.faceView1.getWidth(), mBinding.faceView1.getHeight());
                mMatrix.postRotate(0); //Matrix.postRotate默认是顺时针
                mFaceOnScreenRect.set(face.rect);
                mMatrix.mapRect(mFaceOnScreenRect);//获取到的屏幕坐标
                //将屏幕坐标转换成bitmap的坐标，用于截图
                Rect faceOnBitmapRect = new Rect();
                float time = (float) bitmapWidth / (float) mBinding.faceView1.getWidth();//bitmap的宽度是屏幕宽度的多少倍
                faceOnBitmapRect.left = (int) (mFaceOnScreenRect.left * time);
                faceOnBitmapRect.top = (int) (mFaceOnScreenRect.top * time);
                faceOnBitmapRect.right = (int) (mFaceOnScreenRect.right * time);
                faceOnBitmapRect.bottom = (int) (mFaceOnScreenRect.bottom * time);


                Bitmap cropFace = ImageUtil.getCropBitmap(bmp, faceOnBitmapRect);
                mBinding.ivCropFace.setImageBitmap(cropFace);

                stream.close();

            } catch (Exception ex) {
                Log.e("Sys", "Error:" + ex.getMessage());
            }
        }


    }

    public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation,
                                     int viewWidth, int viewHeight) {
        // Need mirror for front camera.
        matrix.setScale(mirror ? -1 : 1, 1);
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(displayOrientation);
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
    }

    public void takePhoto() {          //点击拍照的方法。
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.autoFocus((b, camera) -> {
            if (b) {        //如果焦点获取成功，拍照
                mCamera.takePicture(null, null, pictureCallBack);  //pictureCallBack 为拍照的回掉。
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
            mCamera.setPreviewCallback(null);
            mCamera.release(); // 释放照相机
        }
        mCamera = Camera.open(cameraId);
        mCamera.setPreviewCallback(mCameraCallback);
        setCameraParams(mCamera, 1080, 1920);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);

            mCamera.startPreview();
            mainHandler.sendEmptyMessage(1);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }
    }

    public static void setCameraParams(Camera mCamera, float width, float height) {
        Camera.Parameters parameters = mCamera.getParameters();
        // 获取摄像头支持的PictureSize列表
        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
        /**从列表中选取合适的分辨率*/
        Camera.Size picSize = getProperSize(pictureSizeList, height / width);
        if (null == picSize) {
            picSize = parameters.getPictureSize();
        }
        // 根据选出的PictureSize重新设置SurfaceView大小
        parameters.setPictureSize(picSize.width, picSize.height);

        // 获取摄像头支持的PreviewSize列表
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        Camera.Size preSize = getProperSize(previewSizeList, height / width);
        if (null != preSize) {
            Log.e("zjun", "preSize.width=" + preSize.width + "  preSize.height=" + preSize.height);
            parameters.setPreviewSize(preSize.width, preSize.height);
        }
        parameters.setJpegQuality(100); // 设置照片质量
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
        }
        // mCamera.cancelAutoFocus();//自动对焦。
        //mCamera.setDisplayOrientation(90);// 设置PreviewDisplay的方向，效果就是将捕获的画面旋转多少度显示
        mCamera.setParameters(parameters);

    }

    /**
     * 从相机支持的分辨率列表中，选取合适的分辨率
     * 默认w:h = 4:3
     * <p>注意：这里的w对应屏幕的height
     * h对应屏幕的width<p/>
     */
    private static Camera.Size getProperSize(List<Camera.Size> pictureSizeList, float screenRatio) {
        Log.i("zjun", "screenRatio=" + screenRatio);
        Camera.Size result = null;
        for (Camera.Size size : pictureSizeList) {
            float currentRatio = ((float) size.width) / size.height;
            if (currentRatio - screenRatio == 0) {
                result = size;
                break;
            }
        }

        if (null == result) {
            for (Camera.Size size : pictureSizeList) {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3) {// 默认w:h = 4:3
                    result = size;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * 点击完成保存图片，并将路径回传给上个页面
     */
    public void takePhotoFinish() {          //完成
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/test2.jpg");
        if (file.exists()) {
            boolean result = file.delete();
            Log.e(getClass().getSimpleName(), "删除文件情况：" + result);
        }
        try (FileOutputStream fo = new FileOutputStream(file);) {
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
                    mCamera.setPreviewCallback(mCameraCallback);
                    mCamera.setPreviewDisplay(holder);
                    setCameraParams(mCamera, 1080, 1920);
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

    byte[] mCameraData = null;
    Camera.PreviewCallback mCameraCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            mCameraData = data;
        }

    };

    @Override
    public int getCameraId() {
        return cameraId;
    }
}
