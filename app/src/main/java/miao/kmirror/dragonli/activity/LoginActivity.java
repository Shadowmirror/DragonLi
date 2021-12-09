package miao.kmirror.dragonli.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.lock.widget.activity.FingerLoginActivity;
import miao.kmirror.dragonli.lock.widget.activity.ImageUnlockActivity;
import miao.kmirror.dragonli.lock.widget.activity.PasswordLoginActivity;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;

public class LoginActivity extends AppCompatActivity{
    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(SpfUtils.getString(this, PasswordUtils.COMMON_PASSWORD) == "" && SpfUtils.getString(this, PasswordUtils.IMAGE_PASSWORD) == ""){
            Intent intent = new Intent(this, FirstUseActivity.class);
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button password = findViewById(R.id.login_password);
        Button finger = findViewById(R.id.login_finger);
        Button image = findViewById(R.id.login_image);
        password.setOnClickListener(v -> {
            ActivityUtils.simpleIntent(this, PasswordLoginActivity.class);
        });
        finger.setOnClickListener(v -> {
            ActivityUtils.simpleIntent(this, FingerLoginActivity.class);
        });
        image.setOnClickListener(v -> {
            ActivityUtils.simpleIntent(this, ImageUnlockActivity.class);
        });
    }
}