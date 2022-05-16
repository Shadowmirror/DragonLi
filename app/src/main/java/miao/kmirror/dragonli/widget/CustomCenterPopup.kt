package miao.kmirror.dragonli.widget

import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.lxj.xpopup.core.CenterPopupView
import miao.kmirror.dragonli.R
import miao.kmirror.dragonli.utils.clickWithLimit

/**
 * @author Kmirror
 */
class CustomCenterPopup(context: Context, val title: String = "标题", val desc: String = "描述", val hint: String = "提示", val confirmListener: (etContent: String) -> Boolean, val cancelListener: () -> Unit) : CenterPopupView(context) {

    private val tvTitle: TextView by lazy {
        findViewById(R.id.tv_title)
    }

    private val tvDesc: TextView by lazy {
        findViewById(R.id.tv_desc)
    }

    private val etContent: EditText by lazy {
        findViewById(R.id.et_content)
    }
    private val btnConfirm: Button by lazy {
        findViewById(R.id.btn_confirm)
    }
    private val btnCancel: Button by lazy {
        findViewById(R.id.btn_cancel)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.layout_set_leave_time
    }

    override fun onCreate() {
        super.onCreate()
        tvTitle.text = title
        etContent.hint = hint
        tvDesc.text = desc
        btnCancel.clickWithLimit {
            cancelListener()
            dismiss()
        }
        btnConfirm.clickWithLimit {
            if (confirmListener(etContent.text.toString())) {
                dismiss()
            }
        }
    }
}
