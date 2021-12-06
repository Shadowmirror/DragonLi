package miao.kmirror.dragonli.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getCurrentTimeFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }
}
