package miao.kmirror.dragonli.lock.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.LoginActivity;
import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.constant.ConstantValue;
import miao.kmirror.dragonli.lock.widget.ImageLockView;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.DateUtils;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class ImageUnlockActivity extends AppCompatActivity implements ImageLockView.OnGraphChangedListener {

    public static final String TAG = "ImageUnlockActivity";
    private ImageLockView mImageLockView;
    private Button mTvCancel;
    private TextView mTvTitle;
    private int errorMaxLockNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_unlock);

        Toolbar myToolBar = findViewById(R.id.test_toolbar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();
        initView();

    }

    private void initData() {
        errorMaxLockNumber = SpfUtils.getInt(this, ConstantValue.ERROR_MAX_LOCK_NUMBER);
    }

    private void initView() {
        mTvTitle = findViewById(R.id.tv_title);
        mImageLockView = findViewById(R.id.il_graphical_pw);
        mTvCancel = findViewById(R.id.bt_cancel);

        mTvCancel.setOnClickListener(v -> {
            onBackPressed();
        });
        mImageLockView.setOnGraphChangedListener(this);
        mImageLockView.setAlpha(70);
        mImageLockView.setDefaultColor(getResources().getColor(R.color.color_graphical_default_color));
        mImageLockView.setChooseColor(getResources().getColor(R.color.color_graphical_choose_color));
    }

    @Override
    public void onGraphFinish(String password) {
        int errorLockNumber = SpfUtils.getInt(this, ConstantValue.ERROR_LOCK_NUMBER);
        if (MD5Utils.getMD5Code(password).equals(SpfUtils.getString(this, PasswordUtils.IMAGE_PASSWORD))) {
            ActivityUtils.flagActivityClearTask(this, MainActivity.class);
        } else {
            if (++errorLockNumber < errorMaxLockNumber) {
                mImageLockView.setMatch(false);
                mImageLockView.setEnable(false);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    /**
                     *要执行的操作
                     */
                    mImageLockView.resetGraphicalPassword();
                    mImageLockView.setEnable(true);
                }, 1000);
                ToastUtils.toastShort(ImageUnlockActivity.this, "密码错误，还有 " + (errorMaxLockNumber - errorLockNumber) + " 次机会尝试");
                SpfUtils.updateErrorNumberData(getApplication(), errorLockNumber);
            } else {
                SpfUtils.saveLockData(this);
                ToastUtils.toastLong(getApplication(), "错误次数过多应用已被锁定");
                ActivityUtils.flagActivityClearTask(this, LoginActivity.class);
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DateUtils.isLastErrorPasswordExpired(getApplication());
    }
}