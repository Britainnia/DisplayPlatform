package com.kira.mypublishplatform.ui.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.dyhdyh.widget.loadingbar2.LoadingBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kira.mypublishplatform.R;
import com.kira.mypublishplatform.activity.HeadActivity;
import com.kira.mypublishplatform.activity.LoginActivity;
import com.kira.mypublishplatform.base.LazyLoad1Fragment;
import com.kira.mypublishplatform.base.MyApplication;
import com.kira.mypublishplatform.bean.BaseListBean;
import com.kira.mypublishplatform.bean.ResponseBean;
import com.kira.mypublishplatform.model.InformationModel;
import com.kira.mypublishplatform.ui.web.WebActivity;
import com.kira.mypublishplatform.utils.ConstUtils;
import com.kira.mypublishplatform.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class DiscoverFragment extends LazyLoad1Fragment {

    protected HeadActivity mActivity;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRv;
    private TextView mTv;
    private List<InformationModel> infoList = new ArrayList<>();
    private BaseQuickAdapter<InformationModel, BaseViewHolder> baseQuickAdapter;

    private int listCount;
    private int nowPage = 1;

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    protected void findView(View view) {

        mRv = view.findViewById(R.id.recyclerView);
        mTv = view.findViewById(R.id.none);
//        mBanner = view.findViewById(R.id.convenientBanner);
        swipeRefreshLayout = view.findViewById(R.id.refresh);

    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

        initUI();

        //下拉刷新的圆圈是否显示
        swipeRefreshLayout.setRefreshing(false);

        //设置下拉时圆圈的颜色（可以由多种颜色拼成）
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);

        //设置下拉时圆圈的背景颜色（这里设置成白色）
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);

        mRv.setLayoutManager(new LinearLayoutManager(mActivity));

        mRv.setAdapter(baseQuickAdapter = new BaseQuickAdapter<InformationModel, BaseViewHolder>(R.layout.layout_item_discover, infoList) {
            @Override
            protected void convert(BaseViewHolder helper, InformationModel item) {

                if (item.getOindex() % 3 == 0) {
                    helper.setGone(R.id.normal_item, false)
                            .setGone(R.id.top_item, true);

                    Glide.with(mContext)
                            .load(item.getImg())
                            .into((ImageView) helper.getView(R.id.top_image));
                } else {
                    helper.setGone(R.id.top_item, false)
                            .setGone(R.id.normal_item, true);

                    Glide.with(mContext)
                            .load(item.getImg())
                            .into((ImageView) helper.getView(R.id.news_image));
                }


                helper.setText(R.id.news_title, item.getTitle())
                        .setText(R.id.news_from, item.getFromSource() == null ? "" : item.getFromSource())
                        .setText(R.id.publish_date, item.getPublishDate());


            }
        });

        baseQuickAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);

    }

    @Override
    protected void setListener() {

        //具体操作
        swipeRefreshLayout.setOnRefreshListener(this::tryLoadData);

        baseQuickAdapter.setOnLoadMoreListener(() -> {
            swipeRefreshLayout.setEnabled(false);
            mRv.postDelayed(() -> {
                if (baseQuickAdapter.getData().size() >= listCount) {
                    //数据全部加载完毕
                    baseQuickAdapter.loadMoreEnd();
                } else {
                    getAllList(true);
                }

                swipeRefreshLayout.setEnabled(true);
            }, 700);
        }, mRv);

        baseQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent();
            intent.setClass(mActivity, WebActivity.class)
                    .putExtra("title", infoList.get(position).getTitle())
                    .putExtra("url", infoList.get(position).getContent())
                    .putExtra("id", infoList.get(position).getId());
            startActivity(intent);
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HeadActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    private void initUI() {

        mRv.setVisibility(View.GONE);
        mTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void tryLoadData() {

        super.reloadData();
        super.tryLoadData();

    }

    @Override
    protected void loadData() {

        LoadingBar.dialog(mActivity).extras(new Object[] {"waiting..."}).show();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                getAllList(false);

            }
        }, 700);

    }

    /**
     * 获取所有活动列表
     */
    private void getAllList(boolean isAdd) {

        if (!isAdd) {
            infoList.clear();
            nowPage = 1;
        }

        int limit = 10;

        MyApplication.api.getInfoList("LRSHQ",nowPage,limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("==onError==" + e.getMessage());
                        if (!isAdd) {
                            //下拉刷新的圆圈是否显示
                            swipeRefreshLayout.setRefreshing(false);
                            LoadingBar.dialog(mActivity).cancel();

                            mRv.setVisibility(View.GONE);
                            mTv.setVisibility(View.VISIBLE);
                        } else {
                            baseQuickAdapter.loadMoreFail();
                        }
                        showLongToast("获取订单失败," + e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        if (!isAdd) {
                            //下拉刷新的圆圈是否显示
                            swipeRefreshLayout.setRefreshing(false);
                            LoadingBar.dialog(mActivity).cancel();
                        }
                        try {

                            String json = body.string();
                            ResponseBean rawBean = new Gson().fromJson(json, ResponseBean.class);

                            if (rawBean.isStatus()) {
                                ResponseBean<BaseListBean<InformationModel>> bean = new Gson().fromJson(json, new TypeToken<ResponseBean<BaseListBean<InformationModel>>>() {}.getType());

                                BaseListBean<InformationModel> list = bean.getData();

                                nowPage++;
                                int lastSize = infoList.size();
                                for (InformationModel info: list.getRows()) {
                                    if ("LRSHQ_XCT".equals(info.getTypeCode()))
                                        continue;
                                    infoList.add(info);
                                }

                                if (!isAdd) {
                                    listCount = list.getTotal();
                                    baseQuickAdapter.setNewData(infoList);
                                    if (infoList.size() > 0) {
                                        mTv.setVisibility(View.GONE);
                                        mRv.setVisibility(View.VISIBLE);
                                    } else {
                                        mRv.setVisibility(View.GONE);
                                        mTv.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    baseQuickAdapter.notifyItemRangeInserted(lastSize, list.getRows().size());
                                    baseQuickAdapter.loadMoreComplete();
                                }

                            } else {
                                if (isAdd) {
                                    mTv.setVisibility(View.GONE);
                                    mRv.setVisibility(View.VISIBLE);
                                    baseQuickAdapter.loadMoreFail();
                                } else {
                                    mRv.setVisibility(View.GONE);
                                    mTv.setVisibility(View.VISIBLE);
                                }

                                if ("10009".equals(rawBean.getCode())) {
                                    Intent intent = new Intent(mActivity, LoginActivity.class);
                                    intent.putExtra(ConstUtils.LOGIN_TOKEN, true);
                                    startActivity(intent);
                                    showLongToast("登录过期，请重新登录");
                                } else {
                                    showLongToast("获取订单失败," + rawBean.getMsg());
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

}
