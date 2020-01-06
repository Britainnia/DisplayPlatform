package com.kira.mypublishplatform.base

import android.os.Bundle

/**
 * 懒加载fragment基类
 * viewpager 方式
 * author: shehuan
 */

abstract class LazyLoad2Fragment : BaseFragment() {
    private var isViewCreated: Boolean = false // 界面是否已创建完成
    var isVisibleToUser: Boolean = false // 是否对用户可见
    private var isDataLoaded: Boolean = false // 数据是否已请求, isNeedReload()返回false的时起作用

    /**
     * fragment再次可见时，是否重新请求数据，默认为flase则只请求一次数据
     *
     * @return
     */
    protected val isNeedReload: Boolean
        get() = true

    /**
     * ViewPager场景下，判断父fragment是否可见
     *
     * @return
     */
    private val isParentVisible: Boolean
        get() {
            val fragment = parentFragment
            return (fragment == null
                    || fragment is LazyLoad2Fragment && fragment.isVisibleToUser
                    || fragment is LazyLoad1Fragment && !fragment.isViewHidden)
        }

    fun reloadData() {
        isDataLoaded = false
    }

    // 实现具体的数据请求逻辑
    protected abstract fun loadData()

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        //        if (isVisibleToUser)
        isCanLoadData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewCreated = true
        isCanLoadData()
    }

    private fun isCanLoadData() {
        //所以条件是view初始化完成并且对用户可见
        if (isViewCreated && isVisibleToUser) {
            tryLoadData()

            //防止重复加载数据
            //            isViewCreated = false;
            //            isVisibleToUser = false;
        }
    }

    /**
     * ViewPager场景下，当前fragment可见，如果其子fragment也可见，则尝试让子fragment请求数据
     */
    private fun dispatchParentVisibleState() {
        val fragmentManager = childFragmentManager
        val fragments = fragmentManager.fragments
        if (fragments.isEmpty()) {
            return
        }
        for (child in fragments) {
            if (child is LazyLoad2Fragment && child.isVisibleToUser) {
                //                ((LazyLoad2Fragment) child).reloadData();
                child.tryLoadData()
            }
            if (child is LazyLoad1Fragment && !child.isViewHidden) {
                //                ((LazyLoad1Fragment) child).reloadData();
                child.tryLoadData()
            }
        }
    }

    open fun tryLoadData() {
        if (isViewCreated && isVisibleToUser && isParentVisible && (isNeedReload || !isDataLoaded)) {
            loadData()
            isDataLoaded = true
            dispatchParentVisibleState()
        }
    }
}
