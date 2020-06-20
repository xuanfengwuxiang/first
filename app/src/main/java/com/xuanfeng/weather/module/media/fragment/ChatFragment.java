package com.xuanfeng.weather.module.media.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.xuanfeng.weather.R;
import com.xuanfeng.weather.databinding.FragmentChatBinding;
import com.xuanfeng.weather.module.media.presenter.ChatPresenter;
import com.xuanfeng.weather.module.media.view.ChatView;
import com.xuanfeng.weather.module.media.widget.ChatRecyclerView.ChatBean;
import com.xuanfeng.xflibrary.mvp.BaseFragment;
import com.xuanfeng.xflibrary.mvp.BasePresenter;

//聊天界面
public class ChatFragment extends BaseFragment<ChatPresenter, ViewModel, FragmentChatBinding> implements ChatView {


    public void onClick(View view) {
        String content = mBinding.etInput.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "请输入聊天内容", Toast.LENGTH_SHORT).show();
            return;
        }
        mBinding.rvChat.setData(new ChatBean("me", content));
        mBinding.etInput.getText().clear();
        mPresenter.getReply(content);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    public BasePresenter initPresenter() {
        mBinding.setFragment(this);
        return new ChatPresenter();
    }

    @Override
    public void initData(Bundle bundle) {
        mBinding.rvChat.setData(new ChatBean("you", "嗨，我是陪聊师##__##"));
    }

    @Override
    public void onGetReply(String content) {
        if (!TextUtils.isEmpty(content)) {
            mBinding.rvChat.setData(new ChatBean("you", content));
        }
    }


}
