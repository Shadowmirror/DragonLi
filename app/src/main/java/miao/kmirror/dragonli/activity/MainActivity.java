package miao.kmirror.dragonli.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


import adapter.MyAdapter;
import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.bean.Text;
import miao.kmirror.dragonli.utils.SpfUtils;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;

    private FloatingActionButton mBtnAdd;

    private List<Text> mTextList;

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

    private void refreshDataFromDb() {
        mTextList = getDataFromDb();
        mMyAdapter.refreshData(mTextList);
    }

    private List<Text> getDataFromDb() {
        return LitePal.findAll(Text.class);
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
        mRecyclerView = findViewById(R.id.text_list_view);
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
                mTextList = LitePal.where("title like ?", "%" + newText + "%").find(Text.class);
                mMyAdapter.refreshData(mTextList);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
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
}