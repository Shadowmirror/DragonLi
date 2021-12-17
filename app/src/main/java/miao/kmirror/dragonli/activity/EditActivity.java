package miao.kmirror.dragonli.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.adapter.AppAdapter;
import miao.kmirror.dragonli.adapter.SkipItemClickListener;
import miao.kmirror.dragonli.adapter.WebAdapter;
import miao.kmirror.dragonli.bean.LockType;
import miao.kmirror.dragonli.dao.AppPackageDao;
import miao.kmirror.dragonli.dao.TextContentDao;
import miao.kmirror.dragonli.dao.TextInfoDao;
import miao.kmirror.dragonli.dao.TextSkipDao;
import miao.kmirror.dragonli.dao.WebInfoDao;
import miao.kmirror.dragonli.entity.AppPackage;
import miao.kmirror.dragonli.entity.TextContent;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.entity.TextSkip;
import miao.kmirror.dragonli.entity.WebInfo;
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
import miao.kmirror.dragonli.utils.ToolbarUtils;

public class EditActivity extends AppCompatActivity implements SkipItemClickListener {

    public static final String TAG = "EditActivity";
    private TextInfo textInfo;

    private EditText etTitle;
    private EditText etContent;

    private boolean isChange = false;
    private int skipOne = 1;
    private MenuItem lockTitle;
    private int tempUnlock;

    /**
     * 数据库操作
     */
    private TextContentDao textContentDao = new TextContentDao();
    private TextInfoDao textInfoDao = new TextInfoDao();
    private AppPackageDao appPackageDao = new AppPackageDao();
    private WebInfoDao webInfoDao = new WebInfoDao();
    private TextSkipDao textSkipDao = new TextSkipDao();

    /**
     * 随机密码功能
     * */
    /**
     * 这三个分别代表时候含有数字、字母、符号
     * 1 表示含有
     * 2 表示不含有
     * 默认是仅数字
     */
    private Switch ableRange;
    private int haveNumber = 1;
    private int haveLetter = 0;
    private int haveSymbol = 0;
    Switch swEnableNumber;
    Switch swEnableLetter;
    Switch swEnableSymbol;
    SeekBar seekBar;
    /**
     * 显示随机密码的长度
     */
    private TextView password_length;

    /**
     * App 和 Web 页跳转
     */
    private AppPackage currentApp;
    private WebInfo currentWeb;
    private int appOrWeb = 0;
    private Button copyAndSkip;
    private TextView tvAppOrWebName;
    private LinearLayout skipList;
    private ImageView imagePopup;
    private List<AppPackage> appPackages;
    private List<WebInfo> webInfoList;
    private WebAdapter mWebAdapter;
    private AppAdapter mAppAdapter;
    private RecyclerView mRecyclerView;
    public PopupWindow popupWindow;
    private Spinner spSelect;
    private TextView tvAddAppOrWeb;
    private TextView dialogTitle;
    private EditText etSkipName;
    private EditText etSkipLink;
    private TextSkip textSkip;

