package com.xuanfeng.weather.module.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.xuanfeng.weather.R;
import com.xuanfeng.weather.databinding.ActivityImageviewDoodleBinding;
import com.xuanfeng.weather.module.news.activity.SurfaceViewActivity;
import com.xuanfeng.xflibrary.mvp.BaseFragment;
import com.xuanfeng.xflibrary.mvp.BasePresenter;
import com.xuanfeng.xflibrary.utils.FileUtil;
import com.xuanfeng.xflibrary.utils.ImageUtil;

public class ImageDoodleFragment extends BaseFragment<BasePresenter, ViewModel, ActivityImageviewDoodleBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_imageview_doodle;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    public void initData(Bundle bundle) {
        mBinding.setListener(this);
        Glide.with(this).asDrawable().load(R.drawable.ic_scenery1).into(mBinding.doodleImageView);

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                mBinding.doodleImageView.unDo();
                break;
            case R.id.tv_save:
                Bitmap bitmap = mBinding.doodleImageView.getBitmap();
                boolean issuccessful = ImageUtil.saveBitmapToSD(bitmap, FileUtil.getSDPath(getActivity()), "涂鸦.jpg");
                if (issuccessful) {
                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_full_panel:
                Intent intent = new Intent(getActivity(), SurfaceViewActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


}
