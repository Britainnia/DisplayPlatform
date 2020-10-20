package com.kira.mypublishplatform.ui.head

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.activity.HeadActivity
import com.kira.mypublishplatform.activity.LoginActivity
import com.kira.mypublishplatform.adapter.SubAdapter
import com.kira.mypublishplatform.base.LazyLoad1Fragment
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.bean.AppBean
import com.kira.mypublishplatform.bean.BaseListBean
import com.kira.mypublishplatform.bean.ResponseBean
import com.kira.mypublishplatform.bean.WeatherBean
import com.kira.mypublishplatform.databinding.FragmentHeadBinding
import com.kira.mypublishplatform.model.InformationModel
import com.kira.mypublishplatform.model.SubModel
import com.kira.mypublishplatform.ui.web.HandleWebActivity
import com.kira.mypublishplatform.utils.ConstUtils
import com.kira.mypublishplatform.utils.FastClickUtils.Companion.isValidClick
import com.kira.mypublishplatform.utils.LocationUtils
import com.kira.mypublishplatform.utils.Logger
import com.kira.mypublishplatform.utils.StatusBarUtils
import com.kira.mypublishplatform.view.NetworkImageHolderView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.IOException
import java.text.MessageFormat

class HeadFragment : LazyLoad1Fragment<FragmentHeadBinding>() {

    companion object {
        fun newInstance() = HeadFragment()
    }

    protected var mActivity: HeadActivity? = null
    private var info: MutableList<InformationModel> = arrayListOf()
    private var sub: MutableList<SubModel> = arrayListOf()
//    private var top: List<SubModel> = arrayListOf()
    private var networkImages: MutableList<Any> = arrayListOf()
//    private var mBanner: ConvenientBanner<Any>? = null
    private var baseQuickAdapter: BaseQuickAdapter<InformationModel, BaseViewHolder>? = null
    private var subAdapter: SubAdapter? = null
//    private var topAdapter: SubAdapter? = null

    private val images = mutableListOf(
        "https://www.gbicc.net/banner/54eff0d3-d89a-4979-854d-1e1d9886bd00.jpg",
        "https://www.gbicc.net/banner/70cc1b73-df9b-41b4-9af6-b5ee50c6c756.jpg",
        "https://www.gbicc.net/banner/c27ae1e3-4134-44c7-a1c5-005ef98e5d82.jpg",
        "https://www.gbicc.net/banner/f08d9df1-d8a7-4a5d-9b0e-bb6b7b3cf651.jpg",
        "https://www.gbicc.net/banner/32c00879-7353-4c6f-895d-bb8848cac1f5.jpg"
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as HeadActivity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun loadData() {

//        binding.me.text =
//            if (!MyApplication.mSp.getBoolean(ConstUtils.LOGIN_STATE)) resources.getString(R.string.log_in)
//            else MyApplication.mSp.getStr(ConstUtils.NAME)

    }

    override fun initData(savedInstanceState: Bundle?) {

        binding.rvNews.layoutManager = LinearLayoutManager(mActivity)
        binding.rvNews.adapter = object : BaseQuickAdapter<InformationModel, BaseViewHolder>(
            R.layout.layout_home_discover,
            info
        ) {
            override fun convert(
                helper: BaseViewHolder,
                item: InformationModel
            ) {
                val options = RequestOptions()
                    .placeholder(R.mipmap.no_pic)     //占位图
                    .error(R.mipmap.no_resource)	  //异常占位图

                helper.setGone(R.id.top_item, false)
                    .setGone(R.id.normal_item, true)
                    .setText(R.id.news_title, item.title)
                    .setText(
                        R.id.news_from,
                        if (item.fromSource == null) "" else item.fromSource
                    )
                    .setText(R.id.publish_date, item.publishDate)
                Glide.with(mContext)
                    .load(item.img)
                    .apply(options)
                    .into(helper.getView(R.id.news_image))

            }
        }.also { baseQuickAdapter = it }

        initEmptyUI()
        baseQuickAdapter!!.setNewData(null)

//        binding.topService.layoutManager = GridLayoutManager(mActivity, 5)
//        topAdapter = SubAdapter(initTop())
//        binding.topService.adapter = topAdapter

        binding.rvService.layoutManager = GridLayoutManager(mActivity, 5)
        subAdapter = SubAdapter(sub)
        binding.rvService.adapter = subAdapter

        LocationUtils.getCNByLocation(mActivity)

        getWeather()

        initService()
//        getService()
        initBannerList()
//        getBannerList()
//        getInfoList()
        initInfoList()
    }

    override fun setListener() {

        subAdapter!!.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, position: Int ->
                if (!isValidClick) {
                    if (position < sub.size - 1) {
                        if (sub[position].enable!!) {
                            val intent = Intent(context, HandleWebActivity::class.java)
                            intent.putExtra("title", sub[position].name)
                                .putExtra("url", sub[position].href)
                            startActivity(intent)
                        }
                    } else {
                        mActivity!!.shiftNavigation(R.id.service_tab)
                    }
                }
            }

//        topAdapter!!.onItemClickListener =
//            BaseQuickAdapter.OnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, position: Int ->
//                if (!isValidClick) {
//                    val intent = Intent(context, HandleWebActivity::class.java)
//                    intent.putExtra("title", initTop()[position].name)
//                        .putExtra("url", initTop()[position].href)
//                    startActivity(intent)
//                }
//            }

        baseQuickAdapter!!.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, position: Int ->
                if (!isValidClick) {
                    val intent = Intent(context, HandleWebActivity::class.java)
                    intent.putExtra("title", info[position].title)
                        .putExtra("url", info[position].typeCode)
                    startActivity(intent)
                }
            }

//        binding.me.setOnClickListener{
//            mActivity!!.shiftNavigation(
//                R.id.me_tab
//            )
//        }

