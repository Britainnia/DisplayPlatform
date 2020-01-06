package com.kira.mypublishplatform.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.BaseActivity
import kotlinx.android.synthetic.main.activity_first.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


//import butterknife.BindColor
//import butterknife.BindView
//import butterknife.ButterKnife
//import butterknife.OnClick

class FirstActivity : BaseActivity() {
//    @BindView(R.id.iv_back)
//    lateinit var ivBack: ImageView
//    @BindView(R.id.tv_title)
//    lateinit var tvTitle: TextView
//    @BindColor(R.color.black_90)
//    var black: Int? = 0
//    @BindView(R.id.header)
//    lateinit var header: LinearLayout
//    @BindView(R.id.estimate)
//    lateinit var estimate: TextView
//    @BindView(R.id.operation_bar)
//    var operationBar: LinearLayout? = null
//    @BindView(R.id.company_image)
//    var companyImage: ImageView? = null
//    @BindView(R.id.company_name)
//    var companyName: TextView? = null
//    @BindView(R.id.service_name)
//    var serviceName: TextView? = null
//    @BindView(R.id.service_title)
//    var serviceTitle: TextView? = null
//    @BindView(R.id.info_company)
//    var infoCompany: LinearLayout? = null
//    @BindView(R.id.head_image)
//    var headImage: ImageView? = null
//    @BindView(R.id.person_name)
//    var personName: TextView? = null
//    @BindView(R.id.gender)
//    var gender: TextView? = null
//    @BindView(R.id.star)
//    var star: TextView? = null
//    @BindView(R.id.staff_phone)
//    var staffPhone: TextView? = null
//    @BindView(R.id.info_person)
//    var infoPerson: LinearLayout? = null
//    @BindView(R.id.start_time)
//    var startTime: TextView? = null
//    @BindView(R.id.icon_start)
//    var iconStart: ImageView? = null
//    @BindView(R.id.tv_start_time)
//    var tvStartTime: TextView? = null
//    @BindView(R.id.card_start_time)
//    var cardStartTime: CardView? = null
//    @BindView(R.id.during_time)
//    var duringTime: TextView? = null
//    @BindView(R.id.icon_during)
//    var iconDuring: ImageView? = null
//    @BindView(R.id.tv_during_time)
//    var tvDuringTime: TextView? = null
//    @BindView(R.id.end)
//    var end: TextView? = null
//    @BindView(R.id.go_qrcode)
//    var goQrcode: RelativeLayout? = null
//    @BindView(R.id.card_during_time)
//    var cardDuringTime: CardView? = null
//    @BindView(R.id.end_time)
//    var endTime: TextView? = null
//    @BindView(R.id.icon_end)
//    var iconEnd: ImageView? = null
//    @BindView(R.id.tv_end_time)
//    var tvEndTime: TextView? = null
//    @BindView(R.id.card_end_time)
//    var cardEndTime: CardView? = null
    private val mContext: Context = this@FirstActivity
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        ButterKnife.bind(this)
//    }

    override fun loadXml() {
        setContentView(R.layout.activity_first)
    }

    override fun getIntentData(savedInstanceState: Bundle?) {

    }

    override fun initView() {

    }

    override fun initData() {

        /*显示actionbar左侧的back键*/
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun setListener() {
        iv_back.setOnClickListener { finish() }
        estimate.setOnClickListener { startActivity(Intent(mContext, TabActivity::class.java)) }
        go_qrcode.setOnClickListener { Toast.makeText(mContext, "click code button", Toast.LENGTH_LONG).show() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//        @OnClick(R.id.iv_back, R.id.estimate, R.id.go_qrcode)
//    fun onViewClicked(view: View) {
//        when (view) {
//
//        }
//    }
}