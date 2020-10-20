package com.kira.mypublishplatform.ui.discover

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.activity.LoginActivity
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.bean.BaseListBean
import com.kira.mypublishplatform.bean.ResponseBean
import com.kira.mypublishplatform.databinding.ActivityDiscoverBinding
import com.kira.mypublishplatform.model.InformationModel
import com.kira.mypublishplatform.ui.web.WebActivity
import com.kira.mypublishplatform.utils.ConstUtils
import com.kira.mypublishplatform.utils.Logger
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.IOException
import java.util.*

class DiscoverActivity : BaseActivity<ActivityDiscoverBinding>() {

    private var infoList: MutableList<InformationModel> = arrayListOf()
    private var baseQuickAdapter: BaseQuickAdapter<InformationModel, BaseViewHolder>? = null
    private var listCount = 0
    private var nowPage = 1

    override fun getIntentData(savedInstanceState: Bundle?) {

    }

    override fun initData() {
        binding.refresh.isRefreshing = false
        //设置下拉时圆圈的颜色（可以由多种颜色拼成）
        binding.refresh.setColorSchemeResources(
            android.R.color.holo_blue_light,
            android.R.color.holo_red_light,
            android.R.color.holo_orange_light
        )
        //设置下拉时圆圈的背景颜色（这里设置成白色）
        binding.refresh.setProgressBackgroundColorSchemeResource(android.R.color.white)
        binding.recyclerView.layoutManager = LinearLayoutManager(baseContext)
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
                if (helper.layoutPosition % 3 == 0) {
                    helper.setGone(R.id.normal_item, false)
                        .setGone(R.id.top_item, true)
                        .setText(R.id.top_title, item.title)
                    Glide.with(mContext)
                        .load(item.img)
                        .apply(options)
                        .into((helper.getView<View>(R.id.top_image) as ImageView))
                } else {
                    helper.setGone(R.id.top_item, false)
                        .setGone(R.id.normal_item, true)
                        .setText(R.id.news_title, item.title)
                    Glide.with(mContext)
                        .load(item.img)
                        .apply(options)
//                        .preload()
                        .into((helper.getView<View>(R.id.news_image) as ImageView))
                }
                helper.setText(
                    R.id.news_from,
                    if (item.fromSource == null) "" else item.fromSource
                )
                    .setText(R.id.publish_date, item.publishDate)
            }
        }.also { baseQuickAdapter = it }

        initEmptyUI()
        baseQuickAdapter!!.setNewData(null)
        baseQuickAdapter!!.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
        baseQuickAdapter!!.isFirstOnly(false)
        refresh()
    }

    override fun setListener() {
        binding.refresh.setOnRefreshListener { refresh() }
        baseQuickAdapter!!.setOnLoadMoreListener({
            binding.refresh.isEnabled = false
            binding.recyclerView.postDelayed({
                if (baseQuickAdapter!!.data.size >= listCount) { //数据全部加载完毕
                    baseQuickAdapter!!.loadMoreEnd()
                } else {
                    getAllList(true)
                }
                binding.refresh.isEnabled = true
            }, 700)
        }, binding.recyclerView)
        baseQuickAdapter!!.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, position: Int ->
                val intent = Intent()
                intent.setClass(baseContext, WebActivity::class.java)
                    .putExtra("title", infoList[position].title)
                    .putExtra("url", infoList[position].content)
                    .putExtra("id", infoList[position].id)
                startActivity(intent)
            }
    }

    private fun initEmptyUI() {
        val noDataView = layoutInflater.inflate(R.layout.empty_view, binding.recyclerView, false)
        val hint = noDataView.findViewById<TextView>(R.id.none)
        hint.setText(R.string.no_information)
        baseQuickAdapter!!.emptyView = noDataView
    }

    private fun refresh() {
        LoadingBar.dialog(baseContext).extras(arrayOf<Any>("资讯获取中...")).show()
        Timer().schedule(object : TimerTask() {
            override fun run() {
                getAllList(false)
            }
        }, 700)
    }

    /**
     * 获取所有活动列表
     */
    private fun getAllList(isAdd: Boolean) {
        if (!isAdd) {
            infoList.clear()
            nowPage = 1
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
                        LoadingBar.dialog(baseContext).cancel()
                        baseQuickAdapter!!.setNewData(null)

                    } else {
                        baseQuickAdapter!!.loadMoreFail()
                    }
                    showLongToast("获取订单失败," + e.message)
                }

                override fun onNext(body: ResponseBody) {
                    if (!isAdd) { //下拉刷新的圆圈是否显示
                        binding.refresh.isRefreshing = false
                        LoadingBar.dialog(baseContext).cancel()
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
                                val intent = Intent(baseContext, LoginActivity::class.java)
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
}