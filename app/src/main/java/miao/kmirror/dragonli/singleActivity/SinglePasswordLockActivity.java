package miao.kmirror.dragonli.singleActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.bean.LockType;
import miao.kmirror.dragonli.dao.TextInfoDao;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class SinglePasswordLockActivity extends AppCompatActivity {
    private TextInfo textInfo;
    private TextInfoDao textInfoDao = new TextInfoDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        textInfo = (TextInfo) getIntent().getSerializableExtra("textInfo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_password_lock);
        EditText EtPassword = findViewById(R.id.et_password);
        EditText EtAgainPassword = findViewById(R.id.et_again_password);
        Button verify = findViewById(R.id.bt_verify_password);
        verify.setOnClickListener(v -> {
            String password = EtPassword.getText().toString();
            String againPassword = EtAgainPassword.getText().toString();
            if (password.equals(againPassword)) {
                textInfo.setPassword(MD5Utils.getMD5Code(password));
                textInfo.setLocked(true);
                textInfo.setLockType(LockType.PASSWORD_LOCK);
                textInfoDao.update(textInfo);
                ToastUtils.toastShort(this, textInfoDao.findById(textInfo.getId()).toString());
                ActivityUtils.flagActivityClearTask(this, MainActivity.class);
            } else {
                ToastUtils.toastShort(this, "两次密码不一致请重新输入！！！");
            }
        });
    }
}