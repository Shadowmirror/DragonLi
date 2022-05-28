package miao.kmirror.dragonli.utils;

import android.content.Context;
import android.content.SharedPreferences;

import miao.kmirror.dragonli.constant.ConstantValue;

public class SpfUtils {

    private static String SPF_NAME = "noteSpf";

    public static void saveString(Context context, String key, String value) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return spf.getString(key, "");
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static void saveLong(Context context, String key, Long value) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public static Long getLong(Context context, String key) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return spf.getLong(key, -1);
    }

    public static int getInt(Context context, String key) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return spf.getInt(key, -1);
    }

    public static int getIntWithDefault(Context context, String key, int defValue) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return spf.getInt(key, defValue);
    }

    /**
     * 更新锁定应用时的数据
     */
    public static void saveLockData(Context context) {
        int errorLockTime = SpfUtils.getInt(context, ConstantValue.ERROR_LOCK_TIME) * 60 * 1000;
        SpfUtils.saveLong(context, ConstantValue.UNLOCK_DATE, System.currentTimeMillis() + errorLockTime);
        SpfUtils.saveInt(context, ConstantValue.ERROR_LOCK_NUMBER, 0);
    }

    /**
     * 更新解锁应用时的数据
     */
    public static void saveUnLockData(Context context) {
        SpfUtils.saveLong(context, ConstantValue.UNLOCK_DATE, 0L);
        SpfUtils.saveLong(context, ConstantValue.LAST_ERROR_PASSWORD_DATE, 0L);
        SpfUtils.saveInt(context, ConstantValue.ERROR_LOCK_NUMBER, 0);
    }

    /**
     * 初始化数据
     */
    public static void saveConfigData(Context context) {
        // 设定离开应用锁定时间
        SpfUtils.saveInt(context, ConstantValue.LEAVE_TIME, ConstantValue.LEAVE_TIME_DEFAULT_VALUE);
        // 设定应用手势/密码解锁错误锁定最大次数
        SpfUtils.saveInt(context, ConstantValue.ERROR_MAX_LOCK_NUMBER, ConstantValue.ERROR_MAX_LOCK_NUMBER_DEFAULT_VALUE);
        // 应用手势/密码接错错误次数
        SpfUtils.saveInt(context, ConstantValue.ERROR_LOCK_NUMBER, 0);
        // 最后一次输入错误密码时间戳
        SpfUtils.saveLong(context, ConstantValue.LAST_ERROR_PASSWORD_DATE, 0L);
        // 锁定应用时间
        SpfUtils.saveInt(context, ConstantValue.ERROR_LOCK_TIME, 1);
        // 解锁应用时间戳
        SpfUtils.saveLong(context, ConstantValue.UNLOCK_DATE, 0L);
        // 手势密码失效时长
        SpfUtils.saveInt(context, ConstantValue.IMAGE_LOCK_EXPIRY_TIME, ConstantValue.DEFAULT_EXPIRY_TIME);
        // 手势密码失效时间戳
        SpfUtils.saveLong(context, ConstantValue.IMAGE_LOCK_EXPIRY_DATE, 0L);
        // 传统密码失效时长
        SpfUtils.saveInt(context, ConstantValue.PASSWORD_EXPIRY_TIME, ConstantValue.DEFAULT_EXPIRY_TIME);
        // 传统密码失效时间戳
        SpfUtils.saveLong(context, ConstantValue.PASSWORD_EXPIRY_DATE, 0L);
    }

    /**
     * 更新错误输入次数时的数据
     */
    public static void updateErrorNumberData(Context context, int errorNumber) {
        SpfUtils.saveInt(context, ConstantValue.ERROR_LOCK_NUMBER, errorNumber);
        SpfUtils.saveLong(context, ConstantValue.LAST_ERROR_PASSWORD_DATE, System.currentTimeMillis());
    }

    /**
     * 更新手势密码提示修改密码时间
     */
    public static void updateImageExpiryDate(Context context) {
        int time = SpfUtils.getInt(context, ConstantValue.IMAGE_LOCK_EXPIRY_TIME);
        SpfUtils.saveLong(context, ConstantValue.IMAGE_LOCK_EXPIRY_DATE, System.currentTimeMillis() + DateUtils.getDayTimeMillis(time));
    }

    /**
     * 更新输入密码提示修改密码时间
     */
    public static void updatePasswordExpiryDate(Context context) {
        int time = SpfUtils.getInt(context, ConstantValue.PASSWORD_EXPIRY_TIME);
        SpfUtils.saveLong(context, ConstantValue.PASSWORD_EXPIRY_DATE, System.currentTimeMillis() + DateUtils.getDayTimeMillis(time));
    }

    /**
     * 更新手势密码提示修改密码时长 单位 天
     */
    public static void updateImageExpiryTime(Context context, int day) {
        SpfUtils.saveInt(context, ConstantValue.IMAGE_LOCK_EXPIRY_TIME, day);
        SpfUtils.updateImageExpiryDate(context);
    }

    /**
     * 更新输入密码提示修改密码时长 单位 天
     */
    public static void updatePasswordExpiryTime(Context context, int day) {
        SpfUtils.saveInt(context, ConstantValue.PASSWORD_EXPIRY_TIME, day);
        SpfUtils.updatePasswordExpiryDate(context);
    }
}
