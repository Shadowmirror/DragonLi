package miao.kmirror.dragonli.widget

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.lxj.xpopup.core.CenterPopupView
import miao.kmirror.dragonli.R
import miao.kmirror.dragonli.constant.ConstantValue
import miao.kmirror.dragonli.navFunction.ChangeAppImage
import miao.kmirror.dragonli.navFunction.ChangeAppPassword
import miao.kmirror.dragonli.utils.SpfUtils
import miao.kmirror.dragonli.utils.clickWithLimit

/**
 * @author Kmirror
 */
class PasswordExpiryPopup(context: Context, val expiryType: Int) : CenterPopupView(context) {

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
        etContent.visibility = View.GONE
        btnConfirm.text = "去修改"
        btnCancel.clickWithLimit {
            dismiss()
        }
        when (expiryType) {
            1 -> {
                tvTitle.text = "手势密码过期"
                tvDesc.text = "应用手势密码已超过 ${SpfUtils.getInt(context, ConstantValue.IMAGE_LOCK_EXPIRY_TIME)} 天没有更改，为了您的应用安全，请及时更改"
                btnConfirm.clickWithLimit {
                    ChangeAppImage.startActivity(context)
                    dismiss()
                }
            }
            2 -> {
                tvTitle.text = "输入密码过期"
                tvDesc.text = "应用输入型密码已超过 ${SpfUtils.getInt(context, ConstantValue.PASSWORD_EXPIRY_TIME)} 天没有更改，为了您的应用安全，请及时更改"
                btnConfirm.clickWithLimit {
                    ChangeAppPassword.startActivity(context, false)
                    dismiss()
                }
            }
            3 -> {
                tvTitle.text = "手势/输入密码过期"
                tvDesc.text = "应用手势密码已超过 ${SpfUtils.getInt(context, ConstantValue.IMAGE_LOCK_EXPIRY_TIME)} 天没有更改，为了您的应用安全，请及时更改" +
                        "\n应用输入型密码已超过 ${SpfUtils.getInt(context, ConstantValue.PASSWORD_EXPIRY_TIME)} 天没有更改，为了您的应用安全，请及时更改"
                btnConfirm.clickWithLimit {
                    ChangeAppPassword.startActivity(context, true)
                    dismiss()
                }

            }
        }
    }
}
