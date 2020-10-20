package com.kira.mypublishplatform.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.adapter.TabRecyclerAdapter
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.bean.AppBean
import com.kira.mypublishplatform.bean.ResponseBean
import com.kira.mypublishplatform.databinding.ActivityTabBinding
import com.kira.mypublishplatform.model.MultipleItem
import com.kira.mypublishplatform.utils.Logger
import com.kira.mypublishplatform.utils.ToastUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
//import kotlinx.android.synthetic.main.activity_tab.*
import okhttp3.ResponseBody
import java.io.IOException
import java.util.*

class TabActivity : BaseActivity<ActivityTabBinding>() {

    private val mContext: Context = this@TabActivity
    private var mManager: LinearLayoutManager? = null
    private var mAdapter: TabRecyclerAdapter? = null
    private val tabList: MutableList<MultipleItem?> = arrayListOf()

    private fun initTab() {
        binding.tabLayout.removeAllTabs()
        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        for (i in tabList.indices) binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(tabList[i]!!.type).setTag(i)
        )
    }

    /**
     * 获取应用
     */
    private fun getAPP() {
            LoadingBar.dialog(mContext).extras(arrayOf<Any>("waiting....")).show()
            MyApplication.api.getApps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        LoadingBar.dialog(mContext).cancel()
                        Logger.e("==onError==" + e.message)
                        ToastUtils.showLong(mContext, "获取应用失败," + e.message)
                    }

                    override fun onNext(body: ResponseBody) {
                        LoadingBar.dialog(mContext).cancel()
                        try {
                            val json = body.string()
                            val rawBean =
                                Gson().fromJson(json, ResponseBean::class.java)
                            if (rawBean.isStatus) {
                                val bean =
                                    Gson().fromJson<ResponseBean<AppBean>>(
                                        json,
                                        object : TypeToken<ResponseBean<AppBean?>?>() {}.type
                                    )
                                val app = bean.data
                                tabList.addAll(app?.apps!!)

                                initTab()

                                mAdapter = TabRecyclerAdapter(tabList)
                                binding.recyclerView.adapter = mAdapter
                            } else {
                                LoadingBar.dialog(mContext).cancel()
                                ToastUtils.showLong(mContext, "获取应用失败," + rawBean.msg)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                })
        }

//    override fun loadXml() {
//        setContentView(R.layout.activity_tab)
//    }

    override fun getIntentData(savedInstanceState: Bundle?) {}

    override fun initData() {
        initTab()
        mManager = LinearLayoutManager(mContext)
        binding.recyclerView.layoutManager = mManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.recyclerView.setOnScrollChangeListener { _: View?, _: Int, _: Int, _: Int, _: Int ->
                //滑动RecyclerView list的时候，根据最上面一个Item的position来切换tab
                binding.tabLayout.setScrollPosition(mManager!!.findFirstVisibleItemPosition(), 0f, true)
            }
        }
    }

    override fun setListener() {
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) { //点击tab的时候，RecyclerView自动滑到该tab对应的item位置
                mManager!!.scrollToPositionWithOffset(tab.position, 0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                mManager!!.scrollToPositionWithOffset(tab.position, 0)
            }
        })
        getAPP()
    }
}