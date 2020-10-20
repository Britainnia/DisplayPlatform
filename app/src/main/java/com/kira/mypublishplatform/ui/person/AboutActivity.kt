package com.kira.mypublishplatform.ui.person

import android.os.Bundle
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.databinding.ActivityAboutBinding

class AboutActivity : BaseActivity<ActivityAboutBinding>() {

    override fun getIntentData(savedInstanceState: Bundle?) {

    }

    override fun initData() {
//        Glide.with(applicationContext)
//            .load(R.mipmap.discover_bg)
//            .placeholder(R.mipmap.no_pic)     //占位图
//            .error(R.mipmap.no_resource)	  //异常占位图
//            .preload()
//            .into(random)
    }

    override fun setListener() {

        binding.ivBack.setOnClickListener {
            finish()
        }

    }

}
