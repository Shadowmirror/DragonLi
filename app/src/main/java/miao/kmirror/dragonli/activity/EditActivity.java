package miao.kmirror.dragonli.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.litepal.LitePal;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.bean.LockType;
import miao.kmirror.dragonli.dao.TextContentDao;
import miao.kmirror.dragonli.dao.TextInfoDao;
import miao.kmirror.dragonli.entity.TextContent;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.fragment.SelectLockTypeFragment;
import miao.kmirror.dragonli.lock.activity.PasswordLoginActivity;
import miao.kmirror.dragonli.singleActivity.SingleFingerLockActivity;
import miao.kmirror.dragonli.singleActivity.SingleImageLockActivity;
import miao.kmirror.dragonli.singleActivity.SinglePasswordUnlockActivity;
import miao.kmirror.dragonli.utils.AESEncryptUtils;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.DateUtils;
import miao.kmirror.dragonli.utils.PasswordUtils;
import miao.kmirror.dragonli.utils.RangePasswordUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class EditActivity extends AppCompatActivity {

    public static final String TAG = "EditActivity";
    private TextInfo textInfo;
    private TextContentDao textContentDao = new TextContentDao();
    private TextInfoDao textInfoDao = new TextInfoDao();
    private EditText etTitle;
    private EditText etContent;
    /**
     * 显示随机密码的长度
     * */
    private TextView password_length;

    /**
     *  这三个分别代表时候含有数字、字母、符号
     *  1 表示含有
     *  2 表示不含有
     *  默认是仅数字
     * */
    private int haveNumber = 1;
    private int haveLetter = 0;
    private int haveSymbol = 0;



    private boolean isChange = false;
    private int skipOne = 1;
    private MenuItem lockTitle;
    private int tempUnlock;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        tempUnlock = intent.getIntExtra("tempUnlock", 0);
        textInfo = (TextInfo) intent.getSerializableExtra("textInfo");

        if (textInfo.getLocked() && tempUnlock == LockType.NON_TEMP_UNLOCK) {
            switch (textInfo.getLockType()) {
                case LockType.PASSWORD_LOCK:
                    finish();
                    ActivityUtils.simpleIntentWithTextInfo(this, SinglePasswordUnlockActivity.class, textInfo);
                    break;
                case LockType.FINGER_LOCK:
                    finish();
                    ActivityUtils.simpleIntentWithTextInfo(this, SingleFingerLockActivity.class, textInfo);
                case LockType.IMAGE_LOCK:
                    finish();
                    ActivityUtils.simpleIntentWithTextInfo(this, SingleImageLockActivity.class, textInfo);
                default:
                    break;
            }
        }

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
                if (skipOne++ != 1 && count > 0) {
                    isChange = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        /**
         * 随机密码功能
         * */
        SeekBar seekBar = findViewById(R.id.range_password);
        password_length = findViewById(R.id.password_length);
        LinearLayout rangeView = findViewById(R.id.range_view);
        rangeView.setVisibility(View.GONE);

        Switch ableRange = findViewById(R.id.able_range);
        ableRange.setChecked(false);
        ableRange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    rangeView.setVisibility(View.GONE);
                }else{
                    rangeView.setVisibility(View.VISIBLE);
                }
            }
        });
        Switch swEnableNumber = findViewById(R.id.sw_enable_number);
        Switch swEnableLetter = findViewById(R.id.sw_enable_letter);
        Switch swEnableSymbol = findViewById(R.id.sw_enable_symbol);
        swEnableNumber.setChecked(true);
        swEnableNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    haveNumber = 1;
                }else{
                    haveNumber = 0;
                }
            }
        });
        swEnableLetter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    haveLetter = 1;
                }else{
                    haveLetter = 0;
                }
            }
        });
        swEnableSymbol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    haveSymbol = 1;
                }else{
                    haveSymbol = 0;
                }
            }
        });




        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 拖动条进度改变时调用
                String rangeTypeStr = "" + haveSymbol + haveLetter + haveNumber;
                int rangeType = Integer.parseInt(rangeTypeStr, 2);
                if(rangeType == 0){
                    ToastUtils.toastShort(EditActivity.this, "至少选择一种方式生成随机密码");
                }else{
                    password_length.setText("" + progress);
                    etContent.setText(RangePasswordUtils.rangePassword(rangeType, progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 拖动条开始时调用
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 拖动条结束时调用
            }
        });


        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        textInfo = textInfoDao.findById(textInfo.getId());
        if (tempUnlock == LockType.TEMP_UNLOCK && textInfo.getLocked() == false) {
            lockTitle.setTitle("未上锁");
        }
    }

    /**
     * 返回按钮的是否保存事件
     */
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
            if (!isChange) {
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
        if (!isChange) {
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
                Log.i(TAG, "onMenuItemClick: getLocked =  " + textInfo.getLocked());
                Log.i(TAG, "onMenuItemClick: getLockType =  " + textInfo.getLockType());
                if (textInfo.getLocked()) {
                    switch (textInfo.getLockType()) {
                        case LockType.PASSWORD_LOCK:
                            ActivityUtils.simpleIntentWithTextInfo(EditActivity.this,
                                    PasswordLoginActivity.class,
                                    textInfo);
                            break;
                        case LockType.FINGER_LOCK:
                            ActivityUtils.simpleIntentWithTextInfo(EditActivity.this,
                                    SingleFingerLockActivity.class,
                                    textInfo,
                                    "deleteLock",
                                    true);
                            break;
                        case LockType.IMAGE_LOCK:
                            ActivityUtils.simpleIntentWithTextInfo(EditActivity.this,
                                    SingleImageLockActivity.class,
                                    textInfo,
                                    "deleteLock",
                                    true);
                            break;
                        default:
                            break;
                    }
                } else {
                    showEditDialog();
                }
                onPrepareOptionsMenu(menu);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    /**
     * 选择加密方式
     */
    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SelectLockTypeFragment selectLockTypeFragment = new SelectLockTypeFragment(textInfo);
        selectLockTypeFragment.show(fm, "fragment_select_lock_type");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        textInfo = (TextInfo) intent.getSerializableExtra("textInfo");
        textInfo = textInfoDao.findById(textInfo.getId());
        lockTitle = menu.findItem(R.id.menu_lock);
        if (textInfo.getLocked()) {
            lockTitle.setTitle("已上锁");
        } else {
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
     */
    public void save_text() {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(title)) {
            ToastUtils.toastShort(this, "标题不能为空！");
            return;
        }
        TextContent textContent = textContentDao.findById(textInfo.getId());
        textInfo = textInfoDao.findById(textInfo.getId());
        textInfo.setTitle(title);
        textInfo.setUpdateDate(DateUtils.getCurrentTimeFormat());
        textContent.setContent(AESEncryptUtils.encrypt(content, AESEncryptUtils.TEST_PASS));
        // 开启事务操作
        try {
            LitePal.beginTransaction();
            int row1 = textInfoDao.update(textInfo);
            int row2 = textContentDao.update(textContent);
            if (row1 > 0 && row2 > 0) {
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