package com.xuanfeng.weather.module.media.widget;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuanfeng.weather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuanfengwuxiang on 2018/5/3.
 * 图文竖直的RecyclerView
 */

public class ChatRecyclerView extends FrameLayout {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private List<ChatBean> mChatList;
    private ChatAdapter mChatAdapter;

    public ChatRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public ChatRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        initViews();
        initData();
    }

    private void initViews() {
        View view = View.inflate(mContext, R.layout.layout_auto_fit_recyclerview, this);
        mRecyclerView = view.findViewById(R.id.rv_content);
    }

    private void initData() {
        initAdapter();
        setLayoutManager(VERTICAL);
    }


    private void initAdapter() {
        mChatList = new ArrayList<>();
        mChatAdapter = new ChatAdapter(mContext, mChatList);
        mRecyclerView.setAdapter(mChatAdapter);
    }

    //设置普通列表
    public ChatRecyclerView setLayoutManager(int orientation) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        return this;
    }

    public void setData(ChatBean chatBean) {
        mChatList.add(chatBean);
        mChatAdapter.notifyItemInserted(mChatList.size() - 1);
        mRecyclerView.smoothScrollToPosition(mChatList.size() - 1);
    }

    //适配器
    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
        private Context mContext;
        private List<ChatBean> mList;

        public ChatAdapter(Context context, List<ChatBean> list) {
            mContext = context;
            mList = list;
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat, parent, false);
            return new ChatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
            try {
                if (mList != null) {
                    final ChatBean bean = mList.get(position);
                    if (bean != null) {
                        if ("me".equals(bean.getType())) {
                            holder.ivMe.setVisibility(VISIBLE);
                            holder.ivYou.setVisibility(GONE);
                            holder.rlParent.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                        } else {
                            holder.ivMe.setVisibility(GONE);
                            holder.ivYou.setVisibility(VISIBLE);
                            holder.rlParent.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                        }
                        holder.tvContent.setText(bean.getContent());

                    }

                }
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), e.toString());
            }
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        class ChatViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivYou;
            private TextView tvContent;
            private ImageView ivMe;
            private RelativeLayout rlParent;

            public ChatViewHolder(View itemView) {
                super(itemView);
                ivYou = itemView.findViewById(R.id.iv_you);
                tvContent = itemView.findViewById(R.id.tv_content);
                ivMe = itemView.findViewById(R.id.iv_me);
                rlParent = itemView.findViewById(R.id.rl_parent);

            }
        }
    }

    //业务bean
    public static class ChatBean {
        private String type;
        private String content;

        public ChatBean(String type, String content) {
            this.type = type;
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class ResponseBean {

        /**
         * status : 0
         * msg : ok
         * result : {"type":"聊天","content":"情侣们在这一天互相馈赠礼物，用以表达爱意或友好。根据中西方文化，节日的时间和特色不同，每年的2月14日是西方的情人节","relquestion":""}
         */

        private String status;
        private String msg;
        private ResultBean result;


        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * type : 聊天
             * content : 情侣们在这一天互相馈赠礼物，用以表达爱意或友好。根据中西方文化，节日的时间和特色不同，每年的2月14日是西方的情人节
             * relquestion :
             */

            private String type;
            private String content;
            private String relquestion;


            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getRelquestion() {
                return relquestion;
            }

            public void setRelquestion(String relquestion) {
                this.relquestion = relquestion;
            }
        }
    }
}
