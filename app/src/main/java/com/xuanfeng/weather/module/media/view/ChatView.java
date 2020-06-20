package com.xuanfeng.weather.module.media.view;

import com.xuanfeng.xflibrary.mvp.BaseView;

/**
 * Created by xuanfengwuxiang on 2018/8/20.
 * 聊天界面的视图操作
 */

public interface ChatView extends BaseView {
    public void onGetReply(String content);
}
