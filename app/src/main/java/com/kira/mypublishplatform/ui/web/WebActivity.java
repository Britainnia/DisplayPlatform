package com.kira.mypublishplatform.ui.web;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kira.mypublishplatform.R;
import com.kira.mypublishplatform.base.BaseActivity;
import com.kira.mypublishplatform.utils.Logger;


/**
 * 培训的H5网页加载界面
 */

public class WebActivity extends BaseActivity {

    private Context context = WebActivity.this;
    private TextView mTvTitle;
    private WebView mWebView;
    private String mUrl;
    private String mTitle;
    private int mId;
    private LinearLayout root;


    @Override
    protected void loadXml() {
        setContentView(R.layout.activity_web);
    }

    @Override
    protected void getIntentData(Bundle savedInstanceState) {
        mUrl = getIntent().getStringExtra("url");
        boolean isCrossScreen = getIntent().getBooleanExtra("isCrossScreen", false);
        mTitle = getIntent().getStringExtra("title");
        mId = getIntent().getIntExtra("id", 0);
//        mDuration = getIntent().getIntExtra("duration",300);
//        startTime = System.currentTimeMillis();

        if (!isCrossScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void initView() {
        root = findViewById(R.id.root);

        mTvTitle = findViewById(R.id.tv_title);

        mWebView = findViewById(R.id.webView);


    }

    @Override
    protected void initData() {
        //mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        mTvTitle.setText(mTitle);

        if (mTvTitle != null) {
            mTvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mTvTitle.setSingleLine(true);
            mTvTitle.setSelected(true);
            mTvTitle.setFocusable(true);
            mTvTitle.setFocusableInTouchMode(true);
        }

        WebSettings webSettings = mWebView.getSettings();

        //webview的缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 设置WebView属性，能够执行JavaScript脚本
        webSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        // 隐藏缩放按钮
//        webSettings.setDisplayZoomControls(false);
        // 为图片添加放大缩小功能
        webSettings.setUseWideViewPort(true);

        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setTextZoom(300);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//            webSettings.setMediaPlaybackRequiresUserGesture(false);
//        }

//        mWebView.setInitialScale(70);

        //图片宽度改为100%  高度为自适应
        String varjs = "<script type='text/javascript'> \nwindow.onload = function()\n{var $img = document.getElementsByTagName('img');for(var p in  $img){$img[p].style.width = '100%'; $img[p].style.height ='auto'}}</script>";
        mWebView.loadData(varjs + mUrl, "text/html", "UTF-8");
//        if (mId != 0)
//            mWebView.loadUrl("http://192.168.1.100:9527/#/zx-app?id=" + mId);
//        else
//            showShortToast("未找到资源");

    }

    @Override
    protected void setListener() {

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.i("=======url======" + url);
                if (url.endsWith("www.baidu.com") || url.endsWith("www.baidu.com/")) {
                    toAnswer();
                }
//                else if (url.endsWith(".pdf")) {
//                    Intent intent = new Intent(WebActivity.this, PdfActivity.class);
//                    intent.putExtra("url", url);
//                    intent.putExtra("isCrossScreen", isCrossScreen);
//                    intent.putExtra("title", mTitle);
//                    intent.putExtra("id", mId);
//                    intent.putExtra("duration", mDuration);
//                    intent.putExtra("isLargest", false);
//                    startActivity(intent);
//                    mWebView.loadUrl(url);
//                }
//                else if (url.endsWith(".html"))
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                if (view.getProgress() == 100) {
//
//                    String audioJs = "javascript: var v = document.getElementsByTagName('audio'); v[0].play();";
//                    view.loadUrl(audioJs);
//
//                    String loginJs = "javascript: control.init()";
//                    view.loadUrl(loginJs);
//
//                }
            }
        });

    }

    public void back(View view) {
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        if(mWebView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再destory()

            if (root != null)
                root.removeView(mWebView);

            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();

        }
        super.onDestroy();
    }

    /**
     * 去答题
     */
    private void toAnswer() {
        showShortToast("访问百度");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

}
