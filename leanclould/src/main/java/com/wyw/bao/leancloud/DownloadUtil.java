package com.wyw.bao.leancloud;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class DownloadUtil {

    /**
     * 安装App
     *
     * @param context
     * @param file
     */
    public static void installAPK(Context context, File file) {
        if (file == null || !file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        int sdkVersion = context.getApplicationInfo().targetSdkVersion;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 即是在清单文件中配置的authorities
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider1", file);//newfile
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);

        } else {
            uri = Uri.parse("file://" + file.toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        try {
            Process p = Runtime.getRuntime().exec("chmod 755 " + file);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //在服务中开启activity必须设置flag,后面解释
        context.startActivity(intent);
    }
}
