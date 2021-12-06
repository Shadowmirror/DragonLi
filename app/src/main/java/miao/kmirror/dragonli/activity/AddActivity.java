package miao.kmirror.dragonli.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.bean.Text;
import miao.kmirror.dragonli.utils.DateUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class AddActivity extends AppCompatActivity {

    private EditText etTitle;

    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
    }

    public void add_text(View view) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if(TextUtils.isEmpty(title)){
            ToastUtils.toastShort(this, "标题不能为空");
            return;
        }

        Text text = new Text();

        text.setTitle(title);
        text.setContent(content);
        text.setCreatedTime(DateUtils.getCurrentTimeFormat());
        boolean isSave = text.save();
        if(isSave){
            ToastUtils.toastShort(this, "添加成功！");
            this.finish();
        }else{
            ToastUtils.toastShort(this, "添加失败！");
        }
    }

}