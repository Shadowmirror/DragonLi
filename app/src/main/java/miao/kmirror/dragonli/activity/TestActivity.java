package miao.kmirror.dragonli.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


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
import miao.kmirror.dragonli.adapter.AppItemClickListener;
import miao.kmirror.dragonli.dao.AppPackageDao;
import miao.kmirror.dragonli.dao.WebInfoDao;
import miao.kmirror.dragonli.entity.AppPackage;
import miao.kmirror.dragonli.entity.WebInfo;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.JsonUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class TestActivity extends AppCompatActivity implements AppItemClickListener {

    public static final String TAG = "TextActivity";

    private AppPackageDao appPackageDao = new AppPackageDao();
    private WebInfoDao webInfoDao = new WebInfoDao();

    private List<AppPackage> appPackages;
    private AppAdapter mAppAdapter;
    private RecyclerView mRecyclerView;
    public PopupWindow popupWindow;
    private TextView testAppName;
    private TextView testAppPackage;
    private ImageView testPopup;
    private Button testButton;
    private LinearLayout testShowInfo;
    private AppPackage currentApp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        initData();
    }

    public void initView(){
        testAppName = findViewById(R.id.test_app_name);
        testAppPackage = findViewById(R.id.test_app_package);
        testShowInfo = findViewById(R.id.test_show_info);
        testShowInfo.setOnClickListener(v -> {
            showPopupWindow();
        });

        testButton = findViewById(R.id.test_button);
        testButton.setOnClickListener(v->{
            ActivityUtils.goApp(this, currentApp);
        });
    }

    public void initData(){
        appPackages = new ArrayList<>();
        this.appPackages = appPackageDao.findAll();
        currentApp = appPackages.get(0);
        testAppName.setText(currentApp.getAppName());
        testAppPackage.setText(currentApp.getAppPackageName());
    }

    private void showPopupWindow() {
        View view = LayoutInflater.from(TestActivity.this).inflate(R.layout.popupwindow, null);

        mRecyclerView = view.findViewById(R.id.test_recyclerView);
        popupWindow = new PopupWindow(TestActivity.this);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.setFocusable(true);
        initPopupData();
        popupWindow.showAsDropDown(testShowInfo);
    }

    public void initPopupData() {
        mAppAdapter = new AppAdapter(this, appPackages);
        mAppAdapter.setOnItemClickListener(this);


        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setAdapter(mAppAdapter);

//        mAppAdapter.refreshData(appPackages);


    }

    @Override
    public void onItemClick(View v, int position) {
        AppPackage appPackage = appPackages.get(position);
        currentApp = appPackage;
        testAppName.setText(appPackage.getAppName());
        testAppPackage.setText(appPackage.getAppPackageName());
        popupWindow.dismiss();
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