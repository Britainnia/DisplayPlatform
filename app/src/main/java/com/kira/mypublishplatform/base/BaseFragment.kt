package com.kira.mypublishplatform.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.kira.mypublishplatform.utils.ToastUtils
import java.lang.reflect.ParameterizedType

/**
 * 碎片基类
 * author: fwj
 */
abstract class
//BaseFragment
BaseFragment<VB : ViewBinding>
: Fragment() {

    private var _binding : VB? = null
    val binding :VB get() = _binding!!

    private var mActivity: Activity? = null
    private lateinit var mLanguage: String //系统语言

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity
        mLanguage = resources.configuration.locale.language
        //        FlurryAgent.logEvent("createFrag");
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return initView(inflater, container)

        //利用反射，调用指定ViewBinding中的inflate方法填充视图
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        _binding = method.invoke(null, layoutInflater, container, false) as VB
        return _binding!!.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(savedInstanceState)
        setListener()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    /**
     * 初始化布局
     */
//    protected abstract fun initView(inflater: LayoutInflater, container: ViewGroup?): View

    /**
     * 初始化数据
     */
    protected abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 设置监听
     */
    protected abstract fun setListener()

    fun showLongToast(msg: String) {
        ToastUtils.showLong(mActivity, msg)
    }

    fun showShortToast(msg: String) {
        ToastUtils.showShort(mActivity, msg)
    }
}
