package com.kira.mypublishplatform.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import android.widget.RadioButton

import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.databinding.ActivityHeadBinding
import com.kira.mypublishplatform.ui.discover.DiscoverFragment
import com.kira.mypublishplatform.ui.head.HeadFragment
import com.kira.mypublishplatform.ui.person.PersonFragment
import com.kira.mypublishplatform.ui.service.TabFragment
import com.kira.mypublishplatform.ui.web.HandleWebActivity
import com.kira.mypublishplatform.utils.ConstUtils
import com.kira.mypublishplatform.utils.Logger
import com.kira.mypublishplatform.utils.NotificationUtils
import com.kira.mypublishplatform.view.TitleDialog

class HeadActivity : BaseActivity<ActivityHeadBinding>() {

    private var lastIndex = -1
    private var mExitTime: Long = 0

    // 存放碎片对象
    private var fmList: MutableList<Fragment>? = arrayListOf()
    // 存放加载后的碎片对象
    private var fmSet: MutableSet<Fragment>? = hashSetOf()

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
//        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun getIntentData(savedInstanceState: Bundle?) {}

    override fun initData() {
//        permission
        fmList?.add(HeadFragment.newInstance())
        fmList?.add(TabFragment.newInstance())
        fmList?.add(DiscoverFragment.newInstance())
        fmList?.add(PersonFragment.newInstance())
//        binding.navView.itemIconTintList = null

        permission
        notification

//        shiftNavigation(R.id.navigation_home)
//        shiftNavigation(R.id.home_tab)
        initHeadUI()

    }

