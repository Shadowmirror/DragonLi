package miao.kmirror.dragonli.lock.widget.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.LoginActivity;
import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.lock.widget.ImageLockView;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class ImageLockActivity extends AppCompatActivity implements ImageLockView.OnGraphChangedListener {
    public static final String TAG = "ImageLockActivity";
    private int passwordCount = 0;
    private ImageLockView mImageLockView;
    private TextView mTvCancel;
    private TextView mTvTitle;
    private boolean isMatch = false;
    private String imagePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_unlock);
        initView();
    }

    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mImageLockView = (ImageLockView) findViewById(R.id.il_graphical_pw);
        mTvCancel = (TextView) findViewById(R.id.bt_cancel);
        mImageLockView.setOnGraphChangedListener(this);
        mImageLockView.setAlpha(70);
        mImageLockView.setDefaultColor(getResources().getColor(R.color.color_graphical_default_color));
        mImageLockView.setChooseColor(getResources().getColor(R.color.color_graphical_choose_color));
        mTvCancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, PasswordLoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onGraphFinish(String password) {

        if (isMatch == false) {
            imagePassword = password;
            mTvTitle.setText("请再次绘制");
            mImageLockView.resetGraphicalPassword();
            isMatch = true;
        } else {
            if (imagePassword.equals(password)) {
                Log.i(TAG, "onGraphFinish: password = " + password);
                SpfUtils.saveString(this, PasswordUtils.IMAGE_PASSWORD, MD5Utils.getMD5Code(password));
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                ToastUtils.toastShort(this, "密码设置成功！");
            } else {
                imagePassword = "";
                isMatch = false;
                mTvTitle.setText("两次图形不一致，请重新绘制");
                mImageLockView.resetGraphicalPassword();
            }
        }

    }
}