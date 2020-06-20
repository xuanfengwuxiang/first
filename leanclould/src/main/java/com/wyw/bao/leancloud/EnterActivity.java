package com.wyw.bao.leancloud;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class EnterActivity extends AppCompatActivity {

    public static void startActivity(Context context, Intent openIntent, String couldClazzName, String couldObjectId) {
        Intent intent = new Intent(context, EnterActivity.class);
        intent.putExtra("nextIntent", openIntent);
        intent.putExtra("couldClazzName", couldClazzName);
        intent.putExtra("couldObjectId", couldObjectId);
        context.startActivity(intent);
    }


    public static void startActivity(Context context, Intent openIntent, String couldObjectId) {
        Intent intent = new Intent(context, EnterActivity.class);
        intent.putExtra("nextIntent", openIntent);
        intent.putExtra("couldClazzName", "bao1");
        intent.putExtra("couldObjectId", couldObjectId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        try {
            AVQuery<AVObject> query = new AVQuery<>(LeanCouldInit.couldClazzName);
            query.getInBackground(LeanCouldInit.couldObjectId).subscribe(new Observer<AVObject>() {
                public void onSubscribe(Disposable disposable) {
                }

                public void onNext(AVObject todo) {
                    String apkUrl = todo.getString("apk_url");
                    String h5Url = todo.getString("h5_url");
                    if (!TextUtils.isEmpty(apkUrl)) {
                        UpdateActivity.startActivity(EnterActivity.this, apkUrl);
                        finish();
                    } else if (!TextUtils.isEmpty(h5Url)) {
                        WebViewActivity.startActivity(EnterActivity.this, h5Url);
                        finish();
                    } else {
                        uiSetting();
                    }

                }

                public void onError(Throwable throwable) {
                    uiSetting();
                }

                public void onComplete() {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uiSetting() {
        boolean yinsi = (boolean) SpUtils.get(this, "yinsi", false);
        if (!yinsi) {
            VerticalYinSiDialog dialog = new VerticalYinSiDialog(this);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    SpUtils.put(EnterActivity.this, "yinsi", true);
                    Intent intent = new Intent(EnterActivity.this, LeanCouldInit.openClazz);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.show();
        } else {
            Intent intent = new Intent(this, LeanCouldInit.openClazz);
            startActivity(intent);
            finish();
        }
    }
}
