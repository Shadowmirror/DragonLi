package miao.kmirror.dragonli.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.litepal.LitePal;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.dao.TextContentDao;
import miao.kmirror.dragonli.dao.TextInfoDao;
import miao.kmirror.dragonli.entity.TextContent;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.utils.AESEncryptUtils;
import miao.kmirror.dragonli.utils.DateUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class EditActivity extends AppCompatActivity {

    public static final String TAG = "EditActivity";
    private TextInfo text;
    private TextContentDao textContentDao = new TextContentDao();
    private TextInfoDao textInfoDao = new TextInfoDao();
    private EditText etTitle;
    private EditText etContent;
    private boolean isChange = false;
    private int skipOne = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        text = (TextInfo) intent.getSerializableExtra("text");

        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);

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
        MenuItem item = menu.findItem(R.id.menu_save);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                save_text();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    private void initData() {
        Intent intent = getIntent();
        text = (TextInfo) intent.getSerializableExtra("text");
        if (text != null) {
            etTitle.setText(text.getTitle());
            TextContent textContent = textContentDao.findById(text.getId());
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
        TextContent textContent = textContentDao.findById(text.getId());
        text.setTitle(title);
        text.setUpdateDate(DateUtils.getCurrentTimeFormat());
        textContent.setContent(AESEncryptUtils.encrypt(content, AESEncryptUtils.TEST_PASS));
        // 开启事务操作
        try{
            LitePal.beginTransaction();
            int row1 = textInfoDao.update(text);
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


//        text.setTitle(title);
//        String temp = AESEncryptUtils.encrypt(content, AESEncryptUtils.TEST_PASS);
//        // 加密
//        text.setContent(temp);
//        text.setCreatedTime(DateUtils.getCurrentTimeFormat());
//        int row = text.updateAll("id = ?", text.getId().toString());
//        if (row != -1) {
//            ToastUtils.toastShort(this, "修改成功！");
//            this.finish();
//        } else {
//            ToastUtils.toastShort(this, "修改失败！");
//        }
    }
}