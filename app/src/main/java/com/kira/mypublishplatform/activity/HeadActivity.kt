package com.kira.mypublishplatform.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.activity.HeadActivity
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.ui.dashboard.DashboardFragment
import com.kira.mypublishplatform.ui.home.HomeFragment
import com.kira.mypublishplatform.ui.notifications.NotificationsFragment
import kotlinx.android.synthetic.main.activity_head.*
import java.util.*
import kotlin.collections.ArrayList

class HeadActivity : BaseActivity() {
//    private var mBottomNavigationView: BottomNavigationView? = null
    private var lastIndex = -1
    private var mExitTime: Long = 0
    // 存放碎片对象
    private var fmList: MutableList<Fragment>? = ArrayList()
    // 存放加载后的碎片对象
    private var fmSet: MutableSet<Fragment>? = HashSet()

    override fun loadXml() {
        setContentView(R.layout.activity_head)
    }

    override fun getIntentData(savedInstanceState: Bundle?) {}
    override fun initView() {
//        mBottomNavigationView = findViewById(R.id.nav_view)
    }

    override fun initData() {
        permission
        
        fmList?.add(HomeFragment.newInstance())
        fmList?.add(DashboardFragment.newInstance())
        fmList?.add(NotificationsFragment.newInstance())
        nav_view.itemIconTintList = null
    }

    override fun setListener() {
        nav_view.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    resetToDefaultIcon() //重置到默认不选中图片
                    item.setIcon(R.mipmap.icon_home_selected)
                    replaceFragment(0)
                }
                R.id.navigation_dashboard -> {
                    resetToDefaultIcon() //重置到默认不选中图片
                    item.setIcon(R.mipmap.icon_discover_selected)
                    replaceFragment(1)
                }
                R.id.navigation_notifications -> {
                    resetToDefaultIcon() //重置到默认不选中图片
                    item.setIcon(R.mipmap.icon_me_selected)
                    replaceFragment(2)
                }
                R.id.navigation_main -> {
                    startActivity(
                        Intent(
                            this@HeadActivity,
                            TabActivity::class.java
                        )
                    )
                    return@setOnNavigationItemSelectedListener false
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
            permission
            true
        }
        shiftNavigation(R.id.navigation_home)
    }

    public fun shiftNavigation(@IdRes id: Int) {
        nav_view.selectedItemId = id
    }

    private fun resetToDefaultIcon() {
        val receive =
            nav_view.menu.findItem(R.id.navigation_home)
        val main =
            nav_view.menu.findItem(R.id.navigation_main)
        val order =
            nav_view.menu.findItem(R.id.navigation_dashboard)
        val mine =
            nav_view.menu.findItem(R.id.navigation_notifications)
        receive.setIcon(R.mipmap.icon_home_normal)
        main.setIcon(R.mipmap.icon_service_normal)
        order.setIcon(R.mipmap.icon_discover_normal)
        mine.setIcon(R.mipmap.icon_me_normal)
    }//申请WRITE_EXTERNAL_STORAGE权限
    //自定义的code
//申请ACCESS_COARSE_LOCATION权限
    //自定义的code
//            ArrayList<String> needPermission = new ArrayList<>();
//            for (String permissionName :
//                    LOCATION_PERMISSIONS) {
//                if (!ContextCompat.checkSelfPermission(HeadActivity.this, permissionName)) {
//                    needPermission.add(permissionName);
//                }
//            }

    /**
     * 请求权限
     */
    private val permission: Unit
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //            ArrayList<String> needPermission = new ArrayList<>();
//            for (String permissionName :
//                    LOCATION_PERMISSIONS) {
//                if (!ContextCompat.checkSelfPermission(HeadActivity.this, permissionName)) {
//                    needPermission.add(permissionName);
//                }
//            }
                ActivityCompat.requestPermissions(
                    this,
                    LOCATION_PERMISSIONS,
                    LOCATION_PERMISSIONS_CODE
                )
            }
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                != PackageManager.PERMISSION_GRANTED
//            ) { //申请ACCESS_COARSE_LOCATION权限
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    100
//                ) //自定义的code
//            } else if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//                != PackageManager.PERMISSION_GRANTED
//            ) { //申请WRITE_EXTERNAL_STORAGE权限
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
//                    101
//                ) //自定义的code
//            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSIONS_CODE) {
            if (grantResults.size == LOCATION_PERMISSIONS.size) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showLongToast("未授予读写权限，无法使用该功能，请开启权限")
                } else if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    showLongToast("未授予定位权限，无法使用该功能，请开启权限")
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
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private const val LOCATION_PERMISSIONS_CODE = 100
    }
}