package miao.kmirror.dragonli.utils;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import miao.kmirror.dragonli.R;

public class ToolbarUtils {
    public static void initToolBar(AppCompatActivity activity){
        Toolbar myToolBar = activity.findViewById(R.id.test_toolbar);
        activity.setSupportActionBar(myToolBar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void initToolBar(AppCompatActivity activity, boolean isDisplayHome){
        Toolbar myToolBar = activity.findViewById(R.id.test_toolbar);
        activity.setSupportActionBar(myToolBar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(isDisplayHome);
    }
}
