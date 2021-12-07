package miao.kmirror.dragonli.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.utils.AESEncryptUtils;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class LoginActivity extends AppCompatActivity{

    private EditText EtPassword;
    private Button BtVerifyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
            String verifyPassword = MD5Utils.getMD5Code(AESEncryptUtils.TEST_PASS);
            if (verifyPassword.equals(tempPassword)) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                ToastUtils.toastShort(this, "密码错误");
                return;
            }
        }
    }
}