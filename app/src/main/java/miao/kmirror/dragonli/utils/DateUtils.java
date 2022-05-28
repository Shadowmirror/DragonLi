package miao.kmirror.dragonli.utils;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import miao.kmirror.dragonli.constant.ConstantValue;

public class DateUtils {
    public static String getCurrentTimeFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }

    public static String getLockTimeFormat(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(SpfUtils.getLong(context, ConstantValue.UNLOCK_DATE));
        return sdf.format(date);
    }

    /**
     * 锁定时间是否过期
     */
    public static Boolean lockExpired(Context context) {
        return System.currentTimeMillis() > SpfUtils.getLong(context, ConstantValue.UNLOCK_DATE);
    }

    /**
     * 检测最后一次输入密码的时间是否超过锁定应用时长
     */
    public static Boolean isLastErrorPasswordExpired(Context context) {
        Long errorLockTime = SpfUtils.getInt(context, ConstantValue.ERROR_LOCK_TIME) * 60 * 1000L;
        Long lastErrorPasswordDate = SpfUtils.getLong(context, ConstantValue.LAST_ERROR_PASSWORD_DATE);
        if (System.currentTimeMillis() > (lastErrorPasswordDate + errorLockTime)) {
            // 设置最后一次错误密码的时间为空
            SpfUtils.saveLong(context, ConstantValue.LAST_ERROR_PASSWORD_DATE, 0L);
            // 置空错误密码次数
            SpfUtils.saveInt(context, ConstantValue.ERROR_LOCK_NUMBER, 0);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 手势密码或者输入密码是否过期
     * 0 为均未过期
     * 1 为手势密码过期
     * 2 为输入密码过期
     * 3 为两者均过期
     */
    public static int passwordExpiry(Context context) {
        Long imageLockExpiryDate = SpfUtils.getLong(context, ConstantValue.IMAGE_LOCK_EXPIRY_DATE);
        Long passwordLockExpiryDate = SpfUtils.getLong(context, ConstantValue.PASSWORD_EXPIRY_DATE);
        Long currentDate = System.currentTimeMillis();
        if (imageLockExpiryDate <= currentDate && passwordLockExpiryDate <= currentDate) {
            return 3;
        } else if (imageLockExpiryDate <= currentDate) {
            return 1;
        } else if (passwordLockExpiryDate <= currentDate) {
            return 2;
        } else {
            return 0;
        }
    }

    public static Long getDayTimeMillis(int day) {
        return day * 24 * 3600 * 1000L;
    }
}
