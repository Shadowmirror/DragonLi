package miao.kmirror.dragonli.lock.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        Button btVerifyFinger = findViewById(R.id.bt_verify_finger);
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("请验证指纹")
                .setNegativeButtonText("取消")
                .build();
        btVerifyFinger.setOnClickListener(new View.OnClickListener() {
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
                ToastUtils.toastShort(FingerLoginActivity.this, "Authentication Succeeded!!");
                ActivityUtils.flagActivityClearTask(FingerLoginActivity.this, MainActivity.class);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                ToastUtils.toastShort(FingerLoginActivity.this, "Authentication Failed!");
            }
        };

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);
        return biometricPrompt;
    }


}