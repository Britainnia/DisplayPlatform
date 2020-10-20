package com.kira.mypublishplatform.ui.web

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.databinding.ActivityWebBinding
import com.kira.mypublishplatform.utils.Logger
//import kotlinx.android.synthetic.main.activity_web.*

/**
 * 培训的H5网页加载界面
 */
class WebActivity : BaseActivity<ActivityWebBinding>() {
//    private var mTvTitle: TextView? = null
//    private var mWebView: WebView? = null
    private var mUrl: String? = null
    private var mTitle: String? = null
    private var mId = 0

//    override fun loadXml() {
//        setContentView(R.layout.activity_web)
//    }

    override fun getIntentData(savedInstanceState: Bundle?) {
        mUrl = intent.getStringExtra("url")
        val isCrossScreen = intent.getBooleanExtra("isCrossScreen", false)
        mTitle = intent.getStringExtra("title")
        mId = intent.getIntExtra("id", 0)
//        mDuration = getIntent().getIntExtra("duration",300);
//        startTime = System.currentTimeMillis();
        if (!isCrossScreen) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun initData() { //mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        binding.tvTitle.text = mTitle
        binding.tvTitle.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding.tvTitle.isSingleLine = true
        binding.tvTitle.isSelected = true
        binding.tvTitle.isFocusable = true
        binding.tvTitle.isFocusableInTouchMode = true
        
        val webSettings = binding.webView.settings
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
        webSettings.mediaPlaybackRequiresUserGesture = false
        webSettings.pluginState = WebSettings.PluginState.ON
        webSettings.allowFileAccess = true
        webSettings.loadWithOverviewMode = true
        webSettings.databaseEnabled = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.defaultTextEncodingName = "UTF-8"
        webSettings.textZoom = 300
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        //            webSettings.setMediaPlaybackRequiresUserGesture(false);
//        }
//        mWebView.setInitialScale(70);
        //图片宽度改为100%  高度为自适应
        val varjs =
            "<script type='text/javascript'> \nwindow.onload = function()\n{var \$img = document.getElementsByTagName('img');for(var p in  \$img){\$img[p].style.width = '100%'; \$img[p].style.height ='auto'}}</script>"
        binding.webView.loadData(varjs + mUrl, "text/html", "UTF-8")
//        if (mId != 0)
//            mWebView.loadUrl("http://192.168.1.100:9527/#/zx-app?id=" + mId);
//        else
//            showShortToast("未找到资源");
    }

    override fun setListener() {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
                Logger.i("=======url======$url")
                if (url.endsWith("www.baidu.com") || url.endsWith("www.baidu.com/")) {
                    toAnswer()
                }
                view.loadUrl(url)
                return true
            }

//            override fun onPageFinished(view: WebView, url: String) {
//                super.onPageFinished(view, url)
//                if (view.progress == 100) {
//
//                    val audioJs = "javascript: var v = document.getElementsByTagName('audio'); v[0].play();"
//                    view.loadUrl(audioJs)
//
//                    val loginJs = "javascript: control.init()"
//                    view.loadUrl(loginJs)
//
//                }
//            }
        }

        binding.ivBack.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                finish()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再destory()
        binding.root.removeView(binding.webView)
        binding.webView.stopLoading()
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        binding.webView.settings.javaScriptEnabled = false
        binding.webView.clearHistory()
        binding.webView.clearView()
        binding.webView.removeAllViews()
        binding.webView.destroy()
        super.onDestroy()
    }

    /**
     * 去答题
     */
    private fun toAnswer() {
        showShortToast("访问百度")
    }

    override fun onPause() {
        super.onPause()
        binding.webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }
}