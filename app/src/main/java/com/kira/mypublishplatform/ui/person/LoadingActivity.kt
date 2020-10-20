package com.kira.mypublishplatform.ui.person

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.api.HttpWrapper
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.bean.ResponseBean
import com.kira.mypublishplatform.utils.ConstUtils
import com.kira.mypublishplatform.utils.Logger
import com.kira.mypublishplatform.utils.ToastUtils
import com.kira.mypublishplatform.view.TitleDialog
import com.rxjava.rxlife.life
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_loading.*


class LoadingActivity : AppCompatActivity() {

    private val mContext: Context = this@LoadingActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val ss = SpannableString("其实我是一个好人")
        val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#FF0000"))
        ss.setSpan(foregroundColorSpan, 2, 6, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        val underlineSpan = UnderlineSpan()
        ss.setSpan(underlineSpan, ss.length - 2, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val clickableSpan = MyClickableSpan(mContext)
        ss.setSpan(clickableSpan, 4, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        text.movementMethod = LinkMovementMethod.getInstance()
        text.highlightColor = Color.parseColor("#36969696")
        text.text = ss

        getKey()

    }

    private fun getKey() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            HttpWrapper.getKey()
                .observeOn(AndroidSchedulers.mainThread())
                .life(this)
                .subscribe(
                    { body ->
//                        LoadingBar.dialog(mContext).cancel()
                        if (body.isStatus) {

                            LoadingBar.dialog(mContext).cancel()
                            val key: String = body.data.toString()
                            Logger.d(key)
                            MyApplication.mSp.putStr(ConstUtils.PUBULIC_KEY, key)

                        } else {
                            ToastUtils.showLong(
                                mContext,
                                "获取公钥失败," + body.msg
                            )
                        }
                    },
                    { throwable: Throwable ->
//                        LoadingBar.dialog(mContext).cancel()
                        Logger.e("==onError==" + throwable.message)
                        ToastUtils.showLong(
                            mContext,
                            "获取公钥失败," + throwable.message
                        )
                    })
        }

    }

    internal class MyClickableSpan(private val context: Context) :
        ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
            ds.color = Color.parseColor("#FF0000")
        }

        override fun onClick(widget: View) {
            val builder = TitleDialog.Builder(context)
            builder.setTitle(R.string.submit_order).setMessage("您de Key值：\n" + "0000")
                .setPositiveButton(
                    "确认"
                ) { dialog: DialogInterface, _: Int ->
                    ToastUtils.showShort(
                        context,
                        "我按了确认"
                    )
                    dialog.dismiss()
                }
                .setNegativeButton(
                    "取消"
                ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                .create().show()
        }

    }
}
