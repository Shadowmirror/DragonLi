package miao.kmirror.dragonli.singleActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.Button;

import java.util.concurrent.Executor;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.EditActivity;
import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.bean.LockType;
import miao.kmirror.dragonli.dao.TextInfoDao;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class SingleFingerLockActivity extends AppCompatActivity {

    private TextInfo textInfo;
    private TextInfoDao textInfoDao = new TextInfoDao();
    private boolean deleteLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        textInfo = (TextInfo) getIntent().getSerializableExtra("textInfo");
        deleteLock = (boolean) getIntent().getBooleanExtra("deleteLock", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_finger_lock);
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("请验证指纹")
                .setNegativeButtonText("取消")
                .build();
        Button btVerifyFinger = findViewById(R.id.bt_verify_finger);
        btVerifyFinger.setOnClickListener(v -> {
            getPrompt().authenticate(promptInfo);
        });
        getPrompt().authenticate(promptInfo);
    }


    private BiometricPrompt getPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                ToastUtils.toastShort(SingleFingerLockActivity.this, errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (textInfo.getLocked() && deleteLock == false) {
                    // 临时解锁
                    finish();
                    ActivityUtils.simpleIntentWithTextInfo(SingleFingerLockActivity.this,
                            EditActivity.class,
                            textInfo,
                            "tempUnlock",
                            LockType.TEMP_UNLOCK);
                } else if(textInfo.getLocked() && deleteLock == true) {
                    // 删锁
                    textInfo.setLocked(LockType.UNABLE_LOCK);
                    textInfo.setLockType(LockType.NON_LOCK);
                    textInfoDao.update(textInfo);
                    finish();
                }else{
                    // 加锁
                    textInfo.setLocked(true);
                    textInfo.setLockType(LockType.FINGER_LOCK);
                    textInfoDao.update(textInfo);
                    ActivityUtils.flagActivityClearTask(SingleFingerLockActivity.this, MainActivity.class);
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                ToastUtils.toastShort(SingleFingerLockActivity.this, "指纹验证失败！");
            }
        };

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);
        return biometricPrompt;
    }

}