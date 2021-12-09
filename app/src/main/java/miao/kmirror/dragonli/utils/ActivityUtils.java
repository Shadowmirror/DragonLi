package miao.kmirror.dragonli.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import miao.kmirror.dragonli.activity.MainActivity;

public class ActivityUtils {
    public static void flagActivityClearTask(Context thisContext, Class flagActivity){
        Intent intent = new Intent(thisContext, flagActivity)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        thisContext.startActivity(intent);
    }

    public static void simpleIntent(Context thisContext, Class flagActivity){
        Intent intent = new Intent(thisContext, flagActivity);
        thisContext.startActivity(intent);
    }
}