    override fun setListener() {
//        binding.navView.setOnNavigationItemSelectedListener { item: MenuItem ->
//            when (item.itemId) {
//                R.id.navigation_home -> {
//                    resetToDefaultIcon() //重置到默认不选中图片
//                    item.setIcon(R.mipmap.icon_home_selected)
//                    replaceFragment(0)
//                }
//                R.id.navigation_dashboard -> {
//                    resetToDefaultIcon() //重置到默认不选中图片
//                    item.setIcon(R.mipmap.icon_discover_selected)
//                    replaceFragment(2)
//                }
//                R.id.navigation_mine -> {
//                    if (MyApplication.mSp.getBoolean(ConstUtils.LOGIN_STATE)) {
//                        resetToDefaultIcon() //重置到默认不选中图片
//                        item.setIcon(R.mipmap.icon_me_selected)
//                        replaceFragment(3)
//                    } else {
//                        startActivityForResult(
//                            Intent(
//                                this@HeadActivity,
//                                LoginActivity::class.java
//                            ),
//                            REQUEST_LOGIN
//                        )
//                        return@setOnNavigationItemSelectedListener false
//                    }
//                }
//                R.id.navigation_main -> {
////                    startActivity(
////                        Intent(
////                            this@HeadActivity,
////                            TabActivity::class.java
////                        )
////                    )
////                    return@setOnNavigationItemSelectedListener false
//                    resetToDefaultIcon() //重置到默认不选中图片
//                    item.setIcon(R.mipmap.icon_service_selected)
//                    replaceFragment(1)
//                }
//                else -> return@setOnNavigationItemSelectedListener false
//            }
//            true
//        }


        binding.tabsRg.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.home_tab -> {
                    resetToDefaultIcon() //重置到默认不选中图片
                    val icon = resources.getDrawable(R.mipmap.icon_home_selected)
                    icon.setBounds(0, 0, 50, 50)
                    binding.homeTab.setCompoundDrawables(null, icon, null, null)
                    replaceFragment(0)
                }
                R.id.service_tab -> {
                    resetToDefaultIcon() //重置到默认不选中图片
                    val icon = resources.getDrawable(R.mipmap.icon_service_selected)
                    icon.setBounds(0, 0, 50, 50)
                    binding.serviceTab.setCompoundDrawables(null, icon, null, null)
                    replaceFragment(1)
                }
                R.id.discover_tab -> {
                    resetToDefaultIcon() //重置到默认不选中图片
                    val icon = resources.getDrawable(R.mipmap.icon_discover_selected)
                    icon.setBounds(0, 0, 50, 50)
                    binding.discoverTab.setCompoundDrawables(null, icon, null, null)
                    replaceFragment(2)
                }
                R.id.me_tab -> {
                    if (MyApplication.mSp.getBoolean(ConstUtils.LOGIN_STATE)) {
                        resetToDefaultIcon() //重置到默认不选中图片
                        val icon = resources.getDrawable(R.mipmap.icon_me_selected)
                        icon.setBounds(0, 0, 50, 50)
                        binding.meTab.setCompoundDrawables(null, icon, null, null)
                        replaceFragment(3)
                    } else {
                        startActivityForResult(
                            Intent(
                                this@HeadActivity,
                                LoginActivity::class.java
                            ),
                            REQUEST_LOGIN
                        )
                    }
                }
                else -> {
                    resetToDefaultIcon()
                }
            }
        }

        binding.gbiccTab.setOnClickListener {
//            binding.tabsRg.clearCheck()
//            resetToDefaultIcon()
            val intent = Intent(baseContext, HandleWebActivity::class.java)
            intent.putExtra("title", "公司官网")
                .putExtra("url", "https://www.gbicc.net/")
            startActivity(intent)
        }
    }

    fun shiftNavigation(@IdRes id: Int) {
//        binding.navView.selectedItemId = id
//        binding.tabsRg.check(id)
        val check: RadioButton = findViewById(id)
        check.isChecked = true
    }

    private fun initHeadUI() {

        val home = resources.getDrawable(R.mipmap.icon_home_selected)
        val service = resources.getDrawable(R.mipmap.icon_service_normal)
        val discover = resources.getDrawable(R.mipmap.icon_discover_normal)
        val me = resources.getDrawable(R.mipmap.icon_me_normal)
        //setBounds(left,top,right,bottom)里的参数从左到右分别是
        //drawable的左边到textview左边缘+padding的距离，drawable的上边离textview上边缘+padding的距离
        //drawable的右边边离textview左边缘+padding的距离，drawable的下边离textview上边缘+padding的距离
        //所以right-left = drawable的宽，top - bottom = drawable的高
        home.setBounds(0, 0, 50, 50)
        service.setBounds(0, 0, 50, 50)
        discover.setBounds(0, 0, 50, 50)
        me.setBounds(0, 0, 50, 50)
        binding.homeTab.setCompoundDrawables(null, home, null, null)
        binding.serviceTab.setCompoundDrawables(null, service, null, null)
        binding.discoverTab.setCompoundDrawables(null, discover, null, null)
        binding.meTab.setCompoundDrawables(null, me, null, null)
        replaceFragment(0)

    }

    private fun resetToDefaultIcon() {
//        val receive =
//            binding.navView.menu.findItem(R.id.navigation_home)
//        val main =
//            binding.navView.menu.findItem(R.id.navigation_main)
//        val order =
//            binding.navView.menu.findItem(R.id.navigation_dashboard)
//        val mine =
//            binding.navView.menu.findItem(R.id.navigation_mine)
//        receive.setIcon(R.mipmap.icon_home_normal)
//        main.setIcon(R.mipmap.icon_service_normal)
//        order.setIcon(R.mipmap.icon_discover_normal)
//        mine.setIcon(R.mipmap.icon_me_normal)

        val home = resources.getDrawable(R.mipmap.icon_home_normal)
        val service = resources.getDrawable(R.mipmap.icon_service_normal)
        val discover = resources.getDrawable(R.mipmap.icon_discover_normal)
        val me = resources.getDrawable(R.mipmap.icon_me_normal)
        //setBounds(left,top,right,bottom)里的参数从左到右分别是
        //drawable的左边到textview左边缘+padding的距离，drawable的上边离textview上边缘+padding的距离
        //drawable的右边边离textview左边缘+padding的距离，drawable的下边离textview上边缘+padding的距离
        //所以right-left = drawable的宽，top - bottom = drawable的高
        home.setBounds(0, 0, 50, 50)
        service.setBounds(0, 0, 50, 50)
        discover.setBounds(0, 0, 50, 50)
        me.setBounds(0, 0, 50, 50)
        binding.homeTab.setCompoundDrawables(null, home, null, null)
        binding.serviceTab.setCompoundDrawables(null, service, null, null)
        binding.discoverTab.setCompoundDrawables(null, discover, null, null)
        binding.meTab.setCompoundDrawables(null, me, null, null)
    }


    /**
     * 请求权限
     */
    private val permission: Unit
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    this,
                    LOCATION_PERMISSIONS,
                    LOCATION_PERMISSIONS_CODE
                )
            }
        }

    private fun goToOpen() {
        val localIntent = Intent()
        //直接跳转到应用通知设置的代码：
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> { //8.0及以上
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                localIntent.data = Uri.fromParts("package", packageName, null)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> { //5.0以上到8.0以下
                localIntent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                localIntent.putExtra("app_package", packageName)
                localIntent.putExtra("app_uid", applicationInfo.uid)
            }
            else -> //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(localIntent)
    }

    /**
     * 请求通知权限
     */
    private val notification: Unit
        get() {
            if (!NotificationUtils.isNotifyEnabled(this)) {
                val builder: TitleDialog.Builder = TitleDialog.Builder(this)
                builder.setTitle("提示").setMessage("本应用可能使用通知栏，是否开启?")
                    .setPositiveButton("确认") { dialog, _ ->
                        goToOpen()
                        dialog.dismiss()
                    }
                    .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                    .create().show()
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSIONS_CODE) {
            if (grantResults.size == LOCATION_PERMISSIONS.size) {
                when {
                    grantResults[0] != PackageManager.PERMISSION_GRANTED -> {
                        showLongToast("未授予读写权限，无法使用该功能，请开启权限")
                    }
                    grantResults[1] != PackageManager.PERMISSION_GRANTED -> {
                        showLongToast("未授予定位权限，无法使用该功能，请开启权限")
                    }
                    grantResults[2] != PackageManager.PERMISSION_GRANTED -> {
                        showLongToast("未授予拍照权限，无法使用该功能，请开启权限")
                    }
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOGIN -> {
                if (resultCode == RESULT_OK) {
//                    shiftNavigation(R.id.me_tab)
                    resetToDefaultIcon() //重置到默认不选中图片
                    val icon = resources.getDrawable(R.mipmap.icon_me_selected)
                    icon.setBounds(0, 0, 50, 50)
                    binding.meTab.setCompoundDrawables(null, icon, null, null)
                    replaceFragment(3)
                } else {
                    Logger.i("放弃登录")
                    when (lastIndex) {
                        0 -> shiftNavigation(R.id.home_tab)
                        1 -> shiftNavigation(R.id.service_tab)
                        2 -> shiftNavigation(R.id.discover_tab)
                    }
                }
            }

        }
    }

//    private val isGpsOpen: Boolean
//        get() {
//            val lm =
//                getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        }

    /**
     * 显示与隐藏碎片的方法
     */
    private fun replaceFragment(position: Int) {
        if (fmList!!.size <= 0) return
        if (position == lastIndex) return
        // 获取点击事件的碎片对象
        val f = fmList!![position]
        //
        val ft =
            supportFragmentManager.beginTransaction()
        // 在点击后，判断这个碎片是否加载过，不点击的话，不加载碎片
        if (!fmSet!!.contains(f)) {
            ft.add(R.id.ll_frameLayout, f)
            fmSet!!.add(f)
        }
        // 遍历set集合，对加载过的碎片全部隐藏
        for (aFmSet in fmSet!!) {
            ft.hide(aFmSet)
        }
        // 然后单独对点击的碎片进行显示
        ft.show(f)
        ft.commit()
        lastIndex = position
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            exit()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            showShortToast("再按一次退出程序")
            mExitTime = System.currentTimeMillis()
        } else {
            MyApplication.exit()
        }
    }

    companion object {
        //录像需要的权限
        private val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
        )
        private const val LOCATION_PERMISSIONS_CODE = 100
        private const val REQUEST_LOGIN = 10
    }
}