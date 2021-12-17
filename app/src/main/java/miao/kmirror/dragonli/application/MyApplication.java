package miao.kmirror.dragonli.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import org.litepal.LitePal;

import miao.kmirror.dragonli.activity.LoginActivity;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.ToastUtils;


/**
 * @author Kmirror
 */
public class MyApplication extends Application {

    public int leaveTime = 5;

    private int activityCount = 0;

    public boolean isForeground;


    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }


    /**
     * Activity 生命周期监听，用于监控app前后台状态切换
     */
    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {

        public long stopTime = 0;
        public long restartTime = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (activityCount == 0) {
                //app回到前台
                isForeground = true;
                if (stopTime != 0) {
                    restartTime = System.currentTimeMillis();
                    // 离开应用时长
                    if ((restartTime - stopTime) / 1000 >= leaveTime) {
                        ToastUtils.toastShort(MyApplication.this, "离开本应用已 " + leaveTime + " 秒，本应用自动锁定");
                        stopTime = 0;
                        ActivityUtils.flagActivityClearTask(MyApplication.this, LoginActivity.class);
                    }
                }

            }
            activityCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityCount--;
            if (activityCount == 0) {
                isForeground = false;
                stopTime = System.currentTimeMillis();
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

}
