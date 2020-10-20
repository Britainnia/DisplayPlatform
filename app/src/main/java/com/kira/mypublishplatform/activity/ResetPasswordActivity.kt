package com.kira.mypublishplatform.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.google.gson.Gson
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.bean.ResponseBean
import com.kira.mypublishplatform.databinding.ActivityResetPasswordBinding
import com.kira.mypublishplatform.utils.FastClickUtils.Companion.isValidClick
import com.kira.mypublishplatform.utils.Logger
import com.kira.mypublishplatform.utils.other.CountDownTimerUtils
import com.kira.mypublishplatform.utils.other.EditHelper
import com.kira.mypublishplatform.utils.other.EditHelper.EditHelperData
import com.kira.mypublishplatform.utils.other.Validators
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
//import kotlinx.android.synthetic.main.activity_reset_password.*
import okhttp3.ResponseBody
import java.io.IOException
import java.util.*

class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding>() {

    private val editHelper = EditHelper(this)
//    private val mContext: Context = this@ResetPasswordActivity
//    override fun loadXml() {
//        setContentView(R.layout.activity_reset_password)
//    }

    override fun getIntentData(savedInstanceState: Bundle?) {}


    override fun initData() {
        editHelper.addEditHelperData(
            EditHelperData(
                binding.phoneNumber,
                Validators.getLenghMinRegex(1),
                "请输入手机号码"
            )
        )
        editHelper.addEditHelperData(
            EditHelperData(
                binding.code,
                Validators.getLenghMinRegex(1),
                "请输入验证码"
            )
        )
        editHelper.addEditHelperData(
            EditHelperData(
                binding.password,
                Validators.getLenghMinRegex(1),
                "请输入密码"
            )
        )
        editHelper.addEditHelperData(
            EditHelperData(
                binding.password2,
                Validators.getLenghMinRegex(1),
                "请再次确认密码"
            )
        )
    }

    override fun setListener() {
        binding.verify.setOnClickListener {
            if (editHelper.check(R.id.phoneNumber)) {
                if (Validators.isPhoneNumber(binding.phoneNumber.text.toString())) { //发送验证码
                    getMsg()
                    // 倒计时
                    val mCountDownTimerUtils =
                        CountDownTimerUtils(binding.verify, 60000, 1000)
                    mCountDownTimerUtils.start()
                } else {
                    showLongToast("您输入的手机号码有误")
                }
            }
        }
        binding.submit.setOnClickListener { view: View ->
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            Objects.requireNonNull(imm)
                .hideSoftInputFromWindow(view.windowToken, 0) //强制隐藏键盘
            if (editHelper.check(R.id.phoneNumber) && editHelper.check(R.id.code)
                && editHelper.check(R.id.password) && editHelper.check(R.id.password2)
                && !isValidClick) {
                if (Validators.isPhoneNumber(binding.phoneNumber.text.toString())) {
                    if (binding.password.text.toString() == binding.password2.text.toString()) { //提交
//                        clientResetPass()
                        showLongToast("重置密码成功！")
                        finish()
                    } else {
                        showLongToast("两次密码输入不一致")
                    }
                } else {
                    showLongToast("您输入的手机号码有误")
                }
            }
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    /**
     * 重置
     */
    private fun clientResetPass() {
        LoadingBar.dialog(baseContext).extras(arrayOf<Any>("正在重置密码..")).show()
        val account = binding.phoneNumber.text.toString()
        val pass = binding.password.text.toString()
        val code = binding.code.text.toString()
        MyApplication.api.clientResetPass(account, pass, code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    LoadingBar.dialog(baseContext).cancel()
                    Logger.e("==onError==" + e.message)
                    showLongToast("重置失败," + e.message)
                }

                override fun onNext(body: ResponseBody) {
                    LoadingBar.dialog(baseContext).cancel()
                    try {
                        val json = body.string()
                        val rawBean =
                            Gson().fromJson(json, ResponseBean::class.java)
                        if (rawBean.isStatus) {
                            showLongToast("重置密码成功！")
                            finish()
                            //ResponseBean<OrderDataBean> bean = new Gson().fromJson(json, new TypeToken<ResponseBean<OrderDataBean>>() {}.getType());
                        } else {
                            showLongToast("重置失败," + rawBean.msg)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
    }

    /**
     * 获取验证码
     */
    private fun getMsg() {
            MyApplication.api.sendResetMsg(binding.phoneNumber.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onComplete() {}
                    override fun onError(e: Throwable) { //                        LoadingDialog.cancel();
                        Logger.e("==onError==" + e.message)
                        showLongToast("获取验证码失败," + e.message)
                    }

                    override fun onNext(body: ResponseBody) { //                        LoadingDialog.cancel();
                        try {
                            val json = body.string()
                            val rawBean =
                                Gson().fromJson(json, ResponseBean::class.java)
                            if (rawBean.isStatus) { //                                ResponseBean<String> jsonBean = new Gson().fromJson(json, new TypeToken<ResponseBean<String>>() {}.getType());
//                                String mess = jsonBean.getData();
//
//                                MessageBean bean = new Gson().fromJson(mess, MessageBean.class);
//                                MyApplication.mSp.putStr(ConstUtils.REGISTER_MSG, bean.getMsg_id());
                                showLongToast("获取验证码成功！")
                            } else {
                                showLongToast("获取验证码失败," + rawBean.msg)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                })
        }
}