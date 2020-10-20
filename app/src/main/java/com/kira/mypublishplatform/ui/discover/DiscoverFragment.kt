package com.kira.mypublishplatform.ui.discover

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import com.kira.mypublishplatform.base.LazyLoad1Fragment
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.bean.BaseListBean
import com.kira.mypublishplatform.bean.ResponseBean
import com.kira.mypublishplatform.databinding.FragmentDiscoverBinding
import com.kira.mypublishplatform.model.InformationModel
import com.kira.mypublishplatform.ui.web.HandleWebActivity
import com.kira.mypublishplatform.utils.ConstUtils
import com.kira.mypublishplatform.utils.Logger
import com.kira.mypublishplatform.utils.StatusBarUtils
import com.kira.mypublishplatform.view.NetworkImageHolderView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.IOException

class DiscoverFragment : LazyLoad1Fragment<FragmentDiscoverBinding>() {
    protected var mActivity: HeadActivity? = null
    private var infoList: MutableList<InformationModel> = arrayListOf()
    private var networkImages: MutableList<Any> = arrayListOf()
    private var baseQuickAdapter: BaseQuickAdapter<InformationModel, BaseViewHolder>? = null
    private var listCount = 0
    private var nowPage = 1

    private val images = mutableListOf(
        "https://www.gbicc.net/images/index_product1.jpg",
        "https://www.gbicc.net/images/index_product2.jpg",
        "https://www.gbicc.net/images/index_product3.jpg",
        "https://www.gbicc.net/images/index_product4.jpg",
        "https://www.gbicc.net/images/index_product5.jpg",
        "https://www.gbicc.net/images/index_product6.jpg",
        "https://www.gbicc.net/images/index_product7.jpg",
        "https://www.gbicc.net/images/index_product8.jpg"
    )

    override fun initData(savedInstanceState: Bundle?) { //下拉刷新的圆圈是否显示
        binding.refresh.isRefreshing = false
        //设置下拉时圆圈的颜色（可以由多种颜色拼成）
        binding.refresh.setColorSchemeResources(
            android.R.color.holo_blue_light,
            android.R.color.holo_red_light,
            android.R.color.holo_orange_light
        )
        //设置下拉时圆圈的背景颜色（这里设置成白色）
        binding.refresh.setProgressBackgroundColorSchemeResource(android.R.color.white)
        binding.recyclerView.layoutManager = LinearLayoutManager(mActivity)
        binding.recyclerView.adapter = object : BaseQuickAdapter<InformationModel, BaseViewHolder>(
            R.layout.layout_item_discover,
            infoList
        ) {
            override fun convert(
                helper: BaseViewHolder,
                item: InformationModel
            ) {
                val options = RequestOptions()
                    .placeholder(R.mipmap.no_pic)     //占位图
                    .error(R.mipmap.no_resource)	  //异常占位图

                when (helper.layoutPosition % 3){
                    0 -> {
                        helper.setGone(R.id.normal_item, false)
                            .setGone(R.id.top_item, true)
                            .setGone(R.id.multiply_item, false)
                            .setText(R.id.top_title, item.title)
                        Glide.with(mContext)
                            .load(item.img)
                            .apply(options)
                            .into(helper.getView(R.id.top_image))
                        helper.setText(
                            R.id.top_news_from,
                            if (item.fromSource == null) "" else item.fromSource
                        )
                            .setText(R.id.top_publish_date, item.publishDate)
                    }
                    1 -> {
                        helper.setGone(R.id.top_item, false)
                            .setGone(R.id.normal_item, true)
                            .setGone(R.id.multiply_item, false)
                            .setText(R.id.news_title, item.title)
                        Glide.with(mContext)
                            .load(item.img)
                            .apply(options)
//                        .preload()
                            .into(helper.getView(R.id.news_image))
                        helper.setText(
                            R.id.normal_news_from,
                            if (item.fromSource == null) "" else item.fromSource
                        )
                            .setText(R.id.normal_publish_date, item.publishDate)
                    }
                    2 -> {
                        helper.setGone(R.id.normal_item, false)
                            .setGone(R.id.top_item, false)
                            .setGone(R.id.multiply_item, true)
                            .setText(R.id.multiply_title, item.title)
                        Glide.with(mContext)
                            .load(item.img)
                            .apply(options)
                            .into(helper.getView(R.id.multiply_image_one))
                        Glide.with(mContext)
                            .load(item.uuser)
                            .apply(options)
                            .into(helper.getView(R.id.multiply_image_two))
                        Glide.with(mContext)
                            .load(item.cuser)
                            .apply(options)
                            .into(helper.getView(R.id.multiply_image_three))
                        helper.setText(
                            R.id.multiply_news_from,
                            if (item.fromSource == null) "" else item.fromSource
                        )
                            .setText(R.id.multiply_publish_date, item.publishDate)
                    }
                }

            }
        }.also { baseQuickAdapter = it }

        initEmptyUI()
        baseQuickAdapter!!.setNewData(null)
        baseQuickAdapter!!.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
        baseQuickAdapter!!.isFirstOnly(false)

//        getAllList(false)
        createList(false)
        initBannerList()
//        getBannerList()

    }

