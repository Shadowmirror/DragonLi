package miao.kmirror.dragonli.activity;

import static java.lang.Math.E;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;


import miao.kmirror.dragonli.adapter.MyAdapter;
import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.application.MyApplication;
import miao.kmirror.dragonli.constant.ConstantValue;
import miao.kmirror.dragonli.dao.TextInfoDao;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.navFunction.ChangeAppImage;
import miao.kmirror.dragonli.navFunction.ChangeAppPassword;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;
import miao.kmirror.dragonli.widget.CustomCenterPopup;

public class MainActivity extends AppCompatActivity {

    private TextInfoDao textInfoDao = new TextInfoDao();

    public boolean appUnLocked = false;

    private long pauseTime = 0;
    private long resumeTime = 0;

    public static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mBtnAdd;
    private List<TextInfo> mTextList;
    private MyAdapter mMyAdapter;

    public static final int MODE_LINEAR = 0;
    public static final int MODE_GRID = 1;
    public static final String KEY_LAYOUT_MODE = "key_layout_mode";
    private int currentListLayoutMode = MODE_LINEAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDataFromDb();
        setListLayout();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void refreshDataFromDb() {
        mTextList = getDataFromDb();
        mMyAdapter.refreshData(mTextList);
    }

    private List<TextInfo> getDataFromDb() {
        return textInfoDao.findAll();
    }

    private void initEvent() {
        mMyAdapter = new MyAdapter(this, mTextList);
        mRecyclerView.setAdapter(mMyAdapter);
        setListLayout();
    }

    private void setListLayout() {
        currentListLayoutMode = SpfUtils.getIntWithDefault(this, KEY_LAYOUT_MODE, MODE_LINEAR);
        if (currentListLayoutMode == MODE_LINEAR) {
            setToLinearList();
        } else {
            setToGridList();
        }
    }

    private void setToGridList() {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMyAdapter.setViewType(MyAdapter.TYPE_GRID_LAYOUT);
        mMyAdapter.notifyDataSetChanged();
    }

    private void setToLinearList() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mMyAdapter.setViewType(MyAdapter.TYPE_LINEAR_LAYOUT);
        mMyAdapter.notifyDataSetChanged();
    }

    private void initData() {
        SpfUtils.saveUnLockData(this);
        mTextList = new ArrayList<>();
//        mTextList = LitePal.findAll(Text.class);
//        for (Text text : mTextList) {
//            Log.i(TAG, text.toString());
//        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.text_list_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_set_image:
                        ActivityUtils.simpleIntent(MainActivity.this, ChangeAppImage.class);
                        break;
                    case R.id.nav_set_password:
                        ActivityUtils.simpleIntent(MainActivity.this, ChangeAppPassword.class);
                        break;
                    case R.id.nav_set_leave_time:
                        setConfigValuePopup(
                                "离开应用锁定应用的时长",
                                "此设置为当您离开应用多少秒后应用锁定的时长",
                                "当前为 " + SpfUtils.getInt(getApplication(), ConstantValue.LEAVE_TIME) + " 秒",
                                ConstantValue.LEAVE_TIME
                        );
                        break;
                    case R.id.nav_set_error_lock_time:
                        setConfigValuePopup(
                                "错误过多应用锁定的时长",
                                "此设置为当您输入错误密码超过最大次数后锁定应用的时长",
                                "当前为 " + SpfUtils.getInt(getApplication(), ConstantValue.ERROR_LOCK_TIME) + " 分钟",
                                ConstantValue.ERROR_LOCK_TIME
                        );
                        break;
                    case R.id.nav_set_error_max_lock_number:
                        setConfigValuePopup(
                                "密码错误输入次数",
                                "此设置为您的应用的手势密码和传统密码的最大输错次数",
                                "当前次数为 " + SpfUtils.getInt(getApplication(), ConstantValue.ERROR_MAX_LOCK_NUMBER) + " 次",
                                ConstantValue.ERROR_MAX_LOCK_NUMBER
                        );
                        break;
                    case R.id.nav_set_image_lock_expiry_time:
                        setConfigValuePopup(
                                "手势密码提示修改密码时长",
                                "此设置为多少天后提示您修改您的手势密码，以保证应用安全。\n弹窗提示时间 = 上次密码修改时间 + 您设置的天数",
                                "当前设置为 " + SpfUtils.getInt(getApplication(), ConstantValue.IMAGE_LOCK_EXPIRY_TIME) + " 天",
                                ConstantValue.IMAGE_LOCK_EXPIRY_TIME
                        );
                        break;
                    case R.id.nav_set_password_expiry_time:
                        setConfigValuePopup(
                                "传统输入密码提示修改密码时长",
                                "此设置为多少天后提示您修改您的传统输入密码，以保证应用安全。\n弹窗提示时间 = 上次密码修改时间 + 您设置的天数",
                                "当前设置为 " + SpfUtils.getInt(getApplication(), ConstantValue.PASSWORD_EXPIRY_TIME) + " 天",
                                ConstantValue.PASSWORD_EXPIRY_TIME
                        );
                        break;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }

        });
    }

    private void setConfigValuePopup(String title, String desc, String hint, String key) {
        new XPopup.Builder(this)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(new CustomCenterPopup(
                        this,
                        title,
                        desc,
                        hint,
                        (etContent) -> {
                            String tempStr = etContent;
                            int tempInt;
                            if (TextUtils.isEmpty(tempStr)) {
                                tempStr = "0";
                            }
                            tempInt = Integer.parseInt(tempStr);
                            if (tempInt == 0) {
                                ToastUtils.toastShort(this, "不能为 0 或空");
                                return false;
                            } else {
                                SpfUtils.saveInt(this, key, tempInt);
                                ToastUtils.toastShort(this, "设置成功");
                                return true;
                            }
                        },
                        () -> null)).show();

    }

    public void toPageAddText(View view) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mTextList = textInfoDao.findLikeTitleKey(newText);
                mMyAdapter.refreshData(mTextList);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_linear:
                setToLinearList();
                currentListLayoutMode = MODE_LINEAR;
                SpfUtils.saveInt(this, KEY_LAYOUT_MODE, MODE_LINEAR);
                return true;
            case R.id.menu_grid:
                setToGridList();
                currentListLayoutMode = MODE_GRID;
                SpfUtils.saveInt(this, KEY_LAYOUT_MODE, MODE_GRID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentListLayoutMode == MODE_LINEAR) {
            MenuItem item = menu.findItem(R.id.menu_linear);
            item.setChecked(true);
        } else {
            menu.findItem(R.id.menu_grid).setChecked(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}