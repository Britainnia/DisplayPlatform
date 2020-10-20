package com.kira.mypublishplatform.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity

/**
 * app启动页，判断是否登录，验证本地token是否过期，等等
 */
class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({

//            if (MyApplication.mSp.getBoolean(ConstUtils.LOGIN_STATE)) {
//                //去获取用户信息
//                String token = MyApplication.mSp.getStr(ConstUtils.USER_TOKEN);
//                String userId = MyApplication.mSp.getStr(ConstUtils.USER_ID);
            startActivity(Intent(this@StartActivity, HeadActivity::class.java))
            finish()
        }, 1000)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) true else super.onKeyDown(
            keyCode,
            event
        )
    }
}