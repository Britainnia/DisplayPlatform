package com.kira.mypublishplatform.ui.web

import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.databinding.ActivityWebBinding
import com.kira.mypublishplatform.model.ShareModel
import com.kira.mypublishplatform.utils.FastClickUtils.Companion.isValidClick
import com.kira.mypublishplatform.utils.Logger
import com.kira.mypublishplatform.view.ShareDialogFragment
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.util.*

class HandleWebActivity : BaseActivity<ActivityWebBinding>() {

    private var mUrl: String? = null
    private var mTitle: String? = null
    private var wxApi: IWXAPI? = null

    private var mShareList: MutableList<ShareModel> = mutableListOf()
    private var sg: ShareDialogFragment? = null

    override fun getIntentData(savedInstanceState: Bundle?) {
        mUrl = intent.getStringExtra("url")
        val isCrossScreen = intent.getBooleanExtra("isCrossScreen", false)
        mTitle = intent.getStringExtra("title")
        if (!isCrossScreen) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun initData() {
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

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
        webSettings.pluginState = WebSettings.PluginState.ON
        webSettings.allowFileAccess = true
        webSettings.loadWithOverviewMode = true
        webSettings.databaseEnabled = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.defaultTextEncodingName = "UTF-8"
//        webSettings.textZoom = 300;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webSettings.mediaPlaybackRequiresUserGesture = false
//        }
//        mWebView.setInitialScale(70);
        binding.webView.loadUrl(mUrl)

        wxApi = WXAPIFactory.createWXAPI(this, MyApplication.WeChatId)
        wxApi?.registerApp(MyApplication.WeChatId)

        initList()
    }

    override fun setListener() {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
                Logger.i("=======url======$url")
                view.loadUrl(url)
                return true
            }

        }

        binding.tvQuestion.setOnClickListener{
            if (!isValidClick) {

                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                Objects.requireNonNull(imm)
                    .hideSoftInputFromWindow(it.windowToken, 0) //强制隐藏键盘

                if (sg != null)
                    sg!!.show(supportFragmentManager, "share")
            }
//            sendUrlToWX(1, mUrl, mTitle, null)
        }


//        addressReadOnly.setOnClickListener(view -> showLongToast("抱歉，如需修改请您前往当前所填的社区"));
        sg!!.setCallback(object : ShareDialogFragment.Callback {
            override fun onItemClick(choice: Int) {
                sg!!.dismiss()
                sendUrlToWX(choice, mUrl, mTitle, null)
            }
        })

        binding.ivBack.setOnClickListener {
//            if (binding.webView.canGoBack()) {
//                binding.webView.goBack()
//            } else {
                finish()
//            }
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

    override fun onPause() {
        super.onPause()
        binding.webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    private fun initList() {

        mShareList.clear()

        mShareList.add(
            ShareModel(
                "朋友圈",
                "file:///android_asset/moments.png",
                ""
            )
        )

        mShareList.add(
            ShareModel(
                "微信",
                "file:///android_asset/wechat.png",
                ""
            )
        )

        sg = ShareDialogFragment(mShareList, baseContext)

    }

    /**
     * 分享链接到微信朋友圈
     */
    private fun sendUrlToWX(flag: Int?, url: String?, title: String?, description: String?) {
        val webpageObject = WXWebpageObject()
        webpageObject.webpageUrl = url
        val msg = WXMediaMessage(webpageObject)
        msg.title = title
        msg.description = description
        val thumb = BitmapFactory.decodeResource(resources, R.mipmap.logo)
        msg.setThumbImage(thumb)
        val req = SendMessageToWX.Req()
        req.transaction = System.currentTimeMillis().toString()
        req.message = msg //发送到朋友圈 flag = 0               //发送给微信好友  flag = 1
        req.scene =
            if (flag == 0) SendMessageToWX.Req.WXSceneTimeline
            else SendMessageToWX.Req.WXSceneSession
        //        req.scene = SendMessageToWX.Req.WXSceneSession;
        wxApi?.sendReq(req)
    }
}