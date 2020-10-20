package com.kira.mypublishplatform.ui.person

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.bean.message.MegLogBean
import com.kira.mypublishplatform.databinding.ActivityMessageBinding
import com.kira.mypublishplatform.utils.DateFormatUtil
import com.kira.mypublishplatform.utils.DateFormatUtil.Companion.formatDateTimeMill
import kotlinx.android.synthetic.*
import java.util.*

class MessageActivity : BaseActivity<ActivityMessageBinding>() {

    private val logList: List<MegLogBean> = ArrayList()
    private var baseQuickAdapter: BaseQuickAdapter<*, *>? = null

    override fun getIntentData(savedInstanceState: Bundle?) {}

    override fun initData() { //下拉刷新的圆圈是否显示
        binding.refresh.isRefreshing = false
        //设置下拉时圆圈的颜色（可以由多种颜色拼成）
        binding.refresh.setColorSchemeResources(
            android.R.color.holo_blue_light,
            android.R.color.holo_red_light,
            android.R.color.holo_orange_light
        )

        //设置下拉时圆圈的背景颜色（这里设置成白色）
        binding.refresh.setProgressBackgroundColorSchemeResource(android.R.color.white)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = object :
            BaseQuickAdapter<MegLogBean, BaseViewHolder>(R.layout.layout_item_message, logList) {
            override fun convert(helper: BaseViewHolder, bean: MegLogBean) {
                helper.setText(R.id.title_status, bean.title)
                    .setText(
                        R.id.refresh_date,
                        formatDateTimeMill(
                            bean.createTime,
                            DateFormatUtil.YMD_HMS_FORMAT
                        )
                    )
                    .setText(R.id.log_content, bean.body)
            }
        }.also { baseQuickAdapter = it }
        initEmptyUI()
        baseQuickAdapter!!.setNewData(null)
        baseQuickAdapter!!.openLoadAnimation()
        LoadingBar.dialog(this).extras(arrayOf<Any>("消息获取中..")).show()
        Timer().schedule(object : TimerTask() {
            override fun run() {
                pushMessage
            }
        }, 500)
    }

    override fun setListener() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.refresh.setOnRefreshListener {
            LoadingBar.dialog(this).extras(arrayOf<Any>("消息获取中..")).show()
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    pushMessage
                }
            }, 500)
        }

//        baseQuickAdapter!!.onItemClickListener =
//            BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int -> }
    }

    private fun initEmptyUI() {
//        val inflater = layoutInflater
        val noDataView = layoutInflater.inflate(R.layout.empty_view, binding.recyclerView, false)
        val hint = noDataView.findViewById<TextView>(R.id.none)
        hint.setText(R.string.no_order)
        baseQuickAdapter!!.emptyView = noDataView
    }

//        String token = MyApplication.mSp.getStr(ConstUtils.USER_TOKEN);
//        MyApplication.api.getPM(token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LoadingDialog.cancel();
//                        swipeRefreshLayout.setRefreshing(false);
//                        Logger.e("==onError==" + e.getMessage());
//                        showLongToast("获取消息失败," + e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody body) {
//                        LoadingDialog.cancel();
//                        swipeRefreshLayout.setRefreshing(false);
//                        try {
//
//                            String json = body.string();
//                            ResponseBean rawBean = new Gson().fromJson(json, ResponseBean.class);
//
//                            if (rawBean.isStatus()) {
//                                ResponseBean<List<MegLogBean>> bean = new Gson().fromJson(json, new TypeToken<ResponseBean<List<MegLogBean>>>() {}.getType());
//
//                                logList = bean.getData();
//                                baseQuickAdapter.setNewData(logList);
//
//                                if (logList.size() > 0) {
//                                    mTv.setVisibility(View.GONE);
//                                    mRv.setVisibility(View.VISIBLE);
//                                } else {
//                                    mRv.setVisibility(View.GONE);
//                                    mTv.setVisibility(View.VISIBLE);
//                                }
//
//                            } else if ("10009".equals(rawBean.getCode())) {
//
//                                mRv.setVisibility(View.GONE);
//                                mTv.setVisibility(View.VISIBLE);
//
//                                //token过期了，重新登录
//                                Intent intent = new Intent(MessageActivity.this, LoginActivity.class);
//                                //intent.putExtra(ConstUtils.LOGIN_TOKEN, true);
//                                startActivityForResult(intent, 250);
//                                showLongToast("登录过期，请重新登录");
//                            } else {
//                                showShortToast("获取消息失败," + rawBean.getMsg());
//                            }
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });

    /**
     * 获取
     */
    private val pushMessage: Unit
        get() {
            LoadingBar.dialog(this).cancel()
            binding.refresh.isRefreshing = false
            //        String token = MyApplication.mSp.getStr(ConstUtils.USER_TOKEN);
//        MyApplication.api.getPM(token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LoadingDialog.cancel();
//                        swipeRefreshLayout.setRefreshing(false);
//                        Logger.e("==onError==" + e.getMessage());
//                        showLongToast("获取消息失败," + e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody body) {
//                        LoadingDialog.cancel();
//                        swipeRefreshLayout.setRefreshing(false);
//                        try {
//
//                            String json = body.string();
//                            ResponseBean rawBean = new Gson().fromJson(json, ResponseBean.class);
//
//                            if (rawBean.isStatus()) {
//                                ResponseBean<List<MegLogBean>> bean = new Gson().fromJson(json, new TypeToken<ResponseBean<List<MegLogBean>>>() {}.getType());
//
//                                logList = bean.getData();
//                                baseQuickAdapter.setNewData(logList);
//
//                                if (logList.size() > 0) {
//                                    mTv.setVisibility(View.GONE);
//                                    mRv.setVisibility(View.VISIBLE);
//                                } else {
//                                    mRv.setVisibility(View.GONE);
//                                    mTv.setVisibility(View.VISIBLE);
//                                }
//
//                            } else if ("10009".equals(rawBean.getCode())) {
//
//                                mRv.setVisibility(View.GONE);
//                                mTv.setVisibility(View.VISIBLE);
//
//                                //token过期了，重新登录
//                                Intent intent = new Intent(MessageActivity.this, LoginActivity.class);
//                                //intent.putExtra(ConstUtils.LOGIN_TOKEN, true);
//                                startActivityForResult(intent, 250);
//                                showLongToast("登录过期，请重新登录");
//                            } else {
//                                showShortToast("获取消息失败," + rawBean.getMsg());
//                            }
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
        }
}