package miao.kmirror.dragonli.singleActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.EditActivity;
import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.bean.LockType;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class SinglePasswordUnlockActivity extends AppCompatActivity {
    private EditText EtPassword;
    private Button BtVerifyPassword;
    private TextInfo textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        textInfo = (TextInfo) getIntent().getSerializableExtra("textInfo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_password_unlock);
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
            if (tempPassword.equals(textInfo.getPassword())) {
                finish();
                ActivityUtils.simpleIntentWithTextInfo(this,
                        EditActivity.class,
                        textInfo,
                        "tempUnlock",
                        LockType.TEMP_UNLOCK);
            } else {
                ToastUtils.toastShort(this, "密码错误");
                return;
            }
        }
    }
}