package miao.kmirror.dragonli.lock.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.dao.TextInfoDao;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;
import miao.kmirror.dragonli.utils.ToolbarUtils;


public class FingerLoginActivity extends AppCompatActivity {
    private boolean isLock;
    private TextInfo textInfo;
    private TextInfoDao textInfoDao = new TextInfoDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        textInfo = (TextInfo) getIntent().getSerializableExtra("textInfo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_login);
        ToolbarUtils.initToolBar(this);
        ImageView loginFinger = findViewById(R.id.login_finger);
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("请验证指纹")
                .setNegativeButtonText("取消")
                .build();
        loginFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrompt().authenticate(promptInfo);
            }
        });
        getPrompt().authenticate(promptInfo);
    }

    private BiometricPrompt getPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                ToastUtils.toastShort(FingerLoginActivity.this, errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                ToastUtils.toastShort(FingerLoginActivity.this, "指纹验证成功");
                ActivityUtils.flagActivityClearTask(FingerLoginActivity.this, MainActivity.class);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                ToastUtils.toastShort(FingerLoginActivity.this, "指纹验证失败");
            }
        };

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);
        return biometricPrompt;
    }


}