package com.kira.mypublishplatform.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.InputMethodManager
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.bean.LoginBean
import com.kira.mypublishplatform.bean.ResponseBean
import com.kira.mypublishplatform.databinding.ActivityLoginBinding
import com.kira.mypublishplatform.utils.*
import com.kira.mypublishplatform.utils.other.EditHelper
import com.kira.mypublishplatform.utils.other.EditHelper.EditHelperData
import com.kira.mypublishplatform.utils.other.Validators
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.IOException

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private var editHelper = EditHelper(this)

    /**
     * 获取公钥
     */
    private fun getKey() {
            MyApplication.api.getKey()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        LoadingBar.dialog(this@LoginActivity).cancel()
                        Logger.e("==onError==" + e.message)
                        ToastUtils.showLong(baseContext, "获取公钥失败," + e.message)
                    }

                    override fun onNext(body: ResponseBody) {
//                        LoadingBar.dialog(mContext).cancel()
                        try {
                            val json = body.string()
                            val rawBean =
                                Gson().fromJson(json, ResponseBean::class.java)
                            if (rawBean.isStatus) {
                                val bean =
                                    Gson().fromJson<ResponseBean<String>>(
                                        json,
                                        object : TypeToken<ResponseBean<String?>?>() {}.type
                                    )
                                val key = bean.data
                                Logger.d(key)
                                MyApplication.mSp.putStr(ConstUtils.PUBULIC_KEY, key)
                                clientLogin(key)
                            } else {
                                LoadingBar.dialog(this@LoginActivity).cancel()
                                ToastUtils.showLong(baseContext, "获取公钥失败," + rawBean.msg)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                })
        }

    private fun quickLogin() {
        val name = binding.userName.text.toString()
        val pass = binding.password.text.toString()

        LoadingBar.dialog(this@LoginActivity).cancel()

        MyApplication.mSp.putStr(ConstUtils.USER_TOKEN, "login?token")
        MyApplication.mSp.putStr(ConstUtils.USER_PASSWORD, pass)
        MyApplication.mSp.putStr(ConstUtils.USER_NAME, "杰洛特")
        MyApplication.mSp.putStr(ConstUtils.PHONE, "13814267058")
        MyApplication.mSp.putStr(ConstUtils.NAME, name)

        showShortToast("登录成功")

        MyApplication.mSp.putBoolean(ConstUtils.LOGIN_STATE, true)
        setResult(RESULT_OK)
        finish()
    }

    /**
     * 登录
     */
    private fun clientLogin(key: String?) {
        val name = binding.userName.text.toString()
        val pass = binding.password.text.toString()
        //        String register = MyApplication.RegistrationID;
        try {
            val enName = Base64Utils.encode(

                RSAUtils.encryptData(
                    name.toByteArray(),
                    RSAUtils.loadPublicKey(key)
                )

            )
            val enPass = Base64Utils.encode(

                RSAUtils.encryptData(
                    pass.toByteArray(),
                    RSAUtils.loadPublicKey(key)
                )

            )

            MyApplication.api.clientLogin(enName, enPass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        LoadingBar.dialog(this@LoginActivity).cancel()
                        Logger.e("==onError==" + e.message)
                        ToastUtils.showLong(baseContext, "登录失败," + e.message)

                    }

                    override fun onNext(body: ResponseBody) {
                        LoadingBar.dialog(this@LoginActivity).cancel()
                        try {
                            val json = body.string()
                            val rawBean =
                                Gson().fromJson(json, ResponseBean::class.java)

                            if (rawBean.isStatus) {
                                val bean =
                                    Gson().fromJson<ResponseBean<LoginBean>>(
                                        json,
                                        object : TypeToken<ResponseBean<LoginBean?>?>() {}.type
                                    )
                                val login = bean.data

                                MyApplication.mSp.putStr(ConstUtils.USER_TOKEN, login?.token)
                                MyApplication.mSp.putStr(ConstUtils.USER_NAME, login?.loginAccount)
                                MyApplication.mSp.putStr(ConstUtils.NAME, login?.userName)

                                ToastUtils.showShort(baseContext, "登录成功")

                                MyApplication.mSp.putBoolean(ConstUtils.LOGIN_STATE, true)
                                setResult(RESULT_OK)
                                finish()

                            } else {
                                ToastUtils.showLong(baseContext, "登录失败," + rawBean.msg)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getIntentData(savedInstanceState: Bundle?) {

    }

    override fun initData() {
        editHelper.addEditHelperData(
            EditHelperData(
                binding.userName,
                Validators.getLenghMinRegex(1),
                R.string.inputUserName
            )
        )

        editHelper.addEditHelperData(
            EditHelperData(
                binding.password,
                Validators.getLenghMinRegex(1),
                R.string.inputPassword
            )
        )

        binding.userName.inputType = InputType.TYPE_CLASS_NUMBER
        binding.userName.keyListener = DigitsKeyListener.getInstance("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

        EditControlUtils.setEditTextInhibitInputSpace(binding.password)
    }

    override fun setListener() {
        binding.ivBack.setOnClickListener { finish() }

        binding.userLoginRow.setOnClickListener {
            if (!FastClickUtils.isValidClick && editHelper.check(R.id.user_name)
                && editHelper.check(R.id.password)) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(it.windowToken, 0) //强制隐藏键盘
            LoadingBar.dialog(this@LoginActivity).extras(arrayOf<Any>("登录中。。。")).show()
//            getKey()
            quickLogin()
            }
        }

        binding.register.setOnClickListener {
            if (!FastClickUtils.isValidClick)
                startActivity(
                    Intent(
                        baseContext,
                        RegisterActivity::class.java
                    )
                )
        }

        binding.reset.setOnClickListener {
            if (!FastClickUtils.isValidClick)
                startActivity(
                    Intent(
                        baseContext,
                        ResetPasswordActivity::class.java
                    )
                )
        }

        binding.showPassword.setOnCheckedChangeListener { _, b ->
            if (b) {
                //如果选中，显示密码
                binding.password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                //否则隐藏密码
                binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

    }

}