package com.kira.mypublishplatform.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * 碎片基类
 * author: fwj
 */
abstract class BaseFragment : Fragment() {
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
        return initView(inflater, container)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findView(view)
        initData(savedInstanceState)
        setListener()
    }

    /**
     * 初始化布局
     */
    protected abstract fun initView(inflater: LayoutInflater, container: ViewGroup?): View

    /**
     * 找控件
     */
    protected abstract fun findView(view: View)

    /**
     * 初始化数据
     */
    protected abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 设置监听
     */
    protected abstract fun setListener()

    fun showLongToast(msg: String) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(msg: String) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show()
    }
}
