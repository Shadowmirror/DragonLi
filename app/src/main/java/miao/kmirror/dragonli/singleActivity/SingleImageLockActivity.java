package miao.kmirror.dragonli.singleActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.EditActivity;
import miao.kmirror.dragonli.activity.LoginActivity;
import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.bean.LockType;
import miao.kmirror.dragonli.dao.TextInfoDao;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.lock.widget.ImageLockView;
import miao.kmirror.dragonli.lock.widget.activity.ImageUnlockActivity;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.MD5Utils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class SingleImageLockActivity extends AppCompatActivity implements ImageLockView.OnGraphChangedListener {

    private ImageLockView mImageLockView;
    private TextView mTvTitle;
    private Button mTvCancel;
    private TextInfo textInfo;
    private TextInfoDao textInfoDao = new TextInfoDao();
    private boolean deleteLock = false;
    private boolean isMatch = false;
    /**
     * 记录第一次绘制密码
     * */
    private String imagePassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        textInfo = (TextInfo) getIntent().getSerializableExtra("textInfo");
        deleteLock = (boolean) getIntent().getBooleanExtra("deleteLock", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image_lock);
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
        String tempPassword = MD5Utils.getMD5Code(password);
        if(!textInfo.getLocked()){
            // 加锁
            if (isMatch == false) {
                imagePassword = password;
                mTvTitle.setText("请再次绘制");
                mImageLockView.resetGraphicalPassword();
                isMatch = true;
            } else {
                if (imagePassword.equals(password)) {
                    textInfo.setLocked(true);
                    textInfo.setPassword(MD5Utils.getMD5Code(imagePassword));
                    textInfo.setLockType(LockType.IMAGE_LOCK);
                    textInfoDao.update(textInfo);
                    ActivityUtils.flagActivityClearTask(SingleImageLockActivity.this, MainActivity.class);
                } else {
                    imagePassword = "";
                    isMatch = false;
                    mTvTitle.setText("两次图形不一致，请重新绘制");
                    mImageLockView.resetGraphicalPassword();
                }
            }
        } else if(textInfo.getLocked() && textInfo.getPassword().equals(tempPassword)){
            if (textInfo.getLocked() && deleteLock == false) {
                // 临时解锁
                finish();
                ActivityUtils.simpleIntentWithTextInfo(SingleImageLockActivity.this,
                        EditActivity.class,
                        textInfo,
                        "tempUnlock",
                        LockType.TEMP_UNLOCK);
            } else if(textInfo.getLocked() && deleteLock == true){
                // 删锁
                textInfo.setLocked(LockType.UNABLE_LOCK);
                textInfo.setLockType(LockType.NON_LOCK);
                textInfo.setPassword("");
                textInfoDao.update(textInfo);
                finish();
            }
        }else{
            mImageLockView.setMatch(false);
            ToastUtils.toastShort(SingleImageLockActivity.this, "密码错误");
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    mImageLockView.resetGraphicalPassword();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 1000);
        }
    }
}