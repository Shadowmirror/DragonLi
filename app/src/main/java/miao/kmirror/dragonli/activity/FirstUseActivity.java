package miao.kmirror.dragonli.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.dao.AppPackageDao;
import miao.kmirror.dragonli.dao.WebInfoDao;
import miao.kmirror.dragonli.entity.AppPackage;
import miao.kmirror.dragonli.entity.WebInfo;
import miao.kmirror.dragonli.lock.activity.ImageLockActivity;
import miao.kmirror.dragonli.utils.JsonUtils;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class FirstUseActivity extends AppCompatActivity {

    private static final String TAG = "FirstUseActivity";
    private EditText EtPassword;
    private EditText EtAgainPassword;
    private AppPackageDao appPackageDao = new AppPackageDao();
    private WebInfoDao webInfoDao = new WebInfoDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_use);
        initLocalWebAndApp();
        EtPassword = findViewById(R.id.et_password);
        EtAgainPassword = findViewById(R.id.et_again_password);
        Button toSettingImage = findViewById(R.id.to_setting_image);
        toSettingImage.setOnClickListener(v -> {
            String password = EtPassword.getText().toString();
            String againPassword = EtAgainPassword.getText().toString();
            if (password.equals(againPassword)) {
                SpfUtils.saveString(this, PasswordUtils.COMMON_PASSWORD, MD5Utils.getMD5Code(password));
                Intent intent = new Intent(this, ImageLockActivity.class);
                startActivity(intent);
            } else {
                ToastUtils.toastShort(this, "两次密码不一致请重新输入！！！");
            }
        });
    }

    private void initLocalWebAndApp() {
        if (appPackageDao.findAll().size() == 0 && webInfoDao.findAll().size() == 0) {
            Gson gson = new Gson();
            List<AppPackage> appPackages = gson.fromJson(
                    JsonUtils.getJson("app.json", this),
                    new TypeToken<List<AppPackage>>() {
                    }.getType());
            appPackageDao.saveAll(appPackages);
            List<WebInfo> webInfoList = gson.fromJson(
                    JsonUtils.getJson("web.json", this),
                    new TypeToken<List<WebInfo>>() {
                    }.getType());
            webInfoDao.saveAll(webInfoList);
        }
    }
}