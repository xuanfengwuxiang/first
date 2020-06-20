package com.wyw.bao.leancloud;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;


public class VerticalYinSiDialog extends Dialog {
    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        if (null == context) {
            throw new NullPointerException("getScreenWidth(), context must not null!!");
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        return screenWidth;
    }

    public VerticalYinSiDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setLayout((int) (getScreenWidth(context) * 0.8f), WindowManager.LayoutParams.MATCH_PARENT);
            getWindow().getDecorView().setPadding(0, 0, 0, 0);
        }
        setContentView(R.layout.dialog_yin_si);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
