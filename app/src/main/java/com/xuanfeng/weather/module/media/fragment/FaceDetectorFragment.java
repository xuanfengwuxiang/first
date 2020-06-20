package com.xuanfeng.weather.module.media.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.xuanfeng.weather.R;
import com.xuanfeng.weather.databinding.FragmentFaceDetectorBinding;
import com.xuanfeng.weather.module.media.activity.CameraActivity;
import com.xuanfeng.weather.module.media.activity.CameraHorientalActivity;
import com.xuanfeng.weather.module.media.view.FaceDetectorView;
import com.xuanfeng.weather.utils.ImageUtil;
import com.xuanfeng.xflibrary.mvp.BaseFragment;
import com.xuanfeng.xflibrary.mvp.BasePresenter;
import com.xuanfeng.xflibrary.utils.StringUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 人脸识别界面
 */
public class FaceDetectorFragment extends BaseFragment<BasePresenter, ViewModel, FragmentFaceDetectorBinding> implements FaceDetectorView, View.OnClickListener {
    private Paint paint;//画人脸区域用到的Paint
    private Bitmap bm;//选择的图片的Bitmap对象
    private static final int MAX_FACE_NUM = 5;//最大可以检测出的人脸数量

    @Override
    public int getLayoutId() {
        return R.layout.fragment_face_detector;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    public void initListener() {
        mBinding.setListener(this);
    }

    @Override
    public void initData(Bundle bundle) {
        initListener();
        prepareForFaceDetect();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_detect://图片识别
                faceDetect();
                break;
            case R.id.tv_goto_camera://相机识别
                startActivity(new Intent(getActivity(), CameraActivity.class));
                break;
            case R.id.tv_horiental_limit://横屏的人脸限制
                startActivity(new Intent(getActivity(), CameraHorientalActivity.class));
                break;
            default:
                break;
        }
    }

    //人脸识别需要使用的控件初始化
    private void prepareForFaceDetect() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(getResources().getDisplayMetrics().density * 2);
        paint.setStyle(Paint.Style.STROKE);//设置画出的是空心方框，而不是实心方块
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_cgx);
        bm = bm.copy(Bitmap.Config.RGB_565, true);//要使用Android内置的人脸识别，需要将Bitmap对象转为RGB_565格式，否则无法识别
        mBinding.ivFace.setImageBitmap(bm);
    }

    //人脸检测
    private void faceDetect() {
        ImageUtil.faceDetect(paint, bm, MAX_FACE_NUM, mObserver);
    }

    //人脸检测后的回调
    Observer mObserver = new Observer() {


        @Override
        public void onComplete() {
            //do nothing
        }

        @Override
        public void onError(Throwable e) {
            //do nothing
        }

        @Override
        public void onSubscribe(Disposable d) {
            //do nothing
        }

        @Override
        public void onNext(Object o) {
            if (!(o instanceof String)) {
                return;
            }
            String realFaceNum = (String) o;
            if (StringUtils.isEmpty(realFaceNum)) {
                Toast.makeText(getActivity(), "未检测到人脸", Toast.LENGTH_SHORT).show();
                return;
            }
            //画出人脸区域后要刷新ImageView
            mBinding.ivFace.invalidate();
            Toast.makeText(getActivity(), "图片中检测到" + realFaceNum + "张人脸", Toast.LENGTH_SHORT).show();
        }
    };

}
