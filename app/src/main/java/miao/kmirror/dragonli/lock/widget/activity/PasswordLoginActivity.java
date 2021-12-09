package miao.kmirror.dragonli.lock.widget.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.utils.AESEncryptUtils;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class PasswordLoginActivity extends AppCompatActivity{
    public static final String TAG = "PasswordLoginActivity";
    private EditText EtPassword;
    private Button BtVerifyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_login);

        // 密码登录
        EtPassword = findViewById(R.id.et_password);
        BtVerifyPassword = findViewById(R.id.bt_verify_password);

        BtVerifyPassword.setOnClickListener(v -> {
            verifyPassword();
        });
    }

    public void verifyPassword() {
        String password = EtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.toastShort(this, "密码不能为空");
            return;
        } else {
            String tempPassword = MD5Utils.getMD5Code(password);
            if (tempPassword.equals(SpfUtils.getString(this, PasswordUtils.COMMON_PASSWORD))) {
                ActivityUtils.flagActivityClearTask(this, MainActivity.class);
            } else {
                ToastUtils.toastShort(this, "密码错误");
                return;
            }
        }
    }
}