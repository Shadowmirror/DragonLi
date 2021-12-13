package miao.kmirror.dragonli.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


import miao.kmirror.dragonli.adapter.MyAdapter;
import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.dao.TextInfoDao;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.navFunction.ChangeAppImage;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.SpfUtils;
import miao.kmirror.dragonli.utils.ToastUtils;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

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
        if (pauseTime != 0) {
            resumeTime = System.currentTimeMillis();
            // 离开应用时长
            if ((resumeTime - pauseTime) / 1000 >= 60) {
                ToastUtils.toastShort(this, "离开本应用已 60 秒，本应用自动锁定");
                ActivityUtils.flagActivityClearTask(this, LoginActivity.class);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime = System.currentTimeMillis();
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
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_setImage:
                        ActivityUtils.simpleIntent(MainActivity.this, ChangeAppImage.class);
                        break;
                    case R.id.nav_setPassword:
                        ToastUtils.toastShort(MainActivity.this, "Click SetPassword!!");
                        break;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }

        });

    }

    public void to_page_add_text(View view) {
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
//                Log.i(TAG, "onQueryTextChange: newText = " + newText);
                mTextList = textInfoDao.findLikeTitleKey(newText);
                mMyAdapter.refreshData(mTextList);
                return true;
            }
        });
//        return super.onCreateOptionsMenu(menu);
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