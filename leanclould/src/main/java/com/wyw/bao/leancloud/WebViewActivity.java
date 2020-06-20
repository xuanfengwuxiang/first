package com.wyw.bao.leancloud;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener1;
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist;


public class WebViewActivity extends PortraitActivity {
    private WebView mWebView;

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    private FrameLayout frameLayoutDownload;
    private NumberProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        frameLayoutDownload = findViewById(R.id.group_download_data);
        progressBar = findViewById(R.id.numer_process);
        mWebView = findViewById(R.id.webview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setTextZoom(100);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.clearCache(true);
        mWebView.requestFocus();
        mWebView.setVerticalScrollBarEnabled(false);
        CookieSyncManager.getInstance();
        CookieManager.getInstance().removeAllCookie();

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                dowload(url);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("WebViewActivity", url);
                if (url.startsWith("http://") ||
                        url.startsWith("https://")) { // intent
                    view.loadUrl(url);
                    return false;
                }
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e("WebViewActivity", "request.getUrl():" + request.getUrl());
                if (request.getUrl().toString().startsWith("http://") ||
                        request.getUrl().toString().startsWith("https://")) { // intent
                    view.loadUrl(request.getUrl().toString());
                    return false;
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        mWebView.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.onPause();
            ((ViewGroup) mWebView.getParent()).removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isDownloading) return true;
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isDownloading;
    private int retryCount;
    private void dowload(final String url) {
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
                progressBar.setProgress((int) (currentOffset * 1f / totalLength * 100));

            }

            @Override
            public void taskStart(@NonNull DownloadTask task, @NonNull Listener1Assist.Listener1Model model) {
                frameLayoutDownload.setVisibility(View.VISIBLE);
                isDownloading = true;
            }

            @Override
            public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {

            }

            @Override
            public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {

            }


            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull Listener1Assist.Listener1Model model) {
                if (cause == EndCause.COMPLETED){
                    isDownloading = false;
                    frameLayoutDownload.setVisibility(View.GONE);
                    DownloadUtil.installAPK(WebViewActivity.this, task.getFile());
                    return;
                }
                if (retryCount > 5) {
                    isDownloading = false;
                    frameLayoutDownload.setVisibility(View.GONE);
                    Toast.makeText(WebViewActivity.this, "下载失败,请重试", Toast.LENGTH_LONG).show();
                } else {
                    retryCount++;
                    dowload(url);
                }

            }
        });
    }

}
