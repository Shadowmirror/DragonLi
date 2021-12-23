package miao.kmirror.dragonli.navFunction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.lock.widget.ImageLockView;
import miao.kmirror.dragonli.singleActivity.SingleImageLockActivity;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;
import miao.kmirror.dragonli.utils.ToolbarUtils;

public class ChangeAppImage extends AppCompatActivity implements ImageLockView.OnGraphChangedListener {


    private ImageLockView mImageLockView;
    private TextView mTvTitle;
    private Button mTvCancel;
    /**
     * 记录是否重新设置密码
     */
    private boolean isReset = false;
    /**
     * 记录第一次绘制密码
     */
    private String imagePassword;
    private boolean isMatch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_app_image);
        ToolbarUtils.initToolBar(this, false);
        initView();
    }

    private void initView() {
        mTvTitle = findViewById(R.id.tv_title);
        mImageLockView = findViewById(R.id.il_graphical_pw);
        mTvCancel = findViewById(R.id.bt_cancel);
        mTvCancel.setOnClickListener(v -> {
            finish();
        });
        mImageLockView.setOnGraphChangedListener(this);
        mImageLockView.setAlpha(70);
        mImageLockView.setDefaultColor(getResources().getColor(R.color.color_graphical_default_color));
        mImageLockView.setChooseColor(getResources().getColor(R.color.color_graphical_choose_color));
    }

    @Override
    public void onGraphFinish(String password) {
        String tempPassword = MD5Utils.getMD5Code(password);

        if (!isReset) {
            // 验证应用手势锁
            String localPassword = SpfUtils.getString(this, "imagePassword");
            if (localPassword.equals(tempPassword)) {
                isReset = true;
                mImageLockView.resetGraphicalPassword();
                mTvTitle.setText("手势密码验证成功，请设置新的手势锁");
                ToastUtils.toastShort(this, "手势密码验证成功，请设置新的手势锁");
            } else {
                mImageLockView.setMatch(false);
                ToastUtils.toastShort(ChangeAppImage.this, "密码错误");
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
        } else {
            if (!isMatch) {
                mTvTitle.setText("请再输入一遍");
                imagePassword = tempPassword;
                mImageLockView.resetGraphicalPassword();
                isMatch = true;
            } else {
                if (imagePassword.equals(tempPassword)) {
                    SpfUtils.saveString(this, "imagePassword", imagePassword);
                    ToastUtils.toastShort(this, "手势锁修改成功！");
                    ActivityUtils.flagActivityClearTask(this, MainActivity.class);
                } else {
                    imagePassword = "";
                    isMatch = false;
                    mImageLockView.setMatch(false);
                    mTvTitle.setText("两次图形不一致，请重新绘制");
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
    }
}