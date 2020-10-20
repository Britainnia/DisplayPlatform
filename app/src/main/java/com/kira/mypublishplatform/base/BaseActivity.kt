package com.kira.mypublishplatform.base

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.utils.StatusBarUtils
import com.kira.mypublishplatform.utils.ToastUtils
import java.lang.reflect.ParameterizedType

/**
 * 活动的基类
 */
abstract class
//BaseActivity
BaseActivity<VB : ViewBinding>
: AppCompatActivity() {

    lateinit var binding: VB
    private lateinit var mLanguage: String //系统语言
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setStatusBarColor(this, resources.getColor(R.color.theme))
        mLanguage = resources.configuration.locale.language

        //利用反射，调用指定ViewBinding中的inflate方法填充视图
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[0] as Class<VB>
            val method = clazz.getMethod("inflate", LayoutInflater::class.java)
            binding = method.invoke(null, layoutInflater) as VB
            setContentView(binding.root)
        }

        MyApplication.addActivity(this)
//        FlurryAgent.logEvent("createAct");
//        loadXml()
        getIntentData(savedInstanceState)
        initData()
        setListener()
    }

    override fun onDestroy() {
        //        FlurryAgent.logEvent("destroyAct");
        MyApplication.finishActivity(this)
        LoadingBar.release()
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
//    protected abstract fun loadXml()

    /**
     * 得到上一个Activity传来的Intent数据
     *
     * @param savedInstanceState
     */
    protected abstract fun getIntentData(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 设置监听
     */
    protected abstract fun setListener()


    fun showLongToast(msg: String) {
        ToastUtils.showLong(this, msg)
    }

    fun showShortToast(msg: String) {
        ToastUtils.showShort(this, msg)
    }

}
