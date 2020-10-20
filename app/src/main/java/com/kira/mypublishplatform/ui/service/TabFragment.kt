package com.kira.mypublishplatform.ui.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.activity.HeadActivity
import com.kira.mypublishplatform.adapter.ScrollLeftAdapter
import com.kira.mypublishplatform.adapter.ScrollRightAdapter
import com.kira.mypublishplatform.adapter.TabRecyclerAdapter
import com.kira.mypublishplatform.base.LazyLoad1Fragment
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.bean.AppBean
import com.kira.mypublishplatform.bean.ResponseBean
import com.kira.mypublishplatform.bean.ScrollBean
import com.kira.mypublishplatform.bean.ScrollBean.ScrollItemBean
import com.kira.mypublishplatform.databinding.FragmentTabBinding
import com.kira.mypublishplatform.model.MultipleItem
import com.kira.mypublishplatform.model.OldInfoModel
import com.kira.mypublishplatform.model.SubModel
import com.kira.mypublishplatform.utils.Logger
import com.kira.mypublishplatform.utils.StatusBarUtils

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.IOException
import java.util.*

class TabFragment : LazyLoad1Fragment<FragmentTabBinding>() {
    private val oldInfoModel = OldInfoModel()

    private var mManager: LinearLayoutManager? = null
    private var mAdapter: TabRecyclerAdapter? = null
    private val tabList: MutableList<MultipleItem?> = arrayListOf()

    //右侧title在数据中所对应的position集合
    private val tPosition: MutableList<Int> = ArrayList()
//    private var mContext: Context? = null

    //title的高度
    private var tHeight = 0

    //记录右侧当前可见的第一个item的position
    private var first = 0
    private var rightManager: GridLayoutManager? = null

    protected var mActivity: HeadActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as HeadActivity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun initData(savedInstanceState: Bundle?) {

        createData()
        mManager = LinearLayoutManager(mActivity)
        binding.recyclerView.layoutManager = mManager

    }

