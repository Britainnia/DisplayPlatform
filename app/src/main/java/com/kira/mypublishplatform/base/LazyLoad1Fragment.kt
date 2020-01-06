package com.kira.mypublishplatform.base

/**
 * 懒加载fragment基类
 * show/hide 方式
 * author: shehuan
 */

abstract class LazyLoad1Fragment : BaseFragment() {
    private var isDataLoaded: Boolean = false // 数据是否已请求
    var isViewHidden: Boolean = true // 记录当前fragment的是否隐藏

    /**
     * fragment再次可见时，是否重新请求数据，默认为flase则只请求一次数据
     *
     * @return
     */
    protected val isNeedReload: Boolean
        get() = true

    /**
     * show()、hide()场景下，父fragment是否显示
     *
     * @return
     */
    //    private boolean isParentHidden() {
    //        Fragment fragment = getParentFragment();
    //        if (fragment == null) {
    //            return false;
    //        } else
    //            return !(fragment instanceof LazyLoad1Fragment) || ((LazyLoad1Fragment) fragment).isHidden;
    //    }

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

    /**
     * 使用show()、hide()控制fragment显示、隐藏时回调该方法
     *
     * @param hidden
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isViewHidden = hidden
        if (!hidden) {
            tryLoadData()
        }
    }

    /**
     * show()、hide()场景下，当前fragment没隐藏，如果其子fragment也没隐藏，则尝试让子fragment请求数据
     */
    private fun dispatchParentHiddenState() {
        val fragmentManager = childFragmentManager
        val fragments = fragmentManager.fragments
        if (fragments.isEmpty()) {
            return
        }
        for (child in fragments) {
            if (child is LazyLoad1Fragment && !child.isViewHidden) {
                //            if (child instanceof LazyLoad1Fragment) {
                //                ((LazyLoad1Fragment) child).reloadData();
                child.tryLoadData()
            }
            if (child is LazyLoad2Fragment && child.isVisibleToUser) {
                //            if (child instanceof LazyLoad2Fragment) {
                //                ((LazyLoad2Fragment) child).reloadData();
                child.tryLoadData()
            }
        }
    }

    /**
     * show()、hide()场景下，尝试请求数据
     */
    open fun tryLoadData() {
        if (isParentVisible && (isNeedReload || !isDataLoaded)) {
            loadData()
            isDataLoaded = true
            dispatchParentHiddenState()
        }
    }
}
