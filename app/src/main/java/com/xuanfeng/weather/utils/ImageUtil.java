package com.xuanfeng.weather.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by xuanfengwuxiang on 2017/12/21.
 */

public class ImageUtil {
    private ImageUtil() {
    }

    //在给定的bitmap上，进行人脸检测，并画出人脸区域框框
    public static void faceDetect(final Paint paint, final Bitmap bitmap, final int maxFaceNum, final Observer observer) {

        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //从bitmap获取人脸
                FaceDetector faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), maxFaceNum);
                FaceDetector.Face[] faces = new FaceDetector.Face[maxFaceNum];
                int realFaceNum = faceDetector.findFaces(bitmap, faces);
                //对获取结果进行判断
                if (realFaceNum > 0) {
                    drawFacesArea(faces, bitmap, paint);
                    observer.onNext(realFaceNum + "");
                } else {
                    observer.onNext("");
                }

            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    //根据检测出来的脸，画框框到bitmap上
    private static void drawFacesArea(FaceDetector.Face[] faces, Bitmap bitmap, Paint paint) {
        float eyesDistance;//两眼间距
        Canvas canvas = new Canvas(bitmap);
        for (int i = 0; i < faces.length; i++) {
            FaceDetector.Face face = faces[i];
            if (face != null) {
                PointF pointF = new PointF();
                face.getMidPoint(pointF);//获取人脸中心点
                eyesDistance = face.eyesDistance();//获取人脸两眼的间距
                //画出人脸的区域
                canvas.drawRect(pointF.x - eyesDistance, pointF.y - eyesDistance, pointF.x + eyesDistance, pointF.y + eyesDistance, paint);
            }
        }
    }

    /**
     * 拿到后置摄像头id（假如手机有N个摄像头，cameraId 的值 就是 0 ~ N-1）
     */
    public static int findBackCamera() {
        int cameraId = -1;
        int numberCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    /**
     * 拿到前置摄像头id
     */
    public static int findFrontCamera() {
        int cameraId = -1;
        int numberCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    /**
     * 设置相机参数
     *
     * @param width
     * @param height
     */
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
        mCamera.setDisplayOrientation(90);// 设置PreviewDisplay的方向，效果就是将捕获的画面旋转多少度显示
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
     * @param sourceBitmap 需要处理的原始图片
     * @param orginRect    想要抠出来的人脸框位置
     * @return 抠出来的人脸图
     */
    public static Bitmap getCropBitmap(Bitmap sourceBitmap, Rect orginRect) {
        if (sourceBitmap == null || sourceBitmap.isRecycled()) {
            return null;
        }
        Rect rect = getScaleRect(orginRect, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        return sourceBitmap.createBitmap(sourceBitmap, rect.left, rect.top, rect.width(), rect.height());
    }

    public static Rect getScaleRect(Rect rect, int maxW, int maxH) {
        Rect resultRect = new Rect();
        /**
         * 根据人脸框往外分别扩展宽度和高度的15%，可以根据需要调整，如果左边框或者上边框重新计算抠图框之后位置小于0，那么左边框和上边框位置就设为0，
         * 如果右边框和下边框重新计算抠图框之后位置大于原始图片的宽高，那么左边框和下边框位置就设为图片的宽度和高度
         */
        int left = (int) (rect.left - ((rect.width() * 15) / 100));
        int right = (int) (rect.right + ((rect.width() * 15) / 100));
        int bottom = (int) (rect.bottom + ((rect.height() * 15) / 100));
        int top = (int) (rect.top - ((rect.height() * 15) / 100));
        resultRect.left = left > 0 ? left : 0;
        resultRect.right = right > maxW ? maxW : right;
        resultRect.bottom = bottom > maxH ? maxH : bottom;
        resultRect.top = top > 0 ? top : 0;
        return resultRect;
    }
}
