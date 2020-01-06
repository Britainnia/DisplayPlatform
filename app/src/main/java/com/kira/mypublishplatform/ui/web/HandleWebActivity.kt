package com.kira.mypublishplatform.ui.web

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.utils.Logger
import kotlinx.android.synthetic.main.activity_web.*

/**
 * 培训的H5网页加载界面
 */
class HandleWebActivity : BaseActivity() {

    private var mUrl: String? = null
    private var mTitle: String? = null
    override fun loadXml() {
        setContentView(R.layout.activity_web)
    }

    override fun getIntentData(savedInstanceState: Bundle?) {
        mUrl = intent.getStringExtra("url")
        val isCrossScreen = intent.getBooleanExtra("isCrossScreen", false)
        mTitle = intent.getStringExtra("title")
        if (!isCrossScreen) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun initView() {

    }

    override fun initData() {
        webView!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        tv_title.text = mTitle
        if (tv_title != null) {
            tv_title.ellipsize = TextUtils.TruncateAt.MARQUEE
            tv_title.isSingleLine = true
            tv_title.isSelected = true
            tv_title.isFocusable = true
            tv_title.isFocusableInTouchMode = true
        }
        val webSettings = webView!!.settings
        //webview的缓存模式
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        // 设置WebView属性，能够执行JavaScript脚本
        webSettings.javaScriptEnabled = true
        // 设置可以支持缩放
        webSettings.setSupportZoom(true)
        // 设置出现缩放工具
        webSettings.builtInZoomControls = true
        // 隐藏缩放按钮
//        webSettings.setDisplayZoomControls(false);
// 为图片添加放大缩小功能
        webSettings.useWideViewPort = true
        webSettings.domStorageEnabled = true
        webSettings.pluginState = WebSettings.PluginState.ON
        webSettings.allowFileAccess = true
        webSettings.loadWithOverviewMode = true
        webSettings.databaseEnabled = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.defaultTextEncodingName = "UTF-8"
        //        webSettings.setTextZoom(300);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webSettings.mediaPlaybackRequiresUserGesture = false
        //        }
//        mWebView.setInitialScale(70);
        webView.loadUrl(mUrl)
    }

    override fun setListener() {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
                Logger.i("=======url======$url")
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
            }
        }
    }

    fun back(view: View?) {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        if (webView != null) { // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再destory()
            if (root != null) root!!.removeView(webView)
            webView.stopLoading()
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.settings.javaScriptEnabled = false
            webView.clearHistory()
            webView.clearView()
            webView.removeAllViews()
            webView.destroy()
        }
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }
}