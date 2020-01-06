package com.kira.mypublishplatform.base

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.utils.StatusBarUtils

/**
 * 活动的基类
 */
abstract class BaseActivity : AppCompatActivity() {
    private lateinit var mLanguage: String //系统语言
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setStatusBarColor(this, resources.getColor(R.color.theme))
        mLanguage = resources.configuration.locale.language
        MyApplication.addActivity(this)
//        FlurryAgent.logEvent("createAct");
        loadXml()
        getIntentData(savedInstanceState)
        initView()
        initData()
        setListener()
    }

    override fun onStart() {
        super.onStart()
        //        FlurryAgent.setCaptureUncaughtExceptions(true);//这个看个人需要
        //        FlurryAgent.onStartSession(this, MyApplication.FlurryKey);
    }

    override fun onStop() {
        super.onStop()
        //        FlurryAgent.onEndSession(this);
    }

    override fun onDestroy() {
        //        FlurryAgent.logEvent("destroyAct");
        MyApplication.finishActivity(this)
        super.onDestroy()
    }

    //不根据系统字体的大小改变
    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }

    /**
     * 加载布局
     */
    protected abstract fun loadXml()

    /**
     * 得到上一个Activity传来的Intent数据
     *
     * @param savedInstanceState
     */
    protected abstract fun getIntentData(savedInstanceState: Bundle?)

    /**
     * 初始化控件
     */
    protected abstract fun initView()

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 设置监听
     */
    protected abstract fun setListener()


    fun showLongToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}
