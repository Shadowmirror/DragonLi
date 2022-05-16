package miao.kmirror.dragonli.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.ConfirmPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.concurrent.Executor;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.constant.ConstantValue;
import miao.kmirror.dragonli.lock.activity.FingerLoginActivity;
import miao.kmirror.dragonli.lock.activity.ImageUnlockActivity;
import miao.kmirror.dragonli.lock.activity.PasswordLoginActivity;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.DateUtils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;
import miao.kmirror.dragonli.utils.ToolbarUtils;

/**
 * @author Kmirror
 */
public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    private BiometricPrompt.PromptInfo promptInfo;
    private ImageView loginFinger;
    private TextView imageOrPasswordLogin;
    private TextView loginImage;
    private TextView loginPassword;
    private BottomSheetDialog bottomSheetDialog;
    ConfirmPopupView lockPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ToolbarUtils.initToolBar(this, false);
        if (SpfUtils.getString(this, PasswordUtils.COMMON_PASSWORD) == "" || SpfUtils.getString(this, PasswordUtils.IMAGE_PASSWORD) == "") {
            // 第一次使用本应用
            ActivityUtils.flagActivityClearTask(this, FirstUseActivity.class);
            ToastUtils.toastShort(this, "第一次使用本应用，请使用设置应用密码！");
        } else {
            initData();
            initView();
        }

    }

    private void initData() {
        lockPopup = new XPopup.Builder(this)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asConfirm("应用已被锁定", "应用将于 " + DateUtils.getLockTimeFormat(this) + " 解锁", this::finish, this::finish);
    }

    void initView() {
        imageOrPasswordLogin = findViewById(R.id.image_or_password_login);
        imageOrPasswordLogin.setOnClickListener(v -> {
            showBottomSheetDialog();
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("请验证指纹")
                .setNegativeButtonText("取消")
                .build();
        loginFinger = findViewById(R.id.login_finger);
        loginFinger.setOnClickListener(v -> {
            getPrompt().authenticate(promptInfo);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: 重新进入登录页");
        if (DateUtils.lockExpired(getApplication())) {
            DateUtils.isLastErrorPasswordExpired(getApplication());
            getPrompt().authenticate(promptInfo);
        } else {
            lockPopup.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        lockPopup.dismiss();
    }

    void showBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
        loginImage = view.findViewById(R.id.login_image);
        loginPassword = view.findViewById(R.id.login_password);
        loginImage.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            ActivityUtils.simpleIntent(this, ImageUnlockActivity.class);
        });
        loginPassword.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            ActivityUtils.simpleIntent(this, PasswordLoginActivity.class);
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();
    }

    /**
     * 指纹验证功能
     */
    private BiometricPrompt getPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                ToastUtils.toastShort(LoginActivity.this, errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                ToastUtils.toastShort(LoginActivity.this, "指纹验证成功");
                ActivityUtils.flagActivityClearTask(LoginActivity.this, MainActivity.class);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                ToastUtils.toastShort(LoginActivity.this, "指纹验证失败");
            }
        };

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);
        return biometricPrompt;
    }


}