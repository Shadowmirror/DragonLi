package miao.kmirror.dragonli.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.litepal.LitePal;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.bean.LockType;
import miao.kmirror.dragonli.dao.TextContentDao;
import miao.kmirror.dragonli.dao.TextInfoDao;
import miao.kmirror.dragonli.entity.TextContent;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.fragment.EditNameDialogFragment;
import miao.kmirror.dragonli.singleActivity.SinglePasswordUnlockActivity;
import miao.kmirror.dragonli.utils.AESEncryptUtils;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.DateUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class EditActivity extends AppCompatActivity {

    public static final String TAG = "EditActivity";
    private TextInfo textInfo;
    private TextContentDao textContentDao = new TextContentDao();
    private TextInfoDao textInfoDao = new TextInfoDao();
    private EditText etTitle;
    private EditText etContent;
    private boolean isChange = false;
    private int skipOne = 1;
    private MenuItem lockTitle;
    private int unlockType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        unlockType = intent.getIntExtra("unlockType", 0);
        textInfo = (TextInfo) intent.getSerializableExtra("textInfo");

        if(textInfo.getLocked()){
            switch (textInfo.getLockType()){
                case LockType.PASSWORD_LOCK:{
                    finish();
                    ActivityUtils.simpleIntentWithTextInfo(this, SinglePasswordUnlockActivity.class, textInfo);
                    break;
                }
                default:
                    break;
            }
        }

        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);

        Button test = findViewById(R.id.bt_edit_test);
        test.setOnClickListener(v -> {
            selectSingleLockType();
        });

        /**
         * 文本改变监听
         * */
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(skipOne++ != 1 && count > 0){
                   isChange = true;
               }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        textInfo = textInfoDao.findById(textInfo.getId());
    }

    /**
     * 返回按钮的是否保存事件
     * */
    public void changeSave() {
        if (isChange) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditActivity.this);
            alertDialog.setTitle("警告：");
            alertDialog.setMessage("内容已更改是否保存？");
            alertDialog.setCancelable(true);
            alertDialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    save_text();
                }
            });
            alertDialog.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditActivity.this.finish();
                }
            });
            alertDialog.show();
        }
    }

    /**
     * 监听左上角返回事件
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            changeSave();
            if(!isChange){
                return super.onOptionsItemSelected(item);
            }
        }
        return true;
    }

    /**
     * 实体返回按钮的监听
     */
    @Override
    public void onBackPressed() {
        changeSave();
        if(!isChange){
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        MenuItem save = menu.findItem(R.id.menu_save);
        save.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                save_text();
                return true;
            }
        });
        //
        MenuItem lock = menu.findItem(R.id.menu_lock);
        lock.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(textInfo.getLocked()){
                    ToastUtils.toastShort(EditActivity.this, "数据已经上锁了哦");
                }else{
                    showEditDialog();
                }
                onPrepareOptionsMenu(menu);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }
    /**
     * 单条数据加密方式选择弹窗
     * */
    private void selectSingleLockType() {
//        Log.i(TAG, "selectSingleLockType: mmm");
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(R.layout.list_item_single_lock_type_layout);
//        builder.create().show();

    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = new EditNameDialogFragment(textInfo);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        textInfo = (TextInfo) intent.getSerializableExtra("textInfo");
        lockTitle = menu.findItem(R.id.menu_lock);

        if(textInfo.getLocked() || unlockType == LockType.TEMP_UNLOCK){
            lockTitle.setTitle("已上锁");
        }else{
            lockTitle.setTitle("未上锁");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void initData() {
        Intent intent = getIntent();
        textInfo = (TextInfo) intent.getSerializableExtra("textInfo");
        if (textInfo != null) {
            etTitle.setText(textInfo.getTitle());
            TextContent textContent = textContentDao.findById(textInfo.getId());
            String temp = AESEncryptUtils.decrypt(textContent.getContent(), AESEncryptUtils.TEST_PASS);
            etContent.setText(temp);
        }
    }

    /**
     * 保存文本
     * */
    public void save_text() {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(title)) {
            ToastUtils.toastShort(this, "标题不能为空！");
            return;
        }
        TextContent textContent = textContentDao.findById(textInfo.getId());
        textInfo.setTitle(title);
        textInfo.setUpdateDate(DateUtils.getCurrentTimeFormat());
        textContent.setContent(AESEncryptUtils.encrypt(content, AESEncryptUtils.TEST_PASS));
        // 开启事务操作
        try{
            LitePal.beginTransaction();
            int row1 = textInfoDao.update(textInfo);
            int row2 = textContentDao.update(textContent);
            if(row1 > 0 && row2 > 0){
                LitePal.setTransactionSuccessful();
                ToastUtils.toastShort(this, "修改成功！");
                this.finish();
            } else {
                ToastUtils.toastShort(this, "修改失败！");
            }
        } finally {
            LitePal.endTransaction();
        }

    }


}