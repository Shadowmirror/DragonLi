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

public class LoginActivity extends AppCompatActivity {

    private EditText EtPassword;
    private Button BtVerifyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_login);

        EtPassword = findViewById(R.id.et_password);
        BtVerifyPassword = findViewById(R.id.bt_verify_password);
        BtVerifyPassword.setOnClickListener(v -> {
            verifyPassword();
        });
    }

    public void verifyPassword(){
        String password = EtPassword.getText().toString();
        if(TextUtils.isEmpty(password)){
            ToastUtils.toastShort(this, "密码不能为空");
            return;
        }else{
            String tempPassword = MD5Utils.getMD5Code(password);
            String verifyPassword = MD5Utils.getMD5Code(AESEncryptUtils.TEST_PASS);
            if(verifyPassword.equals(tempPassword)){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else{
                ToastUtils.toastShort(this, "密码错误");
                return;
            }
        }
    }
}