package com.kira.mypublishplatform.base

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Process
//import androidx.multidex.MultiDex
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.kira.mypublishplatform.api.DisApi
import com.kira.mypublishplatform.utils.ConstUtils

import com.kira.mypublishplatform.utils.Logger
import com.kira.mypublishplatform.utils.NotificationUtils
import com.kira.mypublishplatform.utils.SharedPreferencesUtils
import okhttp3.OkHttpClient
import org.xutils.x
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rxhttp.wrapper.param.RxHttp

import java.util.*
import java.util.concurrent.TimeUnit

/**
 * app全局类
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        myApp = this
        mContext = applicationContext
        Logger.i("===========onCreate=============")
        mSp = SharedPreferencesUtils(this)
//        mNo = NotificationUtils(this)
        isDebug = isApkInDebug(mContext)

        initDisplay() //初始屏幕信息
//        initImageLoader(this) //初始图片加载器
//        initImagePicker(); //初始图片选择器
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            closeAndroidPDialog()
        }
        initApi() //初始网络请求Api
        if (isMainProcess) {
            //初始化XUtils3
            x.Ext.setDebug(isDebug)
            x.Ext.init(this)
        }

        //初始化通知频道（Android 8.0以上要求）

    }

//    override fun attachBaseContext(base: Context) {
//        super.attachBaseContext(base)
//        MultiDex.install(this)
//    }

    private fun closeAndroidPDialog() {
        try {
            val aClass =
                Class.forName("android.content.pm.PackageParser\$Package")
            val declaredConstructor =
                aClass.getDeclaredConstructor(
                    String::class.java
                )
            declaredConstructor.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val cls =
                Class.forName("android.app.ActivityThread")
            val declaredMethod =
                cls.getDeclaredMethod("currentActivityThread")
            declaredMethod.isAccessible = true
            val activityThread = declaredMethod.invoke(null)
            val mHiddenApiWarningShown =
                cls.getDeclaredField("mHiddenApiWarningShown")
            mHiddenApiWarningShown.isAccessible = true
            mHiddenApiWarningShown.setBoolean(activityThread, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 初始化网络请求API
     */
    private fun initApi() {
        if (mRetrofit == null) {
            val client = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()

            mRetrofit = Retrofit.Builder()
                .baseUrl(ConstUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //支持Gson解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //支持RXJava
                .client(client)
                .build()

            api = mRetrofit!!.create(DisApi::class.java)

            RxHttp.init(client, isDebug)
        }
    }

    /**
     * 初始化屏幕的宽高信息
     */
    private fun initDisplay() { //初始化DisplayMetrics对象
        val displayMetrics = mContext.resources.displayMetrics
        mDisplayHeight = displayMetrics.heightPixels
        mDisplayWidth = displayMetrics.widthPixels
        Logger.i("屏幕宽:$mDisplayWidth")
        Logger.i("屏幕高:$mDisplayHeight")
    }

    /**
     * 初始化图片选择器
     */
//    private fun initImagePicker() {
//        ImagePicker imagePicker = ImagePicker.getInstance();
//        imagePicker.setMultiMode(true);
//        imagePicker.setImageLoader(new UILImageLoader());     //设置图片加载器
//        imagePicker.setShowCamera(true);                      //显示拍照按钮
//        imagePicker.setCrop(false);                            //允许裁剪（单选才有效）
//        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
//        imagePicker.setSelectLimit(9);                        //选中数量限制
//        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
//        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
//    }

    /**
     * 判断是否运行于主线程上
     */
    private val isMainProcess: Boolean
        get() {
            val pid = Process.myPid()
            val activityManager =
                getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (appProcess in activityManager.runningAppProcesses) {
                if (appProcess.pid == pid) {
                    return applicationInfo.packageName == appProcess.processName
                }
            }
            return false
        }

    companion object {
        lateinit var myApp: Application
        /**
         * 日志的开关，false:不打印log; true:打印log;
         */
        var isDebug = true
        /**
         * 屏幕的宽度
         */
        var mDisplayWidth = 0
        /**
         * 屏幕的高度
         */
        var mDisplayHeight = 0
        /**
         * 全局 Retrofit
         */
        var mRetrofit: Retrofit? = null
        /**
         * 全局 API
         */
        lateinit var api: DisApi
        /**
         *
         */
        lateinit var mSp: SharedPreferencesUtils
        /**
         *
         */
        lateinit var mNo: NotificationUtils
        /**
         * 全局上下文
         */
        lateinit var mContext: Context

        var isExit = false
        var activityList: MutableList<Activity?>? = LinkedList()

        /**
         * 初始化ImageLoader
         */
//        fun initImageLoader(context: Context?) { //缓存文件的目录
//            val cacheDir: File =
//                StorageUtils.getOwnCacheDirectory(context, "HomeCare/Cache")
//            val config: ImageLoaderConfiguration = Builder(context)
//                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
//                .threadPoolSize(4)
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
//                .memoryCache(UsingFreqLimitedMemoryCache(4 * 1024 * 1024))
//                .memoryCacheSize(5 * 1024 * 1024) // 内存缓存的最大值
//                .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .diskCache(UnlimitedDiskCache(cacheDir)) //自定义缓存路径
//                .imageDownloader(BaseImageDownloader(context, 5 * 1000, 30 * 1000))
//                .writeDebugLogs()
//                .build()
//            //全局初始化此配置
//            com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config)
//        }

        /**
         * 添加Activity到集合中
         */
        fun addActivity(activity: Activity?) {
            if (activity != null) activityList!!.add(activity)
        }

        /**
         * 关闭指定的Activity
         */
        fun finishActivity(activity: Activity?) {
            if (activityList != null && activity != null && activityList!!.contains(
                    activity
                )
            ) { //使用迭代器安全删除
                val it =
                    activityList!!.iterator()
                while (it.hasNext()) {
                    val temp = it.next()
                    // 清理掉已经释放的activity
                    if (temp == null) {
                        it.remove()
                        continue
                    }
                    if (temp === activity) {
                        it.remove()
                    }
                }
                //activity.finish();
            }
        }

        /**
         * 关闭指定类名的Activity
         */
        fun finishActivity(cls: Class<*>) {
            if (activityList != null) { // 使用迭代器安全删除
                val it =
                    activityList!!.iterator()
                while (it.hasNext()) {
                    val activity = it.next()
                    // 清理掉已经释放的activity
                    if (activity == null) {
                        it.remove()
                        continue
                    }
                    if (activity.javaClass == cls) {
                        it.remove()
                        activity.finish()
                    }
                }
            }
        }

        fun exit() {
            for (activity in activityList!!) {
                activity!!.finish()
            }
        }

        /**
         * 判断当前应用是否是debug状态
         */
        fun isApkInDebug(context: Context): Boolean {
            return try {
                val info = context.applicationInfo
                info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            } catch (e: java.lang.Exception) {
                false
            }
        }

        /**
         * 相关程序 APPID 与 APPKEY
         */

        const val BuglyId = "de225e6f21"

        const val WeChatId = "wx63fcbd5c581b6bb7"
        //WeChatSercet = "034998f39070b6e6eaef4152c7134469"


    }
}