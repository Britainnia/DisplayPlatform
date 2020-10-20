package com.kira.mypublishplatform.activity

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.google.gson.Gson
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.bean.ResponseBean
import com.kira.mypublishplatform.databinding.ActivityRegisterBinding
import com.kira.mypublishplatform.utils.FastClickUtils.Companion.isValidClick
import com.kira.mypublishplatform.utils.Logger
import com.kira.mypublishplatform.utils.other.CountDownTimerUtils
import com.kira.mypublishplatform.utils.other.EditHelper
import com.kira.mypublishplatform.utils.other.EditHelper.EditHelperData
import com.kira.mypublishplatform.utils.other.Validators
import com.kira.mypublishplatform.view.FlatButton
import com.kira.mypublishplatform.view.TitleDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
//import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.ResponseBody
import java.io.IOException
import java.util.*

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private val editHelper = EditHelper(this)
//    private val mContext: Context = this@RegisterActivity
//    override fun loadXml() {
//        setContentView(R.layout.activity_register)
//    }

    override fun getIntentData(savedInstanceState: Bundle?) {}

    override fun initData() {
        binding.loginAccount.inputType = InputType.TYPE_CLASS_NUMBER
        binding.loginAccount.keyListener =
            DigitsKeyListener.getInstance("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
        editHelper.addEditHelperData(
            EditHelperData(
                binding.loginAccount,
                Validators.getLenghMinRegex(1),
                "请输入用户名"
            )
        )
        editHelper.addEditHelperData(
            EditHelperData(
                binding.userName,
                Validators.getLenghMinRegex(1),
                "请输入中文姓名"
            )
        )
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
            if (editHelper.check(R.id.loginAccount) && editHelper.check(R.id.userName)
                && editHelper.check(R.id.phoneNumber) && editHelper.check(R.id.code)
                && editHelper.check(R.id.password) && editHelper.check(R.id.password2)
                && !isValidClick) {
                if (!Validators.isPhoneNumber(binding.phoneNumber.text.toString())) {
                    showLongToast("您输入的手机号码有误")
                    return@setOnClickListener
                }
                if (!Validators.isSimpleChinese(binding.userName.text.toString())) {
                    showLongToast("姓名存在非中文")
                    return@setOnClickListener
                }
                if (binding.password.text.toString() == binding.password2.text.toString()) { //提交用户注册信息
                    val builder = TitleDialog.Builder(this)
                    builder.setTitle("确认注册").setMessage("您是否确认所填写的信息?")
                        .setPositiveButton(
                            "确认"
                        ) { dialog: DialogInterface, _: Int ->
                            dialog.dismiss()
//                            clientRegister()
                            showLongToast("账号注册成功！")
                            finish()
                        }
                        .setNegativeButton(
                            "取消"
                        ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                        .create().show()
                } else {
                    showLongToast("两次密码输入不一致")
                }
            }
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    /**
     * 注册
     */
    private fun clientRegister() {
        LoadingBar.dialog(baseContext).extras(arrayOf<Any>("正在注册中..")).show()
        //        Gson gson = new Gson();
//        RegisterBean bean = new RegisterBean();
//        bean.setLoginAccount(account.getText().toString());
//        bean.setUserPhone(phoneNumber.getText().toString());
//        bean.setLoginPass(password.getText().toString());
//        bean.setVerCode(code.getText().toString());
//        bean.setUserName(name.getText().toString());
//        bean.setMsgId(MyApplication.mSp.getStr(ConstUtils.REGISTER_MSG));
//        String json = gson.toJson(bean);
//Logger.i("===上传最后结果===" + json);
//        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        MyApplication.api.clientRegister(binding.loginAccount.text.toString()
            , binding.password.text.toString()
            , binding.phoneNumber.text.toString()
            , binding.userName.text.toString()
            , binding.code.text.toString()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    LoadingBar.dialog(baseContext).cancel()
                    Logger.e("==onError==" + e.message)
                    showLongToast("注册失败," + e.message)
                }

                override fun onNext(body: ResponseBody) {
                    LoadingBar.dialog(baseContext).cancel()
                    try {
                        val json = body.string()
                        val rawBean =
                            Gson().fromJson(json, ResponseBean::class.java)
                        if (rawBean.isStatus) {
                            showLongToast("账号注册成功！")
                            finish()
                        } else {
                            showLongToast("注册失败," + rawBean.msg)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
    }//                                ResponseBean<String> jsonBean = new Gson().fromJson(json, new TypeToken<ResponseBean<String>>() {}.getType());
//                                String mess = jsonBean.getData();
//                                MessageBean bean = new Gson().fromJson(mess, MessageBean.class);
//                                MyApplication.mSp.putStr(ConstUtils.REGISTER_MSG, bean.getMsg_id());
//                        LoadingDialog.cancel();//                        LoadingDialog.cancel();

    /**
     * 获取验证码
     */
    private fun getMsg() {
            MyApplication.api.sendMsg(binding.phoneNumber.text.toString(), "register")
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