    /**
     * 用于删除网站和应用条目的临时变量
     */
    private WebInfo tempWebInfo;
    private AppPackage tempAppPackage;
    private int appOrWebId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ToolbarUtils.initToolBar(this);

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
                    break;
                case LockType.IMAGE_LOCK:
                    finish();
                    ActivityUtils.simpleIntentWithTextInfo(this, SingleImageLockActivity.class, textInfo);
                    break;
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
         * 随机密码功能开始
         * */
        password_length = findViewById(R.id.password_length);
        LinearLayout rangeView = findViewById(R.id.range_view);
        rangeView.setVisibility(View.GONE);
        ableRange = findViewById(R.id.able_range);
        ableRange.setChecked(false);
        ableRange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    rangeView.setVisibility(View.GONE);
                } else {
                    rangeView.setVisibility(View.VISIBLE);
                    initRange();
                }
            }
        });

        initAppOrWebSkipData();

        /**
         * 应用或网站跳转功能
         * */
        initAppOrWebSkip();

        /**
         * 数据加载
         * */
        initData();
    }

    private void initAppOrWebSkipData() {
        appPackages = new ArrayList<>();
        appPackages = appPackageDao.findAll();
        webInfoList = new ArrayList<>();
        webInfoList = webInfoDao.findAll();

        textSkip = textSkipDao.findByTextId(textInfo.getId());
        Log.i(TAG, "initData: textSkip = " + textSkip);
        appOrWeb = textSkip.getAppOrWeb();
        appOrWebId = textSkip.getAppOrWebId();
        if (textSkip.getAppOrWeb() == 0) {
            currentApp = appPackageDao.findById(appOrWebId);
            currentWeb = webInfoList.get(0);
        } else if (textSkip.getAppOrWeb() == 1) {
            currentWeb = webInfoDao.findById(appOrWebId);
            currentApp = appPackages.get(0);
        }

        Log.i(TAG, "initData: currentApp = " + currentApp);


    }

    /**
     * 应用或网站跳转功能
     */
    public void initAppOrWebSkip() {
        spSelect = findViewById(R.id.sp_select);
        skipList = findViewById(R.id.skip_list);
        tvAppOrWebName = findViewById(R.id.tv_app_or_web_name);
        imagePopup = findViewById(R.id.image_popup);
        copyAndSkip = findViewById(R.id.copy_and_skip);

        spSelect.setSelection(appOrWeb);

        spSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                appOrWeb = position;
                if (appOrWeb == 0) {
                    tvAppOrWebName.setText(currentApp.getAppName());
                } else if (appOrWeb == 1) {
                    tvAppOrWebName.setText(currentWeb.getWebName());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        skipList.setOnClickListener(v -> {
            showPopupWindow();
        });
        copyAndSkip.setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData label = ClipData.newPlainText("Label", etContent.getText());
            cm.setPrimaryClip(label);
            if (appOrWeb == 0) {
                ActivityUtils.goApp(this, currentApp);
            } else if (appOrWeb == 1) {
                ActivityUtils.goWeb(this, currentWeb);
            }
        });
    }

    /**
     * 列表弹窗功能
     */
    private void showPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow, null);
        tvAddAppOrWeb = view.findViewById(R.id.add_app_or_web);
        if (appOrWeb == 0) {
            tvAddAppOrWeb.setText("+ 增加应用");
            tvAddAppOrWeb.setOnClickListener(v -> {
                Dialog addDialog = createAddDialog();
                addDialog.show();
            });
        } else if (appOrWeb == 1) {
            tvAddAppOrWeb.setText("+ 增加网站");
            tvAddAppOrWeb.setOnClickListener(v -> {
                Dialog addDialog = createAddDialog();
                addDialog.show();
            });
        }
        mRecyclerView = view.findViewById(R.id.skip_recyclerView);
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.setFocusable(true);
        initPopupData();
        popupWindow.showAsDropDown(skipList);
    }

    /**
     * 添加网站或应用的弹窗
     */
    public Dialog createAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_app_or_web, null);
        dialogTitle = view.findViewById(R.id.dialog_title);
        etSkipName = view.findViewById(R.id.et_skip_name);
        etSkipLink = view.findViewById(R.id.et_skip_link);
        if (appOrWeb == 0) {
            dialogTitle.setText("增加应用");
            etSkipName.setHint("应用名称：DragonLi");
            etSkipLink.setHint("应用包名：miao.kmirror.dragonli");
        } else if (appOrWeb == 1) {
            dialogTitle.setText("增加网站");
            etSkipName.setHint("网站名称：Github");
            etSkipLink.setHint("网站网址：www.github.com");
        }
        builder.setView(view)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String skipName = etSkipName.getText().toString();
                        String skipLink = etSkipLink.getText().toString();
                        if (TextUtils.isEmpty(skipName) || TextUtils.isEmpty(skipLink)) {
                            ToastUtils.toastShort(EditActivity.this, "输入栏不能为空");
                        } else {
                            if (appOrWeb == 0) {
                                AppPackage appPackage = new AppPackage();
                                appPackage.setAppName(skipName);
                                appPackage.setAppPackageName(skipLink);

                                if (checkExist(skipName, skipLink)) {
                                    ToastUtils.toastShort(EditActivity.this, "该应用已存在");
                                } else {
                                    appPackageDao.save(appPackage);
                                    appPackages = appPackageDao.findAll();
                                    mAppAdapter.refreshData(appPackages);
                                }
                            } else if (appOrWeb == 1) {
                                WebInfo webInfo = new WebInfo();
                                webInfo.setWebName(skipName);
                                webInfo.setWebLink(skipLink);
                                if (checkExist(skipName, skipLink)) {
                                    ToastUtils.toastShort(EditActivity.this, "该网站已存在");
                                } else {
                                    webInfoDao.save(webInfo);
                                    webInfoList = webInfoDao.findAll();
                                    mWebAdapter.refreshData(webInfoList);
                                }
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        return builder.create();
    }

    private boolean checkExist(String skipName, String skipLink) {
        int len = 0;
        if (appOrWeb == 0) {
            List<AppPackage> appPackages = LitePal.where("appName = ? and appPackageName = ?", skipName, skipLink).find(AppPackage.class);
            len = appPackages.size();
        } else if (appOrWeb == 1) {
            List<WebInfo> webInfoList = LitePal.where("webName = ? and webLink = ?", skipName, skipLink).find(WebInfo.class);
            len = webInfoList.size();
        }
        return len > 0 ? true : false;
    }

    /**
     * 加载 PopupWindow 需要的数据
     */
    private void initPopupData() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        if (appOrWeb == 0) {
            mAppAdapter = new AppAdapter(this, appPackages);
            mAppAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mAppAdapter);
        } else if (appOrWeb == 1) {
            mWebAdapter = new WebAdapter(this, webInfoList);
            mWebAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mWebAdapter);
        }
    }


    public void initRange() {

        swEnableNumber = findViewById(R.id.sw_enable_number);
        swEnableLetter = findViewById(R.id.sw_enable_letter);
        swEnableSymbol = findViewById(R.id.sw_enable_symbol);
        seekBar = findViewById(R.id.range_password);
        swEnableNumber.setChecked(true);
        swEnableNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    haveNumber = 1;
                } else {
                    haveNumber = 0;
                }
            }
        });
        swEnableLetter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    haveLetter = 1;
                } else {
                    haveLetter = 0;
                }
            }
        });
        swEnableSymbol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    haveSymbol = 1;
                } else {
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
                if (rangeType == 0) {
                    ToastUtils.toastShort(EditActivity.this, "至少选择一种方式生成随机密码");
                } else {
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
                    saveText();
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

    /**
     * 菜单栏
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        MenuItem save = menu.findItem(R.id.menu_save);
        save.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                saveText();
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
        if (appOrWeb == 0) {
            tvAppOrWebName.setText(currentApp.getAppName());
        } else if (appOrWeb == 1) {
            tvAppOrWebName.setText(currentWeb.getWebName());
        }
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
    public void saveText() {
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
            int row3 = textSkipDao.updateById(textSkip.getId(), appOrWeb, appOrWebId);
            if (row1 > 0 && row2 > 0 && row3 > 0) {
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

    @Override
    public void onItemClick(View v, int position) {
        if (appOrWeb == 0) {
            AppPackage appPackage = appPackages.get(position);
            currentApp = appPackage;
            appOrWebId = currentApp.getId();
            tvAppOrWebName.setText(appPackage.getAppName());
        } else if (appOrWeb == 1) {
            WebInfo webInfo = webInfoList.get(position);
            currentWeb = webInfo;
            appOrWebId = currentWeb.getId();
            tvAppOrWebName.setText(webInfo.getWebName());
        }
        popupWindow.dismiss();
    }

    /**
     * 应用或网站条目删除
     */
    @Override
    public void onItemLongClick(View v, int position) {
        if (appOrWeb == 0) {
            tempAppPackage = appPackages.get(position);
        } else if (appOrWeb == 1) {
            tempWebInfo = webInfoList.get(position);
        }
        // 弹出弹窗
        Dialog dialog = new Dialog(this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        View dialogView = LayoutInflater.from(EditActivity.this).inflate(R.layout.list_item_dialog_layout, null);
        TextView tvDelete = dialogView.findViewById(R.id.tv_delete);
        TextView tvEdit = dialogView.findViewById(R.id.tv_edit);
        tvEdit.setText("取消");
        tvDelete.setOnClickListener(v1 -> {
            try {
                LitePal.beginTransaction();
                int row = 0;
                if (appOrWeb == 0) {
                    if (tempAppPackage.getId() != 1) {
                        if (currentApp.getAppName()
                                == tempAppPackage.getAppName()
                                && currentApp.getAppPackageName()
                                == tempAppPackage.getAppPackageName()) {
                            currentApp = appPackages.get(0);
                            tvAppOrWebName.setText(currentApp.getAppName());
                        }
                        row = appPackageDao.delete(tempAppPackage);
                        Log.i(TAG, "onItemLongClick: row = " + row);
                        textSkipDao.deleteByAppOrWebId(appOrWeb, tempAppPackage.getId());
                    } else {
                        ToastUtils.toastShort(this, "此数据为不可删除数据");
                    }

                } else if (appOrWeb == 1) {
                    if (tempWebInfo.getId() != 1) {
                        if (currentWeb.getWebName()
                                == tempWebInfo.getWebName()
                                && currentWeb.getWebLink()
                                == tempWebInfo.getWebLink()) {
                            currentApp = appPackages.get(0);
                            tvAppOrWebName.setText(currentWeb.getWebName());
                        }
                        row = webInfoDao.delete(tempWebInfo);
                        textSkipDao.deleteByAppOrWebId(appOrWeb, tempWebInfo.getId());
                    } else {
                        ToastUtils.toastShort(this, "此数据为不可删除数据");
                    }
                }
                if (row > 0) {
                    LitePal.setTransactionSuccessful();
                    if (appOrWeb == 0) {
                        appPackages = appPackageDao.findAll();
                        mAppAdapter.removeData(position);
                    } else if (appOrWeb == 1) {
                        webInfoList = webInfoDao.findAll();
                        mWebAdapter.removeData(position);
                    }
                    ToastUtils.toastShort(this, "删除成功");
                } else {
                    ToastUtils.toastShort(this, "删除失败！");
                }
                dialog.dismiss();
            } finally {
                LitePal.endTransaction();
                dialog.dismiss();
            }
        });
        tvEdit.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}