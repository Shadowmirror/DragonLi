package miao.kmirror.dragonli.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.adapter.AppAdapter;
import miao.kmirror.dragonli.adapter.SkipItemClickListener;
import miao.kmirror.dragonli.dao.AppPackageDao;
import miao.kmirror.dragonli.dao.WebInfoDao;
import miao.kmirror.dragonli.entity.AppPackage;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.ToastUtils;
import miao.kmirror.dragonli.utils.ToolbarUtils;

public class TestActivity extends AppCompatActivity {

    public static final String TAG = "TextActivity";

    private ImageView loginFinger;
    private TextView imageOrPasswordLogin;
    private TextView loginImage;
    private TextView loginPassword;
    private BottomSheetDialog bottomSheetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ToolbarUtils.initToolBar(this, false, "测试页");
        initView();
    }

    void initView() {
        loginFinger = findViewById(R.id.login_finger);
        imageOrPasswordLogin = findViewById(R.id.image_or_password_login);
        imageOrPasswordLogin.setOnClickListener(v -> {
            showBottomSheetDialog();
        });
    }

    void showBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
        loginImage = view.findViewById(R.id.login_image);
        loginPassword = view.findViewById(R.id.login_password);
        loginImage.setOnClickListener(v -> {

        });
        loginPassword.setOnClickListener(v -> {

        });
        bottomSheetDialog.setContentView(view);

        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();
    }


}