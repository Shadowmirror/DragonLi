package miao.kmirror.dragonli.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.lock.activity.FingerLoginActivity;
import miao.kmirror.dragonli.lock.activity.ImageUnlockActivity;
import miao.kmirror.dragonli.lock.activity.PasswordLoginActivity;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;
import miao.kmirror.dragonli.utils.ToolbarUtils;

public class LoginActivity extends AppCompatActivity{
    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(SpfUtils.getString(this, PasswordUtils.COMMON_PASSWORD) == "" && SpfUtils.getString(this, PasswordUtils.IMAGE_PASSWORD) == ""){
            // 第一次使用本应用
            ActivityUtils.flagActivityClearTask(this, FirstUseActivity.class);
            ToastUtils.toastShort(this, "第一次使用本应用，请使用设置应用密码！");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ToolbarUtils.initToolBar(this, false);

        Button password = findViewById(R.id.login_password);
        Button finger = findViewById(R.id.login_finger);
        Button image = findViewById(R.id.login_image);
        password.setOnClickListener(v -> {
            ActivityUtils.simpleIntent(this, PasswordLoginActivity.class);
        });
        finger.setOnClickListener(v -> {
            ActivityUtils.simpleIntent(this, FingerLoginActivity.class);
        });
        image.setOnClickListener(v -> {
            ActivityUtils.simpleIntent(this, ImageUnlockActivity.class);
        });
    }

}