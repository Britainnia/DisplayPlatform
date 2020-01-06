package com.kira.mypublishplatform.activity

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

//

    override fun loadXml() {
        setContentView(R.layout.activity_main)
    }

    override fun getIntentData(savedInstanceState: Bundle?) {

    }

    override fun initView() {

    }

    override fun initData() {

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
    }

    override fun setListener() {

    }
}