        binding.newsMore.setOnClickListener {
            mActivity!!.shiftNavigation(
                R.id.discover_tab
            )
        }

    }

    // 开始自动翻页
    override fun onResume() {
        super.onResume()
        //开始自动翻页
        binding.convenientBanner.startTurning()
    }

    // 停止自动翻页
    override fun onPause() {
        super.onPause()
        //停止翻页
        binding.convenientBanner.stopTurning()
    }

    private fun initTop(): MutableList<SubModel> {
        val list = mutableListOf<SubModel>()
        list.add(
            SubModel(
                name = "养老服务",
                "file:///android_asset/old.png",
                "http://183.134.253.238/",
                enable = true,
                needLogin = true
            )
        )
        list.add(
            SubModel(
                name = "金融超市",
                "file:///android_asset/market.png",
                "https://www.yyjrcs.cn/",
                enable = true,
                needLogin = true
            )
        )
        list.add(
            SubModel(
                name = "新冠疫情",
                "http://183.134.253.238/newOld/stpg.png",
                "https://intellect.manniuhealth.com/selfTest/html/chat/main.html?v=1.5.0&bud_c=nationsmy&bud_e=",
                enable = true,
                needLogin = true
            )
        )
        list.add(
            SubModel(
                name = "政府资讯",
                "http://183.134.253.238/newOld/zffw.png",
                "http://www.shanghai.gov.cn/nw2/nw2314/nw43190/nw43855/index.html",
                enable = true,
                needLogin = true
            )
        )
        list.add(
            SubModel(
                name = "垃圾分类",
                "http://183.134.253.238/newOld/ylfwbt.png",
                "http://www.greenchina2030.cn/technology_g.html",
                enable = true,
                needLogin = true
            )
        )
        return list
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            StatusBarUtils.setTranslucentStatusBar(mActivity, true)
        }
    }

    /**
     * 获取服务列表
     */
    private fun getWeather() {
        LoadingBar.dialog(mActivity).extras(arrayOf<Any>("天气获取中..")).show()
//        String token = MyApplication.mSp.getStr(ConstUtils.USER_TOKEN);

        val city = LocationUtils.cityName

        if (city == null || city == "无法获取地理信息") {
            LoadingBar.dialog(mActivity).cancel()
            return
        }

        MyApplication.api.getWeather("SaiDJvc3bndZobrUc", city, "zh-Hans")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    LoadingBar.dialog(mActivity).cancel()
                    Logger.e("==onError==" + e.message)
                    showLongToast("获取天气信息失败," + e.message)
                }

                override fun onNext(body: ResponseBody) {
                    LoadingBar.dialog(mActivity).cancel()
                    try {
                        val json = body.string()
                        val rawBean = Gson().fromJson(
                            json,
                            WeatherBean::class.java
                        )

                        if (rawBean.results != null) {
                            val apps = rawBean.results
                            val item = apps!![0]
                            binding.weatherDate.text = item.last_update.substring(0, 10)
                            val now = item.now
                            binding.weatherTemperature.text = MessageFormat.format(
                                "{0} ℃",
                                now.temperature
                            )
                            binding.weatherType.text = now.text
                            val loc = item.location
                            binding.weatherCity.text = loc.name
                        } else {
                            showLongToast("获取天气信息失败！")
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
    }

    private fun initBannerList() {

        val bannerList = images
        networkImages.clear()
        for (info in bannerList) {
            networkImages.add(info)
        }
        binding.convenientBanner.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): NetworkImageHolderView {
                return NetworkImageHolderView(itemView, mActivity)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_networkimage
            }
        }, networkImages)
            .setPageIndicator(
                intArrayOf(
                    R.mipmap.ic_page_indicator,
                    R.mipmap.ic_page_indicator_focused
                )
            )
            .setOnItemClickListener { position: Int ->
                Logger.i("点击了第" + position + "个")
            }

    }

    /**
     * 获取轮播图列表
     */
    private fun getBannerList() {
        MyApplication.api.getInfoList("BANNER", 1, 6)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Logger.e("==onError==" + e.message)
//                    LoadingBar.dialog(mActivity).cancel()
                    showLongToast("获取轮播图失败," + e.message)
                }

                override fun onNext(body: ResponseBody) {
//                    LoadingBar.dialog(mActivity).cancel()
                    try {
                        val json = body.string()
                        val rawBean = Gson().fromJson(
                            json,
                            ResponseBean::class.java
                        )
                        when {
                            rawBean.isStatus -> {
                                val bean =
                                    Gson().fromJson<ResponseBean<BaseListBean<InformationModel>>>(
                                        json,
                                        object :
                                            TypeToken<ResponseBean<BaseListBean<InformationModel?>?>?>() {}.type
                                    )
                                val list = bean.data
                                val bannerList = list?.rows!!
                                networkImages.clear()
                                for (info in bannerList) {
                                    networkImages.add(info.img!!)
                                }
                                binding.convenientBanner.setPages(object : CBViewHolderCreator {
                                    override fun createHolder(itemView: View): NetworkImageHolderView {
                                        return NetworkImageHolderView(itemView, mActivity)
                                    }

                                    override fun getLayoutId(): Int {
                                        return R.layout.item_networkimage
                                    }
                                }, networkImages)
                                    .setPageIndicator(
                                        intArrayOf(
                                            R.mipmap.ic_page_indicator,
                                            R.mipmap.ic_page_indicator_focused
                                        )
                                    )
                                    .setOnItemClickListener { position: Int ->
//                Intent intent = new Intent();
//                intent.setClass(mActivity, WebActivity.class)
//                        .putExtra("title", bannerList.get(position).getTitle())
//                        .putExtra("url", bannerList.get(position).getContent())
//                        .putExtra("id", bannerList.get(position).getId());
//                startActivity(intent);
                                        Logger.i("点击了第" + position + "个")
                                    }
                            }
                            "10009" == rawBean.code -> {
                                val intent = Intent(
                                    mActivity,
                                    LoginActivity::class.java
                                )
                                intent.putExtra(ConstUtils.LOGIN_TOKEN, true)
                                startActivity(intent)
                                showLongToast("登录过期，请重新登录")
                            }
                            else -> {
                                showLongToast("获取轮播图失败," + rawBean.msg)
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
    }

    private fun initService() {
        sub.clear()
        sub.add(
            SubModel(
                name = "养老服务",
                "file:///android_asset/old.png",
                "http://183.134.253.238/",
                enable = true,
                needLogin = true
            )
        )
        sub.add(
            SubModel(
                name = "金融超市",
                "file:///android_asset/market.png",
                "https://www.yyjrcs.cn/",
                enable = true,
                needLogin = true
            )
        )
        sub.add(
            SubModel(
                name = "数据分析",
                "file:///android_asset/data_analysis.png",
                "",
                enable = false,
                needLogin = false
            )
        )
        sub.add(
            SubModel(
                name = "饮食",
                "file:///android_asset/diet.png",
                "",
                enable = false,
                needLogin = false
            )
        )
        sub.add(
            SubModel(
                name = "娱乐",
                "file:///android_asset/entertainment.png",
                "",
                enable = false,
                needLogin = true
            )
        )
        sub.add(
            SubModel(
                name = "政府文件",
                "file:///android_asset/government_file.png",
                "http://www.gov.cn/zfwj/search.htm",
                enable = false,
                needLogin = true
            )
        )
        sub.add(
            SubModel(
                name = "问卷调查",
                "file:///android_asset/investigation.png",
                "",
                enable = false,
                needLogin = false
            )
        )
        sub.add(
            SubModel(
                name = "环保",
                "file:///android_asset/recycle.png",
                "",
                enable = true,
                needLogin = false
            )
        )
        sub.add(
            SubModel(
                name = "疫情防控",
                "file:///android_asset/virus.png",
                "https://intellect.manniuhealth.com/selfTest/html/chat/main.html?v=1.5.0&bud_c=nationsmy&bud_e=",
                enable = false,
                needLogin = true
            )
        )
        sub.add(
            SubModel(
                name = "更多",
                "file:///android_asset/more.png",
                "",
                enable = true,
                needLogin = false
            )
        )

        subAdapter!!.setNewData(sub)
    }

    /**
     * 获取服务列表
     */
    private fun getService() {
        LoadingBar.dialog(mActivity).extras(arrayOf<Any>("信息获取中..")).show()
//        String token = MyApplication.mSp.getStr(ConstUtils.USER_TOKEN);
        MyApplication.api.getApps()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    LoadingBar.dialog(mActivity).cancel()
                    Logger.e("==onError==" + e.message)
                    showLongToast("获取服务信息失败," + e.message)
                }

                override fun onNext(body: ResponseBody) {
                    LoadingBar.dialog(mActivity).cancel()
                    try {
                        val json = body.string()
                        val rawBean = Gson().fromJson(
                            json,
                            ResponseBean::class.java
                        )
                        when {
                            rawBean.isStatus -> {
                                val bean =
                                    Gson().fromJson<ResponseBean<AppBean>>(
                                        json,
                                        object : TypeToken<ResponseBean<AppBean?>?>() {}.type
                                    )
                                val app = bean.data
                                val item = app?.apps!![0]
                                sub = item.items
                                subAdapter!!.setNewData(sub)
                            }
                            "10009" == rawBean.code -> {
                                val intent = Intent(
                                    mActivity,
                                    LoginActivity::class.java
                                )
                                intent.putExtra(ConstUtils.LOGIN_TOKEN, true)
                                startActivityForResult(intent, 250)
                                showLongToast("登录过期，请重新登录")
                            }
                            else -> {
                                showLongToast("获取服务信息失败！" + rawBean.msg)
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
    }

    /**
     * 获取资讯列表
     */
    private fun getInfoList() {
        LoadingBar.dialog(mActivity).extras(arrayOf<Any>("资讯获取中..")).show()
//        String token = MyApplication.mSp.getStr(ConstUtils.USER_TOKEN);
        MyApplication.api.getInfoList("FRONTDT", 1, 4)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    LoadingBar.dialog(mActivity).cancel()
                    Logger.e("==onError==" + e.message)
                    showLongToast("获取资讯信息失败," + e.message)
                }

                override fun onNext(body: ResponseBody) {
                    LoadingBar.dialog(mActivity).cancel()
                    try {
                        val json = body.string()
                        val rawBean = Gson().fromJson(
                            json,
                            ResponseBean::class.java
                        )
                        when {
                            rawBean.isStatus -> {
                                val bean =
                                    Gson().fromJson<ResponseBean<BaseListBean<InformationModel>>>(
                                        json,
                                        object :
                                            TypeToken<ResponseBean<BaseListBean<InformationModel?>?>?>() {}.type
                                    )
                                val list = bean.data
                                info = list?.rows!!
//                                baseQuickAdapter!!.setNewData(info)
                            }
                            "10009" == rawBean.code -> {
                                val intent = Intent(
                                    mActivity,
                                    LoginActivity::class.java
                                )
                                intent.putExtra(ConstUtils.LOGIN_TOKEN, true)
                                startActivityForResult(intent, 250)
                                showLongToast("登录过期，请重新登录")
                            }
                            else -> {
                                showLongToast("获取资讯信息失败！" + rawBean.msg)
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
    }

    private fun initInfoList() {

        info.clear()

        val json = "{\"total\":5,\"rows\":[{\"id\":30,\"title\":\"资源共享，合作共赢 | 吉贝克与麒麟软件正式达成战略合作！\",\"typeCode\":\"https://mp.weixin.qq.com/s/qB9xHrR-vlFbgfF6tInFYQ\",\"publishDate\":\"3天前\",\"img\":\"https://mmbiz.qpic.cn/mmbiz_png/IEpib9zMHrZVuCPC8icD1JCTvvHVutIvbTe0NOxiaATjZSCXZJLgARQZOPibhIzFtsjOvL8nJB100aiaOhFKFlZVUhg/640?wx_fmt=png\",\"note\":\"活动地址：凤山街道子陵社区111号\r\n活动时间：2019.12.7.18:00-2019.12.7.21:00\",\"createdTime\":\"2020-06-28 11:55:23\",\"updateTime\":\"2020-08-17 19:41:26\",\"oindex\":0,\"fromSource\":\"吉贝克GBICC\"}," +
                "{\"id\":37,\"title\":\"中国芯，英雄心：吉贝克“IFRS9系统”采用自主研发数据库和引擎系统，响应自主创新浪潮\",\"typeCode\":\"https://mp.weixin.qq.com/s/hyhili1CNTLl-M5f4NV6oA\",\"publishDate\":\"6天前\",\"img\":\"https://mmbiz.qpic.cn/mmbiz_png/SZzImXPBMF2ibicD3M3wuqRW0LBsTg4PtdPCTIHDypjVYS3gjvAysa6K5iaicXzTTTlJ3oJDbpHuNibO7seOwaDgD8A/640?wx_fmt=png\",\"note\":\"活动地址：大岚镇新岚村\r\n活动时间：2019.12.7.9:00-2019.12.7.16:00\",\"createdTime\":\"2020-06-28 11:55:23\",\"updateTime\":\"2020-08-17 19:41:26\",\"oindex\":0,\"fromSource\":\"吉贝克GBICC\"}," +
                "{\"id\":32,\"title\":\"合作共赢，共筑生态 | 吉贝克与蚂蚁金服、华为、京东数科、麒麟软件达成战略合作，加快推进软件国产化\",\"typeCode\":\"https://mp.weixin.qq.com/s/d4__zreDUBprd-ZUmD5I3g\",\"publishDate\":\"2019-11-23\",\"img\":\"https://mmbiz.qpic.cn/mmbiz_jpg/IEpib9zMHrZUdmWQ45Wc3YiaqEF0nmSnDkbRu2p8icmbuoa9r4btATiaibuMNicMZK1Y0LdQwj2nNt9WMicaAqw76ic8zw/640?wx_fmt=jpeg\",\"note\":\"活动地址：兰江街道锦凤社区\r\n活动时间：2019.12.7.8:30-2019.12.7.16:30\",\"createdTime\":\"2020-06-28 11:55:23\",\"updateTime\":\"2020-08-17 19:41:26\",\"oindex\":0,\"fromSource\":\"吉贝克GBICC\"}]} "


        val bean =
            Gson().fromJson<BaseListBean<InformationModel>>(
                json,
                object :
                    TypeToken<BaseListBean<InformationModel?>?>() {}.type
            )

        info.addAll(bean?.rows!!)

        if (info.size > 0) {
            baseQuickAdapter!!.setNewData(info)
        } else {
            baseQuickAdapter!!.setNewData(null)
        }

    }

    private fun initEmptyUI() {
        val noDataView = layoutInflater.inflate(R.layout.empty_view, binding.rvNews, false)
        val hint = noDataView.findViewById<TextView>(R.id.none)
        hint.setText(R.string.no_information)
        baseQuickAdapter!!.emptyView = noDataView
    }

}