    override fun setListener() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.recyclerView.setOnScrollChangeListener { _: View?, _: Int, _: Int, _: Int, _: Int ->
                //滑动RecyclerView list的时候，根据最上面一个Item的position来切换tab
                binding.tabLayout.setScrollPosition(mManager!!.findFirstVisibleItemPosition(), 0f, true)
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) { //点击tab的时候，RecyclerView自动滑到该tab对应的item位置
                mManager!!.scrollToPositionWithOffset(tab.position, 0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                mManager!!.scrollToPositionWithOffset(tab.position, 0)
            }
        })

    }

    override fun loadData() {
//        getInfo()
//        getAPP()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
//            getInfo()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            StatusBarUtils.setAndroidNativeLightStatusBar(mActivity, true)
        }
    }

    private fun initTab() {
        binding.tabLayout.removeAllTabs()
        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        for (i in tabList.indices) binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(tabList[i]!!.type).setTag(i)
        )

        mAdapter = TabRecyclerAdapter(tabList)
        binding.recyclerView.adapter = mAdapter
    }

    /**
     * 获取应用
     */
    private fun getAPP() {
        LoadingBar.dialog(mActivity).extras(arrayOf<Any>("waiting....")).show()
        MyApplication.api.getApps()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    LoadingBar.dialog(mActivity).cancel()
                    Logger.e("==onError==" + e.message)
                    showLongToast("获取应用失败," + e.message)
                }

                override fun onNext(body: ResponseBody) {
                    LoadingBar.dialog(mActivity).cancel()
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
                            LoadingBar.dialog(mActivity).cancel()
                            showLongToast("获取应用失败," + rawBean.msg)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
    }

    //获取数据(若请求服务端数据,请求到的列表需有序排列)
    private fun createData() {

        val item1 = MultipleItem(MultipleItem.ITEM)

        val title1 = "热门服务"
        val list1 = mutableListOf<SubModel>()
        list1.add(SubModel(name = "养老服务","file:///android_asset/old.png","http://183.134.253.238/", enable = true, needLogin = true))
        list1.add(SubModel(name = "金融超市","file:///android_asset/market.png","https://www.yyjrcs.cn/", enable = true, needLogin = true))
        list1.add(SubModel(name = "新冠疫情","file:///android_asset/virus.png","https://intellect.manniuhealth.com/selfTest/html/chat/main.html?v=1.5.0&bud_c=nationsmy&bud_e=", enable = true, needLogin = false))
        list1.add(SubModel(name = "政府资讯","file:///android_asset/government_file.png","http://www.gov.cn/zfwj/search.htm", enable = true, needLogin = false))
        list1.add(SubModel(name = "垃圾分类","file:///android_asset/garbage_classification.png","http://www.greenchina2030.cn/technology_g.html", enable = true, needLogin = false))

        item1.type = title1
        item1.items = list1

        val item2 = MultipleItem(MultipleItem.ITEM)
        val title2 = "公司产品"
        val list2 = mutableListOf<SubModel>()
        list2.add(SubModel(name = "养老服务","file:///android_asset/old.png","http://183.134.253.238/", enable = true, needLogin = true))
        list2.add(SubModel(name = "金融超市","file:///android_asset/market.png","https://www.yyjrcs.cn/", enable = true, needLogin = true))

        item2.type = title2
        item2.items = list2

        val item3 = MultipleItem(MultipleItem.ITEM)
        val title3 = "政务大厅"
        val list3 = mutableListOf<SubModel>()
        list3.add(SubModel(name = "社区服务","file:///android_asset/community.png","", enable = true, needLogin = true))
        list3.add(SubModel(name = "政府文件","file:///android_asset/government_file.png","http://www.gov.cn/zfwj/search.htm", enable = true, needLogin = true))
        list3.add(SubModel(name = "政务","file:///android_asset/government_affair.png","", enable = true, needLogin = true))

        item3.type = title3
        item3.items = list3

        val item4 = MultipleItem(MultipleItem.ITEM)
        val list4 = mutableListOf<SubModel>()
        list4.add(SubModel(name = "垃圾分类","file:///android_asset/garbage_classification.png","http://www.greenchina2030.cn/technology_g.html", enable = true, needLogin = false))
        list4.add(SubModel(name = "爱心捐助","file:///android_asset/donation.png","", enable = true, needLogin = false))
        list4.add(SubModel(name = "环保","file:///android_asset/recycle.png","", enable = true, needLogin = false))
        list4.add(SubModel(name = "一站式服务","file:///android_asset/onestop_service.png","", enable = true, needLogin = false))
        list4.add(SubModel(name = "附近学校","file:///android_asset/school.png","", enable = true, needLogin = true))
        list4.add(SubModel(name = "附近加油站","file:///android_asset/gas.png","", enable = true, needLogin = true))
        list4.add(SubModel(name = "附近银行","file:///android_asset/bank.png","", enable = true, needLogin = true))

        item4.type = "便民服务"
        item4.items = list4

        val item5 = MultipleItem(MultipleItem.ITEM)
        val list5 = mutableListOf<SubModel>()
        list5.add(SubModel(name = "公交","file:///android_asset/bus.png","", enable = true, needLogin = false))
        list5.add(SubModel(name = "地铁","file:///android_asset/underground.png","", enable = true, needLogin = false))
        list5.add(SubModel(name = "旅游出行","file:///android_asset/travel.png","", enable = true, needLogin = false))
        list5.add(SubModel(name = "出入境","file:///android_asset/entry_exit.png","", enable = true, needLogin = true))

        item5.type = "交通出行"
        item5.items = list5

        val item6 = MultipleItem(MultipleItem.ITEM)
        val list6 = mutableListOf<SubModel>()
        list6.add(SubModel(name = "空气质量","file:///android_asset/air_quality.png","", enable = true, needLogin = false))
        list6.add(SubModel(name = "饮食","file:///android_asset/diet.png","", enable = true, needLogin = false))
        list6.add(SubModel(name = "医疗保健","file:///android_asset/healthcare.png","", enable = true, needLogin = true))
        list6.add(SubModel(name = "天气预报","file:///android_asset/forecast.png","", enable = true, needLogin = false))
        list6.add(SubModel(name = "垃圾回收","file:///android_asset/garbage_classification.png","", enable = true, needLogin = false))
        list6.add(SubModel(name = "娱乐","file:///android_asset/entertainment.png","", enable = true, needLogin = false))
        list6.add(SubModel(name = "问卷调查","file:///android_asset/investigation.png","", enable = true, needLogin = false))
        list6.add(SubModel(name = "维修服务","file:///android_asset/maintain.png","", enable = true, needLogin = false))
        list6.add(SubModel(name = "网络设备","file:///android_asset/network.png","", enable = true, needLogin = false))
        list6.add(SubModel(name = "地图","file:///android_asset/map.png","", enable = true, needLogin = false))
        list6.add(SubModel(name = "系统安全","file:///android_asset/safe_box.png","", enable = true, needLogin = true))

        item6.type = "居民生活"
        item6.items = list6

        tabList.add(item1)
        tabList.add(item2)
        tabList.add(item3)
        tabList.add(item4)
        tabList.add(item5)
        tabList.add(item6)

        initTab()

    }

    /**
     * 获得资源 dimens (dp)
     *
     * @param context
     * @param id      资源id
     * @return
     */
    fun getDimens(context: Context, id: Int): Float {
        val dm = context.resources.displayMetrics
        val px = context.resources.getDimension(id)
        return px / dm.density
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    fun dpToPx(context: Context, dp: Float): Int {
        val displayMetrics = context.resources.displayMetrics
        return (dp * displayMetrics.density + 0.5f).toInt()
    }

    companion object {
        fun newInstance(): TabFragment {
            return TabFragment()
        }
    }
}