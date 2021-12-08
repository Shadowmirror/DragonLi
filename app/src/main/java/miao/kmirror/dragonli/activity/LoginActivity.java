package miao.kmirror.dragonli.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.lock.widget.activity.FingerLoginActivity;
import miao.kmirror.dragonli.lock.widget.activity.ImageUnlockActivity;
import miao.kmirror.dragonli.lock.widget.activity.PasswordLoginActivity;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;

public class LoginActivity extends AppCompatActivity{

    private String commonPassword = SpfUtils.getString(getApplicationContext(), PasswordUtils.COMMON_PASSWORD);
    private String imagePassword = SpfUtils.getString(getApplicationContext(), PasswordUtils.IMAGE_PASSWORD);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(commonPassword == "" && imagePassword == ""){
            Intent intent = new Intent(this, FirstUseActivity.class);
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button password = findViewById(R.id.login_password);
        Button finger = findViewById(R.id.login_finger);
        Button image = findViewById(R.id.login_image);
        password.setOnClickListener(v -> {
            Intent intent = new Intent(this, PasswordLoginActivity.class);
            startActivity(intent);
        });
        finger.setOnClickListener(v -> {
            Intent intent = new Intent(this, FingerLoginActivity.class);
            startActivity(intent);
        });
        image.setOnClickListener(v -> {
            Intent intent = new Intent(this, ImageUnlockActivity.class);
            startActivity(intent);
        });
    }
}