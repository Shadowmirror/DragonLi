package miao.kmirror.dragonli.navFunction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;
import miao.kmirror.dragonli.utils.ToolbarUtils;

public class ChangeAppPassword extends AppCompatActivity {
    private EditText etLocalPassword;
    private EditText etPassword;
    private EditText etAgainPassword;
    private Boolean isAllChange = false;

    public static void startActivity(Context context, boolean isAllChange) {
        Intent intent = new Intent(context, ChangeAppPassword.class);
        intent.putExtra("isAllChange", isAllChange);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAllChange = getIntent().getBooleanExtra("isAllChange", false);
        setContentView(R.layout.activity_change_app_password);
        ToolbarUtils.initToolBar(this, false);
        etLocalPassword = findViewById(R.id.et_local_password);
        etPassword = findViewById(R.id.et_password);
        etAgainPassword = findViewById(R.id.et_again_password);
        Button btVerifyPassword = findViewById(R.id.bt_verify_password);
        btVerifyPassword.setOnClickListener(v -> {
            verify();
        });
    }

    private void verify() {
        String tempLocalPassword = MD5Utils.getMD5Code(etLocalPassword.getText().toString());
        String password = etPassword.getText().toString();
        String again_password = etAgainPassword.getText().toString();
        if (tempLocalPassword.equals(SpfUtils.getString(this, "commonPassword"))) {
            if (TextUtils.isEmpty(password) || TextUtils.isEmpty(again_password)) {
                ToastUtils.toastShortCenter(this, "密码不能为空");
            } else if (password.equals(again_password)) {
                password = MD5Utils.getMD5Code(password);
                SpfUtils.saveString(this, "commonPassword", password);
                ToastUtils.toastShort(this, "密码修改成功");
                SpfUtils.updatePasswordExpiryDate(getApplication());
                if (isAllChange) {
                    ChangeAppImage.startActivity(this);
                } else {
                    ActivityUtils.flagActivityClearTask(this, MainActivity.class);
                }
            } else {
                ToastUtils.toastShortCenter(this, "两次新密码不一致，请重新输入");
            }
        } else {
            ToastUtils.toastShortCenter(this, "原密码校验失败，请重试");
        }
    }
}