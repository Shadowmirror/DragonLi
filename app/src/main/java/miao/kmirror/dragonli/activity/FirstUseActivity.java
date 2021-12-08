package miao.kmirror.dragonli.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.lock.widget.activity.ImageLockActivity;
import miao.kmirror.dragonli.lock.widget.activity.PasswordLoginActivity;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class FirstUseActivity extends AppCompatActivity {

    private EditText EtPassword;
    private EditText EtAgainPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_use);
        EtPassword = findViewById(R.id.et_password);
        EtAgainPassword = findViewById(R.id.et_again_password);
        Button toSettingImage = findViewById(R.id.to_setting_image);
        toSettingImage.setOnClickListener(v -> {
            String password = EtPassword.getText().toString();
            String againPassword = EtAgainPassword.getText().toString();
            if (password.equals(againPassword)) {
                SpfUtils.saveString(getApplicationContext(), PasswordUtils.COMMON_PASSWORD, MD5Utils.getMD5Code(password));
                Intent intent = new Intent(this, ImageLockActivity.class);
                startActivity(intent);
            } else {
                ToastUtils.toastShort(this, "两次密码不一致请重新输入！！！");
            }
        });
    }
}