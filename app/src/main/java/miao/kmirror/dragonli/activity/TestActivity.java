package miao.kmirror.dragonli.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.adapter.AppAdapter;
import miao.kmirror.dragonli.dao.AppPackageDao;
import miao.kmirror.dragonli.dao.WebInfoDao;
import miao.kmirror.dragonli.entity.AppPackage;
import miao.kmirror.dragonli.entity.WebInfo;
import miao.kmirror.dragonli.utils.JsonUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class TestActivity extends AppCompatActivity {

    public static final String TAG = "TextActivity";

    private AppPackageDao appPackageDao = new AppPackageDao();
    private WebInfoDao webInfoDao = new WebInfoDao();

    private List<AppPackage> appPackages;
    private AppAdapter mAppAdapter;
    private RecyclerView mRecyclerView;
    private Button test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        test = findViewById(R.id.test);
        test.setOnClickListener(v -> {
            showPopupWindow();
        });
    }

    private void showPopupWindow() {
        View view = LayoutInflater.from(TestActivity.this).inflate(R.layout.popupwindow, null);

        mRecyclerView = view.findViewById(R.id.test_recyclerView);
        initData();
        PopupWindow popupWindow = new PopupWindow(TestActivity.this);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(test);
    }

    public void initData() {
        appPackages = new ArrayList<>();
        this.appPackages = appPackageDao.findAll();
        mAppAdapter = new AppAdapter(appPackages);


        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setAdapter(mAppAdapter);

//        mAppAdapter.refreshData(appPackages);


    }

//    private void saveApp() {
//        String appName = etAppName.getText().toString();
//        String appPackageName = etAppPackageName.getText().toString();
//        AppPackage appPackage = new AppPackage();
//        appPackage.setAppPackageName(appPackageName);
//        appPackage.setAppName(appName);
//        appPackageDao.save(appPackage);
////        Log.i(TAG, "saveApp: 是否保存成功 = " + appPackageDao.save(appPackage));
//    }


}