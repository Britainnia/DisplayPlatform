package com.kira.mypublishplatform.ui.head;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.dyhdyh.widget.loadingbar2.LoadingBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kira.mypublishplatform.R;
import com.kira.mypublishplatform.activity.HeadActivity;
import com.kira.mypublishplatform.activity.LoginActivity;
import com.kira.mypublishplatform.activity.TabActivity;
import com.kira.mypublishplatform.base.LazyLoad1Fragment;
import com.kira.mypublishplatform.base.MyApplication;
import com.kira.mypublishplatform.bean.BaseListBean;
import com.kira.mypublishplatform.bean.ResponseBean;
import com.kira.mypublishplatform.model.InformationModel;
import com.kira.mypublishplatform.model.SubModel;
import com.kira.mypublishplatform.ui.web.HandleWebActivity;
import com.kira.mypublishplatform.ui.web.WebActivity;
import com.kira.mypublishplatform.utils.ConstUtils;
import com.kira.mypublishplatform.utils.FastClickUtils;
import com.kira.mypublishplatform.utils.Logger;
import com.kira.mypublishplatform.utils.StatusBarUtils;
import com.kira.mypublishplatform.view.NetworkImageHolderView;
import com.kira.mypublishplatform.view.ObservableScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class HeadFragment extends LazyLoad1Fragment {

//    private HomeViewModel mViewModel;
    public static HeadFragment newInstance() {
        return new HeadFragment();
    }

    protected HeadActivity mActivity;
    private List<InformationModel> info = new ArrayList<>();
    private String phoneNumber, newsCode;

    private RecyclerView rNews, rService;
    private TextView mMore, mTitle;
    private ConvenientBanner mBanner;
    private ObservableScrollView mObservableScrollView;
    private ConstraintLayout mHeaderContent;
    private BaseQuickAdapter<InformationModel, BaseViewHolder> baseQuickAdapter;

    private List<String> networkImages = new ArrayList<>();
    private String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };

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

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.head_fragment, container, false);
    }

    @Override
    protected void findView(View view) {

        rNews = view.findViewById(R.id.rv_news);
        rService = view.findViewById(R.id.rv_service);

        mMore = view.findViewById(R.id.news_more);
        mObservableScrollView = view.findViewById(R.id.sv_main_content);
        mHeaderContent = view.findViewById(R.id.ll_header_content);
        mBanner = view.findViewById(R.id.convenientBanner);
        mTitle = view.findViewById(R.id.tv_title);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

        rNews.setLayoutManager(new LinearLayoutManager(getContext()));

        rNews.setAdapter(baseQuickAdapter = new BaseQuickAdapter<InformationModel, BaseViewHolder>(R.layout.layout_home_discover, info) {
            @Override
            protected void convert(BaseViewHolder helper, InformationModel item) {
                if (helper.getLayoutPosition() == 0) {
                    helper.setGone(R.id.top_item, false)
                            .setGone(R.id.normal_item, true)
                            .setText(R.id.news_title, item.getTitle())
                            .setText(R.id.news_from, item.getFromSource() == null ? "" : item.getFromSource())
                            .setText(R.id.publish_date, item.getPublishDate());

                    Glide.with(mContext)
                            .load(item.getImg())
                            .into((ImageView) helper.getView(R.id.news_image));
                } else {
                    helper.setGone(R.id.normal_item, false)
                            .setGone(R.id.top_item, true)
                            .setText(R.id.top_title, item.getTitle());

                    Glide.with(mContext)
                            .load(item.getImg())
                            .into((ImageView) helper.getView(R.id.top_image));
                }

            }
        });

        //获取标题栏高度
        ViewTreeObserver viewTreeObserver = mBanner.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBanner.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int mHeight = mBanner.getHeight();//这里取的高度应该为图片的高度-标题栏
                //注册滑动监听
                mObservableScrollView.setOnObservableScrollViewListener((l1, t1, l2, t2) -> {
                    if (t1 <= 0) {
                        //顶部图处于最顶部，标题栏透明
                        mHeaderContent.setBackgroundColor(Color.argb(0, 0, 186, 156));
                        mTitle.setTextColor(Color.argb(0, 255, 255, 255));
                    } else if (t1 < mHeight) {
                        //滑动过程中，渐变
                        float scale = (float) t1 / mHeight;//算出滑动距离比例
                        float alpha =(255 * scale);//得到透明度
                        mHeaderContent.setBackgroundColor(Color.argb((int) alpha, 0, 186, 156));
                        mTitle.setTextColor(Color.argb((int) alpha, 255, 255, 255));
                    } else {
                        //过顶部图区域，标题栏定色
                        mHeaderContent.setBackgroundColor(Color.argb(255, 0, 186, 156));
                        mTitle.setTextColor(Color.argb(255, 255, 255, 255));
                    }
                });
            }
        });
    }

    @Override
    protected void setListener() {

//        preferential.setOnClickListener(view -> {
//            if (!FastClickUtils.Companion.isValidClick()) {
//                Intent intent = new Intent();
//                intent.setClass(mActivity, HandleWebActivity.class)
//                        .putExtra("title", "优待证办理")
//                        .putExtra("url", "http://www.zjzwfw.gov.cn/zjservice/item/detail/index.do?impleCode=ff8080815df81d19015df864b93500694330800761000&webId=24");
//                startActivity(intent);
//            }
//        });

        baseQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (!FastClickUtils.Companion.isValidClick()) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("title", info.get(position).getTitle())
                        .putExtra("url", info.get(position).getContent())
                        .putExtra("id", info.get(position).getId());
                startActivity(intent);
            }
        });

        mMore.setOnClickListener(view -> {
            if (!FastClickUtils.Companion.isValidClick()) {
                startActivity(new Intent(getContext(), TabActivity.class));
            }
        });

    }

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        mBanner.startTurning();
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        mBanner.stopTurning();
    }

    @Override
    protected void loadData() {

        getService();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            StatusBarUtils.setTranslucentStatusBar(mActivity, true);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            getService();
        }

    }

    /**
     * 获取轮播图列表
     */
    private void getBannerList() {

        MyApplication.api.getInfoList("BANNER",1,6)
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
                        LoadingBar.dialog(mActivity).cancel();
                        showLongToast("获取轮播图失败," + e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody body) {

                        LoadingBar.dialog(mActivity).cancel();
                        try {

                            String json = body.string();
                            ResponseBean rawBean = new Gson().fromJson(json, ResponseBean.class);

                            if (rawBean.isStatus()) {
                                ResponseBean<BaseListBean<InformationModel>> bean = new Gson().fromJson(json, new TypeToken<ResponseBean<BaseListBean<InformationModel>>>() {}.getType());

                                BaseListBean<InformationModel> list = bean.getData();
                                List<InformationModel> bannerList = list.getRows();

                                for (InformationModel info: bannerList) {
                                    networkImages.add(info.getImg());
                                }

                                mBanner.setPages(new CBViewHolderCreator() {
                                    @Override
                                    public NetworkImageHolderView createHolder(View itemView) {
                                        return new NetworkImageHolderView(itemView, mActivity);
                                    }

                                    @Override
                                    public int getLayoutId() {
                                        return R.layout.item_networkimage;
                                    }
                                },networkImages)
                                        .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                                        .setOnItemClickListener(position -> {
                                            Intent intent = new Intent();
                                            intent.setClass(mActivity, WebActivity.class)
                                                    .putExtra("title", bannerList.get(position).getTitle())
                                                    .putExtra("url", bannerList.get(position).getContent())
                                                    .putExtra("id", bannerList.get(position).getId());
                                            startActivity(intent);
                                            Logger.i("点击了第"+position+"个");
                                        });

                            } else if ("10009".equals(rawBean.getCode())) {
                                Intent intent = new Intent(mActivity, LoginActivity.class);
                                intent.putExtra(ConstUtils.LOGIN_TOKEN, true);
                                startActivity(intent);
                                showLongToast("登录过期，请重新登录");
                            } else {
                                showLongToast("获取轮播图失败," + rawBean.getMsg());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

    /**
     * 获取服务列表
     */
    private void getService() {
        LoadingBar.dialog(mActivity).extras(new Object[]{"信息获取中.."}).show();
//        String token = MyApplication.mSp.getStr(ConstUtils.USER_TOKEN);

        MyApplication.api.getInfoList("DISCOVERY",1, 8)
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
                        LoadingBar.dialog(mActivity).cancel();
                        Logger.e("==onError==" + e.getMessage());
                        showLongToast("获取服务信息失败," + e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        LoadingBar.dialog(mActivity).cancel();
                        try {

                            String json = body.string();
                            ResponseBean rawBean = new Gson().fromJson(json, ResponseBean.class);

                            if (rawBean.isStatus()) {
                                ResponseBean<List<SubModel>> bean = new Gson().fromJson(json, new TypeToken<ResponseBean<List<SubModel>>>() {}.getType());

                                List<SubModel> model = bean.getData();

                            } else if ("10009".equals(rawBean.getCode())) {
                                Intent intent = new Intent(mActivity, LoginActivity.class);
                                intent.putExtra(ConstUtils.LOGIN_TOKEN, true);
                                startActivityForResult(intent, 250);
                                showLongToast("登录过期，请重新登录");
                            } else {
                                showLongToast("获取服务信息失败！" + rawBean.getMsg());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

    /**
     * 获取资讯列表
     */
    private void getInfoList(int poi) {
        LoadingBar.dialog(mActivity).extras(new Object[]{"资讯获取中.."}).show();
//        String token = MyApplication.mSp.getStr(ConstUtils.USER_TOKEN);

        MyApplication.api.getInfoList("FRONTDT",1,4)
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
                        LoadingBar.dialog(mActivity).cancel();
                        Logger.e("==onError==" + e.getMessage());
                        showLongToast("获取资讯信息失败," + e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        LoadingBar.dialog(mActivity).cancel();
                        try {

                            String json = body.string();
                            ResponseBean rawBean = new Gson().fromJson(json, ResponseBean.class);

                            if (rawBean.isStatus()) {
                                ResponseBean<BaseListBean<InformationModel>> bean = new Gson().fromJson(json, new TypeToken<ResponseBean<BaseListBean<InformationModel>>>() {}.getType());

                                BaseListBean<InformationModel> list = bean.getData();
                                info = list.getRows();

                                baseQuickAdapter.setNewData(info);

                            } else if ("10009".equals(rawBean.getCode())) {
                                Intent intent = new Intent(mActivity, LoginActivity.class);
                                intent.putExtra(ConstUtils.LOGIN_TOKEN, true);
                                startActivityForResult(intent, 250);
                                showLongToast("登录过期，请重新登录");
                            } else {
                                showLongToast("获取资讯信息失败！" + rawBean.getMsg());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }
}
