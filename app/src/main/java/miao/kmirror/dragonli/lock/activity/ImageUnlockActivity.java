package miao.kmirror.dragonli.lock.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.lock.widget.ImageLockView;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class ImageUnlockActivity extends AppCompatActivity implements ImageLockView.OnGraphChangedListener {

    public static final String TAG = "ImageUnlockActivity";
    private ImageLockView mImageLockView;
    private Button mTvCancel;
    private TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_unlock);
        initView();

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
        if (MD5Utils.getMD5Code(password).equals(SpfUtils.getString(this, PasswordUtils.IMAGE_PASSWORD))) {
            ActivityUtils.flagActivityClearTask(this, MainActivity.class);
        } else {
            mImageLockView.setMatch(false);
            ToastUtils.toastShort(ImageUnlockActivity.this, "密码错误");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /**
                     *要执行的操作
                     */
                    mImageLockView.resetGraphicalPassword();
                }
            }, 1000);
        }
    }
}