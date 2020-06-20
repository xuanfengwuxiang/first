package com.xuanfeng.weather.module.media.fragment;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.xuanfeng.weather.R;
import com.xuanfeng.weather.databinding.FragmentPhotoShowBinding;
import com.xuanfeng.weather.module.media.adapter.ViewPagerPhotoAdapter;
import com.xuanfeng.weather.widget.ZoomOutPageTransformer;
import com.xuanfeng.xflibrary.mvp.BaseFragment;
import com.xuanfeng.xflibrary.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;


public class PhotoShowFragment extends BaseFragment<BasePresenter, ViewModel, FragmentPhotoShowBinding> {


    private List<String> mTittleList;


    protected void setListeners() {
        mBinding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //do nothing
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab != null) {
                    mBinding.vpPhoto.setCurrentItem(tab.getPosition());
                }
            }
        });
    }


    private void setViewPager() {
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511054491&di=4d9c293fbcc58cbf1b44a6fddca5ab30&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F4bed2e738bd4b31c2fc3c19d8dd6277f9e2ff81d.jpg");
        photoUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511066707&di=4e77bca150f6e00e073284c54e169eff&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F3801213fb80e7beca9004ec5252eb9389b506b38.jpg");
        photoUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511054750&di=6c1a46c779b7f237c7a65a80d01576c9&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D316e3f61a74bd11310c0bf7132c6ce7a%2F72f082025aafa40fb20015cfa164034f79f019da.jpg");
        PagerAdapter pagerAdapter = new ViewPagerPhotoAdapter(getContext(), photoUrls, mTittleList);
        mBinding.vpPhoto.setAdapter(pagerAdapter);
        mBinding.vpPhoto.setPageMargin(40);
        mBinding.vpPhoto.setPageTransformer(true, new ZoomOutPageTransformer(getContext()));
    }

    //设置指示器
    private void setTablayout() {
        mTittleList = new ArrayList<>();
        mTittleList.add("风景1");
        mTittleList.add("风景2");
        mTittleList.add("风景3");
        for (int i = 0; i < mTittleList.size(); i++) {
            mBinding.tablayout.addTab(mBinding.tablayout.newTab().setText(mTittleList.get(i)));
        }
        mBinding.tablayout.setupWithViewPager(mBinding.vpPhoto);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_photo_show;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initData(Bundle bundle) {
        setListeners();
        setTablayout();
        setViewPager();
    }
}
