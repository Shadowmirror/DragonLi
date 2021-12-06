package miao.kmirror.dragonli.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.bean.Text;
import miao.kmirror.dragonli.utils.DateUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class EditActivity extends AppCompatActivity {

    private Text text;
    private EditText etTitle;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        text = (Text) intent.getSerializableExtra("text");
        if(text != null){
            etTitle.setText(text.getTitle());
            etContent.setText(text.getContent());
        }
    }

    public void save_text(View view) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if(TextUtils.isEmpty(title)){
            ToastUtils.toastShort(this, "标题不能为空！");
            return;
        }

        text.setTitle(title);
        text.setContent(content);
        text.setCreatedTime(DateUtils.getCurrentTimeFormat());
        int row = text.updateAll("id = ?", text.getId().toString());
        if(row != -1){
            ToastUtils.toastShort(this, "修改成功！");
            this.finish();
        }else{
            ToastUtils.toastShort(this, "修改失败！");
        }
    }
}