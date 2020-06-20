package com.wyw.bao.leancloud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener1;
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist;

public class UpdateActivity extends PortraitActivity {

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    private NumberProgressBar progressBar;
    private int retryCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        progressBar = findViewById(R.id.numer_process);
        download(getIntent().getStringExtra("url"));
        findViewById(R.id.bt_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryCount = 0;
                download(getIntent().getStringExtra("url"));
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isDownloading) return true;
        return super.onKeyDown(keyCode, event);
    }
    private boolean isDownloading;

    private void download(final String url) {
        DownloadTask task = new DownloadTask.Builder(url, getFilesDir())
                .setFilename("test.apk")
                // the minimal interval millisecond for callback progress
                .setMinIntervalMillisCallbackProcess(16)
                // do re-download even if the task has already been completed in the past.
                .setPassIfAlreadyCompleted(false)
                .build();
        task.enqueue(new DownloadListener1() {
            @Override
            public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {
                Log.e("UpdateActivity", "process");
                progressBar.setProgress((int) (currentOffset * 1f / totalLength * 100));

            }

            @Override
            public void taskStart(@NonNull DownloadTask task, @NonNull Listener1Assist.Listener1Model model) {
                isDownloading = true;
                findViewById(R.id.bt_retry).setVisibility(View.GONE);
            }

            @Override
            public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {

            }

            @Override
            public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {

            }


            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull Listener1Assist.Listener1Model model) {
                if (cause == EndCause.COMPLETED) {
                    isDownloading = false;
                    DownloadUtil.installAPK(UpdateActivity.this, task.getFile());
                    return;
                }
                if (retryCount > 5) {
                    isDownloading = false;
                    findViewById(R.id.bt_retry).setVisibility(View.VISIBLE);
                    Toast.makeText(UpdateActivity.this, "下载失败,请重试", Toast.LENGTH_LONG).show();
                } else {
                    retryCount++;
                    download(url);
                }
            }
        });
    }
}
