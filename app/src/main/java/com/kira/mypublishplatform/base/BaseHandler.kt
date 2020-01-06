package com.kira.mypublishplatform.base


import android.app.Activity
import android.os.Handler
import android.os.Message

import androidx.fragment.app.Fragment
import org.xutils.common.util.LogUtil

import java.lang.ref.WeakReference

abstract class BaseHandler : Handler {
    protected var activityWeakReference: WeakReference<Activity>? = null
    protected var fragmentWeakReference: WeakReference<Fragment>? = null

    private var isNeedFrag = false

    private constructor() //构造私有化,让调用者必须传递一个Activity 或者 Fragment的实例

    constructor(activity: Activity) {
        this.activityWeakReference = WeakReference(activity)
        isNeedFrag = false
    }

    constructor(fragment: Fragment) {
        this.fragmentWeakReference = WeakReference(fragment)
        isNeedFrag = true
    }

    override fun handleMessage(msg: Message) {
        if (activityWeakReference == null || activityWeakReference!!.get() == null || activityWeakReference!!.get()!!.isFinishing) {
            // 确认Activity是否不可用
            LogUtil.i("Activity is gone")
            handleMessage(msg, -1)
        } else if (isNeedFrag && (fragmentWeakReference == null || fragmentWeakReference!!.get() == null || fragmentWeakReference!!.get()!!.isRemoving)) {
            //确认判断Fragment不可用
            LogUtil.i("Fragment is gone")
            handleMessage(msg, -2)
        } else {
            handleMessage(msg, msg.what)
        }
    }

    /**
     * 抽象方法用户实现,用来处理具体的业务逻辑
     *
     * @param msg
     * @param what
     */
    abstract fun handleMessage(msg: Message, what: Int)

}
