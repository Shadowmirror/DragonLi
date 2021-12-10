package miao.kmirror.dragonli.lock.widget.activity;


import android.content.Intent;
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
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;


public class FingerLoginActivity extends AppCompatActivity {
    private boolean isLock;
    private TextInfo textInfo;
    private TextInfoDao textInfoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        textInfo = (TextInfo) getIntent().getSerializableExtra("textInfo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_login);
        Button btVerifyFinger = findViewById(R.id.bt_verify_finger);
        btVerifyFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("请验证指纹")
                        .setNegativeButtonText("取消")
                        .build();
                getPrompt().authenticate(promptInfo);
            }
        });
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
                if (textInfo.getLocked() == false) {
                    textInfo.setLocked(true);
                    textInfo.setLockType(1);
                    textInfoDao.update(textInfo);
                    ToastUtils.toastShort(getApplicationContext(), textInfoDao.findById(textInfo.getId()).toString());
                }else{
                    ToastUtils.toastShort(FingerLoginActivity.this, "Authentication Succeeded!!");
                    ActivityUtils.flagActivityClearTask(FingerLoginActivity.this, MainActivity.class);
                }

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