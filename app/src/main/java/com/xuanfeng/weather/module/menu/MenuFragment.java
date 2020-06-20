package com.xuanfeng.weather.module.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.xuanfeng.xflibrary.component.ComponentUtil;
import com.xuanfeng.xflibrary.contacts.ContactsActivity;
import com.xuanfeng.xflibrary.mvp.BaseFragment;
import com.xuanfeng.xflibrary.mvp.BasePresenter;
import com.xuanfeng.weather.R;
import com.xuanfeng.weather.databinding.FragmentMenuBinding;
import com.xuanfeng.weather.module.loseweight.LoseWeightCalculatorActivity;

//侧滑界面
public class MenuFragment extends BaseFragment<BasePresenter, ViewModel,FragmentMenuBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_menu;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initData(Bundle bundle) {
        mBinding.setListener(this);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_lose_weight://减肥计算器
                Intent intent = new Intent(getContext(), LoseWeightCalculatorActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
                break;
            case R.id.tv_test://测试界面
//                ComponentUtil.toRouterPage(getActivity(), "666");
                break;
            case R.id.tv_contacts://联系人
                intent = new Intent(getContext(), ContactsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
