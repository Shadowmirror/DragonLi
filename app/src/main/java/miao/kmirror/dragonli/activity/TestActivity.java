package miao.kmirror.dragonli.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import miao.kmirror.dragonli.R;
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

    private EditText etAppName;
    private EditText etAppPackageName;
    private Button addApp;
    private Button addWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    public void initView() {
        etAppName = findViewById(R.id.et_app_name);
        etAppPackageName = findViewById(R.id.et_app_package_name);
        addApp = findViewById(R.id.add_app);
        addWeb = findViewById(R.id.add_web);
        addApp.setOnClickListener(v -> {
            saveApp();
        });
        addWeb.setOnClickListener(v -> {
//            saveWeb();

        });


        Button test = findViewById(R.id.test);
        test.setOnClickListener(v -> {

        });

    }

    private void saveApp() {
        String appName = etAppName.getText().toString();
        String appPackageName = etAppPackageName.getText().toString();
        AppPackage appPackage = new AppPackage();
        appPackage.setAppPackageName(appPackageName);
        appPackage.setAppName(appName);
        appPackageDao.save(appPackage);
//        Log.i(TAG, "saveApp: 是否保存成功 = " + appPackageDao.save(appPackage));
    }

}