    override fun setListener() { //具体操作
        binding.refresh.setOnRefreshListener {
//            getAllList(false)
            createList(false)
        }

        baseQuickAdapter!!.setOnLoadMoreListener({
            binding.refresh.isEnabled = false
            binding.recyclerView.postDelayed({
                if (baseQuickAdapter!!.data.size >= listCount) { //数据全部加载完毕
                    baseQuickAdapter!!.loadMoreEnd()
                } else {
//                    getAllList(true)
                    createList(true)
                }
                binding.refresh.isEnabled = true
            }, 700)
        }, binding.recyclerView)

        baseQuickAdapter!!.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, position: Int ->
                val intent = Intent()
                intent.setClass(mActivity!!, HandleWebActivity::class.java)
                    .putExtra("title", infoList[position].title)
                    .putExtra("url", infoList[position].typeCode)
                startActivity(intent)
            }
    }

    private fun initEmptyUI() {
        val noDataView = layoutInflater.inflate(R.layout.empty_view, binding.recyclerView, false)
        val hint = noDataView.findViewById<TextView>(R.id.none)
        hint.setText(R.string.no_information)
        baseQuickAdapter!!.emptyView = noDataView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as HeadActivity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun loadData() {}

//    private fun refresh() {
//
//        Timer().schedule(object : TimerTask() {
//            override fun run() {
//                getAllList(false)
//            }
//        }, 700)
//    }

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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            StatusBarUtils.setTranslucentStatusBar(mActivity, true)
        }
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

    private fun createList(isAdd: Boolean) {

        val json = if (isAdd) {
    //            nowPage = 1
            "{\"total\":5,\"rows\":[{\"id\":30,\"title\":\"资源共享，合作共赢 | 吉贝克与麒麟软件正式达成战略合作！\",\"typeCode\":\"https://mp.weixin.qq.com/s/qB9xHrR-vlFbgfF6tInFYQ\",\"publishDate\":\"2020-01-03\",\"img\":\"https://mmbiz.qpic.cn/mmbiz_png/IEpib9zMHrZVuCPC8icD1JCTvvHVutIvbTe0NOxiaATjZSCXZJLgARQZOPibhIzFtsjOvL8nJB100aiaOhFKFlZVUhg/640?wx_fmt=png\",\"note\":\"活动地址：凤山街道子陵社区111号\r\n活动时间：2019.12.7.18:00-2019.12.7.21:00\",\"createdTime\":\"2020-06-28 11:55:23\",\"updateTime\":\"2020-08-17 19:41:26\",\"oindex\":0,\"fromSource\":\"吉贝克GBICC\"}," +
                    "{\"id\":32,\"title\":\"合作共赢，共筑生态 | 吉贝克与蚂蚁金服、华为、京东数科、麒麟软件达成战略合作，加快推进软件国产化\",\"typeCode\":\"https://mp.weixin.qq.com/s/d4__zreDUBprd-ZUmD5I3g\",\"publishDate\":\"2019-11-23\",\"img\":\"https://mmbiz.qpic.cn/mmbiz_jpg/IEpib9zMHrZUdmWQ45Wc3YiaqEF0nmSnDkbRu2p8icmbuoa9r4btATiaibuMNicMZK1Y0LdQwj2nNt9WMicaAqw76ic8zw/640?wx_fmt=jpeg\",\"note\":\"活动地址：兰江街道锦凤社区\r\n活动时间：2019.12.7.8:30-2019.12.7.16:30\",\"createdTime\":\"2020-06-28 11:55:23\",\"updateTime\":\"2020-08-17 19:41:26\",\"oindex\":0,\"fromSource\":\"吉贝克GBICC\"}]} "
        } else {
            infoList.clear()
            "{\"total\":5,\"rows\":[{\"id\":34,\"title\":\"江南农商行再度签约吉贝克：深化数据整合，实现精准决策\",\"typeCode\":\"https://mp.weixin.qq.com/s/0kiEc1Gr7cvt_ad-Nha0-w\",\"publishDate\":\"前天\",\"img\":\"https://mmbiz.qpic.cn/mmbiz_png/IEpib9zMHrZVBhG4awFZic2kIYKm9PEoLvnq7DWdib30bHbLYZ3MrZhicL0zUffBk1TLtp5f7Bpb6NVgukfFv8cXtg/640?wx_fmt=png\",\"note\":\"活动地址：马渚镇姚家村\r\n活动时间：2019.12.7.10:30-2019.12.7.13:30\",\"createdTime\":\"2020-06-28 11:55:23\",\"updateTime\":\"2020-08-17 19:41:26\",\"oindex\":0,\"fromSource\":\"吉贝克GBICC\"}," +
                    "{\"id\":37,\"title\":\"中国芯，英雄心：吉贝克“IFRS9系统”采用自主研发数据库和引擎系统，响应自主创新浪潮\",\"typeCode\":\"https://mp.weixin.qq.com/s/hyhili1CNTLl-M5f4NV6oA\",\"publishDate\":\"6天前\",\"img\":\"https://mmbiz.qpic.cn/mmbiz_png/SZzImXPBMF2ibicD3M3wuqRW0LBsTg4PtdPCTIHDypjVYS3gjvAysa6K5iaicXzTTTlJ3oJDbpHuNibO7seOwaDgD8A/640?wx_fmt=png\",\"note\":\"活动地址：大岚镇新岚村\r\n活动时间：2019.12.7.9:00-2019.12.7.16:00\",\"createdTime\":\"2020-06-28 11:55:23\",\"updateTime\":\"2020-08-17 19:41:26\",\"oindex\":0,\"fromSource\":\"吉贝克GBICC\"}," +
                    "{\"id\":31,\"title\":\"吉贝克董事长刘世平博士受邀出席“2019全球分布式云存储生态合作伙伴（中国·青岛）大会”并发表主题演讲\",\"typeCode\":\"https://mp.weixin.qq.com/s/96WN3B-S4q4jpPYB57jhrQ\",\"publishDate\":\"2020-05-03\",\"img\":\"https://mmbiz.qpic.cn/mmbiz_jpg/IEpib9zMHrZVLLRNgadib9oR5HC2jO9ib1XCfJmmLKxT5RYibgl8iaDKpibomsD7UHfiahTaXuOB7ialMiavHccyhBRox7g/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1\",\"cuser\":\"https://mmbiz.qpic.cn/mmbiz_jpg/IEpib9zMHrZVLLRNgadib9oR5HC2jO9ib1XM1qU71gsrlCqPGgeDia6Y4HoxzN75MPb6dddkHrribwWAVuYKzqiaAwTQ/640?wx_fmt=jpeg\",\"uuser\":\"https://mmbiz.qpic.cn/mmbiz_jpg/IEpib9zMHrZVLLRNgadib9oR5HC2jO9ib1XcibEwIrIqxz0yQyiaWjOgqCRyUicXp0mVfQE0O5jBJWN0K1nl3PX7Ts3Q/640?wx_fmt=jpeg\",\"createdTime\":\"2020-06-28 11:55:23\",\"updateTime\":\"2020-08-17 19:41:26\",\"oindex\":0,\"fromSource\":\"吉贝克GBICC\"}]} "
        }

        if (!isAdd) binding.refresh.isRefreshing = false

        val bean =
            Gson().fromJson<BaseListBean<InformationModel>>(
                json,
                object :
                    TypeToken<BaseListBean<InformationModel?>?>() {}.type
            )

//        nowPage++
        val lastSize = infoList.size
        infoList.addAll(bean?.rows!!)
        if (!isAdd) {
            listCount = bean.total
            if (infoList.size > 0) {
                baseQuickAdapter!!.setNewData(infoList)
            } else {
                baseQuickAdapter!!.setNewData(null)
            }
        } else {
            baseQuickAdapter!!.notifyItemRangeInserted(
                lastSize,
                bean.rows!!.size
            )
            baseQuickAdapter!!.loadMoreComplete()
        }

    }

    /**
     * 获取所有活动列表
     */
    private fun getAllList(isAdd: Boolean) {

        if (!isAdd) {
            infoList.clear()
            nowPage = 1
            LoadingBar.dialog(mActivity).extras(arrayOf<Any>("资讯获取中...")).show()
        }
        val limit = 8
        MyApplication.api.getInfoList("DISCOVERY", nowPage, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Logger.e("==onError==" + e.message)
                    if (!isAdd) { //下拉刷新的圆圈是否显示
                        binding.refresh.isRefreshing = false
                        LoadingBar.dialog(mActivity).cancel()
                        baseQuickAdapter!!.setNewData(null)

                    } else {
                        baseQuickAdapter!!.loadMoreFail()
                    }
                    showLongToast("获取订单失败," + e.message)
                }

                override fun onNext(body: ResponseBody) {
                    if (!isAdd) { //下拉刷新的圆圈是否显示
                        binding.refresh.isRefreshing = false
                        LoadingBar.dialog(mActivity).cancel()
                    }
                    try {
                        val json = body.string()
                        val rawBean =
                            Gson().fromJson(json, ResponseBean::class.java)
                        if (rawBean.isStatus) {
                            val bean =
                                Gson().fromJson<ResponseBean<BaseListBean<InformationModel>>>(
                                    json,
                                    object :
                                        TypeToken<ResponseBean<BaseListBean<InformationModel?>?>?>() {}.type
                                )
                            val list = bean.data
                            nowPage++
                            val lastSize = infoList.size
                            infoList.addAll(list?.rows!!)
                            if (!isAdd) {
                                listCount = list.total
                                if (infoList.size > 0) {
                                    baseQuickAdapter!!.setNewData(infoList)
                                } else {
                                    baseQuickAdapter!!.setNewData(null)
                                }
                            } else {
                                baseQuickAdapter!!.notifyItemRangeInserted(
                                    lastSize,
                                    list.rows!!.size
                                )
                                baseQuickAdapter!!.loadMoreComplete()
                            }
                        } else {
                            if (isAdd) {
                                baseQuickAdapter!!.loadMoreFail()
                            } else {
                                baseQuickAdapter!!.setNewData(null)
                            }
                            if ("10009" == rawBean.code) {
                                val intent = Intent(mActivity, LoginActivity::class.java)
                                intent.putExtra(ConstUtils.LOGIN_TOKEN, true)
                                startActivity(intent)
                                showLongToast("登录过期，请重新登录")
                            } else {
                                showLongToast("获取订单失败," + rawBean.msg)
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
    }

    companion object {
        fun newInstance() = DiscoverFragment()
    }
}