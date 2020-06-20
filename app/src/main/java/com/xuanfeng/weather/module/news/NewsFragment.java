package com.xuanfeng.weather.module.news;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.xuanfeng.weather.R;
import com.xuanfeng.weather.databinding.FragmentNewsBinding;
import com.xuanfeng.xflibrary.magicindicator.MagicListener;
import com.xuanfeng.xflibrary.mvp.BaseFragment;
import com.xuanfeng.xflibrary.mvp.BasePresenter;

//新闻界面
public class NewsFragment extends BaseFragment<BasePresenter, ViewModel, FragmentNewsBinding> implements View.OnClickListener {




    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    public BasePresenter initPresenter() {
        mBinding.setFragment(this);
        return null;
    }

    @Override
    public void initData(Bundle bundle) {
        NewsUtil.setTittleBar(getContext(), mBinding.includeTittle.tvTittle, mBinding.includeTittle.ivLeft);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:

                break;
            default:
                break;
        }
    }


}
