package com.kira.mypublishplatform.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.dyhdyh.widget.loadingbar2.LoadingBar;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kira.mypublishplatform.R;
import com.kira.mypublishplatform.adapter.TabRecyclerAdapter;
import com.kira.mypublishplatform.base.MyApplication;
import com.kira.mypublishplatform.bean.AppBean;
import com.kira.mypublishplatform.bean.BaseListBean;
import com.kira.mypublishplatform.bean.ResponseBean;
import com.kira.mypublishplatform.model.InformationModel;
import com.kira.mypublishplatform.model.MultipleItem;
import com.kira.mypublishplatform.utils.ConstUtils;
import com.kira.mypublishplatform.utils.Logger;
import com.kira.mypublishplatform.utils.StatusBarUtils;
import com.kira.mypublishplatform.utils.ToastUtils;
import com.kira.mypublishplatform.view.ObservableScrollView;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class TabActivity extends AppCompatActivity {

//    private CommonTabLayout mTabLayout;
    private TabLayout mTabLayout;
    private RecyclerView mRecyclerView;
//    private ObservableScrollView mObservableScrollView;
    private Context mContext = TabActivity.this;
    private LinearLayoutManager mManager;
    private TabRecyclerAdapter mAdapter;
    private List<MultipleItem> tabList = new ArrayList<>();
    private List<String> networkImages = new ArrayList<>();
//    private int mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab);

        mTabLayout = findViewById(R.id.tab_layout);
        mRecyclerView =  findViewById(R.id.recycler_view);

//        mAdapter = new TabRecyclerAdapter(tabList);
//        mRecyclerView.setAdapter(mAdapter);

        initTab();
        mManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mManager);
//        for (String s : titles) {
//            MultipleItem item = new MultipleItem(MultipleItem.ITEM);
//            tabList.add(item);
//        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRecyclerView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                //滑动RecyclerView list的时候，根据最上面一个Item的position来切换tab
                mTabLayout.setScrollPosition(mManager.findFirstVisibleItemPosition(), 0, true);
//                mTabLayout.setCurrentTab(mManager.findFirstVisibleItemPosition());
            });
        }

        getAPP();

//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });

//        mTabLayout.setIndicatorColor(ContextCompat.getColor(this, R.color.bs_blue));
//        mTabLayout.setTextSelectColor(ContextCompat.getColor(this, R.color.bs_blue));
//        mTabLayout.setTextUnselectColor(ContextCompat.getColor(this, R.color.black_overlay));

//        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.bs_blue));
//        mTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.black_overlay), ContextCompat.getColor(this, R.color.bs_blue));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //点击tab的时候，RecyclerView自动滑到该tab对应的item位置
                mManager.scrollToPositionWithOffset(tab.getPosition(), 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mManager.scrollToPositionWithOffset(tab.getPosition(), 0);
            }
        });


    }

    private void initTab() {
        mTabLayout.removeAllTabs();
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (int i = 0; i < tabList.size(); i++)
            mTabLayout.addTab(mTabLayout.newTab().setText(tabList.get(i).getType()).setTag(i));
    }

    /**
     * 获取应用
     */
    private void getAPP() {

        LoadingBar.dialog(mContext).extras(new Object[]{"waiting...."}).show();

        MyApplication.api.getApps()
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

                        LoadingBar.dialog(mContext).cancel();
                        Logger.e("==onError==" + e.getMessage());

                        ToastUtils.showLong(mContext,"获取应用失败," + e.getMessage());

                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        LoadingBar.dialog(mContext).cancel();
                        try {

                            String json = body.string();
                            ResponseBean rawBean = new Gson().fromJson(json, ResponseBean.class);
                            if (rawBean.isStatus()) {

                                ResponseBean<AppBean> bean = new Gson().fromJson(json, new TypeToken<ResponseBean<AppBean>>() {}.getType());
                                AppBean app = bean.getData();
                                tabList.addAll(app.getApps());
//                                tabList.addAll(app.getApps());
                                initTab();

//                                mAdapter.setNewData(tabList);
                                mAdapter = new TabRecyclerAdapter(tabList);
                                mRecyclerView.setAdapter(mAdapter);

                            } else {
                                LoadingBar.dialog(mContext).cancel();
                                ToastUtils.showLong(mContext,"获取应用失败," + rawBean.getMsg());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

}
