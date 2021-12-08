package miao.kmirror.dragonli.lock.widget.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.MainActivity;
import miao.kmirror.dragonli.lock.widget.ImageLockView;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class ImageUnlockActivity extends AppCompatActivity implements View.OnClickListener, ImageLockView.OnGraphChangedListener {

    public static final String TAG = "ImageUnlockActivity";

    public static final String TEST_PASSWORD = "0124678";

    private ImageLockView mImageLockView;
    private TextView mTvCancel;
    private TextView mTvTitle;

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

        mTvCancel.setOnClickListener(this);
        mImageLockView.setOnGraphChangedListener(this);
        mImageLockView.setAlpha(70);
        mImageLockView.setDefaultColor(getResources().getColor(R.color.color_graphical_default_color));
        mImageLockView.setChooseColor(getResources().getColor(R.color.color_graphical_choose_color));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGraphFinish(String password) {
        Log.i(TAG, "onGraphFinish: password = " + password);
        if(password.equals(TEST_PASSWORD)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            mImageLockView.setMatch(false);
            ToastUtils.toastShort(ImageUnlockActivity.this, "密码错误");
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