package com.kira.mypublishplatform.activity

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.bean.ResponseBean
import com.kira.mypublishplatform.utils.*
import com.kira.mypublishplatform.utils.other.EditHelper
import com.kira.mypublishplatform.utils.other.EditHelper.EditHelperData
import com.kira.mypublishplatform.utils.other.StringUtils
import com.kira.mypublishplatform.utils.other.Validators
import com.kira.mypublishplatform.view.FlatButton
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.IOException
import java.util.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
//    private var userName: EditText? = null
//    private var password: EditText? = null
    var editHelper = EditHelper(this)
    private val mContext: Context = this@LoginActivity
    private var isRefreshToken = false //是否是刷新，是的话不进行跳转
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setAndroidNativeLightStatusBar(this, true)
        setContentView(R.layout.activity_login)
        isRefreshToken = intent.getBooleanExtra(ConstUtils.LOGIN_TOKEN, false)
        Logger.i("=====isRefreshToken=====$isRefreshToken")

        editHelper.addEditHelperData(
            EditHelperData(
                userName,
                Validators.getLenghMinRegex(1),
                R.string.inputUserName
            )
        )

        editHelper.addEditHelperData(
            EditHelperData(
                password,
                Validators.getLenghMinRegex(1),
                R.string.inputPassword
            )
        )
        userName.inputType = InputType.TYPE_CLASS_NUMBER
        userName.keyListener = DigitsKeyListener.getInstance("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

        user_login_row.setOnClickListener(this)
        register.setOnClickListener(this)
        changePassword.setOnClickListener(this)
//        userName.setText("17621035084");
//        password.setText("000000");
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.user_login_row -> if (editHelper.check(R.id.userName) && editHelper.check(R.id.password)) {
                val imm =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                Objects.requireNonNull(imm)
                    .hideSoftInputFromWindow(view.windowToken, 0) //强制隐藏键盘
                LoadingBar.dialog(mContext).extras(arrayOf<Any>("Loading。。。")).show()
                getKey()
//                login();
//                    new Timer().schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            fastLogin();
//                        }
//                    }, 500);
//                    startActivity(new Intent(mContext, MainActivity.class));
//                    finish();
            }
            R.id.register -> {
            }
            R.id.changePassword -> {
            }
        }
    }

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
                        LoadingBar.dialog(mContext).cancel()
                        Logger.e("==onError==" + e.message)
                        ToastUtils.showLong(mContext, "获取公钥失败," + e.message)
                    }

                    override fun onNext(body: ResponseBody) {
                        LoadingBar.dialog(mContext).cancel()
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
                                LoadingBar.dialog(mContext).cancel()
                                ToastUtils.showLong(mContext, "获取公钥失败," + rawBean.msg)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                })
        }

    private fun login() {
        val key = MyApplication.mSp.getStr(ConstUtils.PUBULIC_KEY)
        if (StringUtils.isBlank(key)) key else clientLogin(
            key
        )
    }

    /**
     * 登录
     */
    private fun clientLogin(key: String) {
        val name = userName.text.toString()
        val pass = password.text.toString()
        //        String register = MyApplication.RegistrationID;
        try {
            val enName = Base64Utils.encode(
                Objects.requireNonNull(
                    RSAUtils.encryptData(
                        name.toByteArray(),
                        RSAUtils.loadPublicKey(key)
                    )
                )
            )
            val enPass = Base64Utils.encode(
                Objects.requireNonNull(
                    RSAUtils.encryptData(
                        pass.toByteArray(),
                        RSAUtils.loadPublicKey(key)
                    )
                )
            )
            //            MyApplication.api.clientLogin(enName, enPass)
            MyApplication.api.clientLogin(enName, enPass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        LoadingBar.dialog(mContext).cancel()
                        Logger.e("==onError==" + e.message)
                        ToastUtils.showLong(mContext, "登录失败," + e.message)
                    }

                    override fun onNext(body: ResponseBody) {
                        LoadingBar.dialog(mContext).cancel()
                        try {
                            val json = body.string()
//                            ResponseBean rawBean = new Gson().fromJson(json, ResponseBean.class);
//                                if (rawBean.isStatus()) {
//
//                                    ResponseBean<LoginBean> bean = new Gson().fromJson(json, new TypeToken<ResponseBean<LoginBean>>() {}.getType());
//                                    LoginBean login = bean.getData();
//
//                                    if (login.getYylx().equals("YY_OLDMAN")) {
//
//                                        Objects.requireNonNull(MyApplication.Companion.getMSp()).putStr(ConstUtils.USER_NAME, login.getLoginAccount());
//                                        Objects.requireNonNull(MyApplication.Companion.getMSp()).putStr(ConstUtils.NAME, login.getUserName());
//                                        Objects.requireNonNull(MyApplication.Companion.getMSp()).putStr(ConstUtils.USER_TOKEN, login.getToken());
//                                        Objects.requireNonNull(MyApplication.Companion.getMSp()).putStr(ConstUtils.USER_ID, login.getStid());
//                                        Objects.requireNonNull(MyApplication.Companion.getMSp()).putStr(ConstUtils.USER_ROLE, login.getYylx());
//
//                                        ToastUtils.showShort(mContext, "登录成功");
//                                        Objects.requireNonNull(MyApplication.Companion.getMSp()).putBoolean(ConstUtils.LOGIN_STATE, true);
//                                        if (isRefreshToken) {
//                                            setResult(RESULT_OK);
//                                            LoginActivity.this.finish();
//                                        } else {
//                                            startActivity(new Intent(mContext, MainActivity.class));
//                                            LoginActivity.this.finish();
//                                        }
//
//                                    } else {
//                                        ToastUtils.showShort(mContext, "该账号不存在！");
//                                    }
//
//                                } else {
//                                    ToastUtils.showLong(mContext,"登录失败," + rawBean.getMsg());
//                                }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}