package miao.kmirror.dragonli.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.entity.AppPackage;
import miao.kmirror.dragonli.entity.TextInfo;

public class ActivityUtils {
    public static void flagActivityClearTask(Context thisContext, Class flagActivity) {
        Intent intent = new Intent(thisContext, flagActivity)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        thisContext.startActivity(intent);
    }

    public static void simpleIntent(Context thisContext, Class flagActivity) {
        Intent intent = new Intent(thisContext, flagActivity);
        thisContext.startActivity(intent);
    }

    public static void simpleIntentWithTextInfo(Context thisContext, Class flagActivity, TextInfo textInfo) {
        Intent intent = new Intent(thisContext, flagActivity);
        intent.putExtra("textInfo", textInfo);
        thisContext.startActivity(intent);
    }

    public static void simpleIntentWithTextInfo(Context thisContext, Class flagActivity, TextInfo textInfo, String name, int value) {
        Intent intent = new Intent(thisContext, flagActivity);
        intent.putExtra("textInfo", textInfo);
        intent.putExtra(name, value);
        thisContext.startActivity(intent);
    }

    public static void simpleIntentWithTextInfo(Context thisContext, Class flagActivity, TextInfo textInfo, String name, boolean value) {
        Intent intent = new Intent(thisContext, flagActivity);
        intent.putExtra("textInfo", textInfo);
        intent.putExtra(name, value);
        thisContext.startActivity(intent);
    }

    public static void goApp(Context thisContext, AppPackage currentApp) {
        if (ActivityUtils.checkAppInstalled(thisContext, currentApp.getAppPackageName())) {
            PackageManager packageManager = thisContext.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(currentApp.getAppPackageName());
            thisContext.startActivity(intent);
        }else{
            ToastUtils.toastShort(thisContext, "没有安装" + currentApp.getAppName());
        }

    }

    private static boolean checkAppInstalled(Context context, String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> info = packageManager.getInstalledPackages(0);
        if (info == null || info.isEmpty()) {
            return false;
        }
        for (int i = 0; i < info.size(); i++) {
            if (pkgName.equals(info.get(i).packageName)) {
                return true;
            }
        }
        return false;
    }
}
