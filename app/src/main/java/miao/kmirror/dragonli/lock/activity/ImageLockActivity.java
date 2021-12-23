package miao.kmirror.dragonli.lock.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.LoginActivity;
import miao.kmirror.dragonli.lock.widget.ImageLockView;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;
import miao.kmirror.dragonli.utils.ToolbarUtils;

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
        ToolbarUtils.initToolBar(this);
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
            finish();
        });
    }

    @Override
    public void onGraphFinish(String password) {

        if (isMatch == false) {
            imagePassword = password;
            mTvTitle.setText("请再次绘制请再次绘制");
            mImageLockView.resetGraphicalPassword();
            isMatch = true;
        } else {
            if (imagePassword.equals(password)) {
                SpfUtils.saveString(this, PasswordUtils.IMAGE_PASSWORD, MD5Utils.getMD5Code(password));
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                ToastUtils.toastShort(this, "密码设置成功！");
            } else {
                imagePassword = "";
                isMatch = false;
                mTvTitle.setText("两次图形不一致，请重新绘制");
                mImageLockView.setMatch(false);
                mImageLockView.setEnable(false);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         *要执行的操作
                         */
                        mImageLockView.resetGraphicalPassword();
                        mImageLockView.setEnable(true);
                    }
                }, mImageLockView.LOCK_TIME);
            }
        }

    }

    /**
     * 监听左上角返回事件
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}