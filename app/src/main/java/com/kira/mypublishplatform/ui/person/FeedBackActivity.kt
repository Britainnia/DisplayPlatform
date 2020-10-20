package com.kira.mypublishplatform.ui.person

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.databinding.ActivityFeedBackBinding
import com.kira.mypublishplatform.utils.DateFormatUtil
import com.kira.mypublishplatform.utils.DateFormatUtil.Companion.date2String
import com.kira.mypublishplatform.utils.other.StringUtils
import com.kira.mypublishplatform.view.TitleDialog
import java.util.*

class FeedBackActivity : BaseActivity<ActivityFeedBackBinding>(), OnTouchListener {

    private var mComment: String? = null

    private val selectedDate = Calendar.getInstance()

    override fun getIntentData(savedInstanceState: Bundle?) {}

    override fun initData() {
        val date = Date()
        binding.time.text = date2String(date, DateFormatUtil.YMD_FORMAT)
    }

    override fun setListener() {
        binding.submit.setOnClickListener { _: View? ->
            mComment = binding.editComment.text.toString().trim { it <= ' ' }
            if (StringUtils.isBlank(mComment)) {
                showShortToast("请填写评论")
                return@setOnClickListener
            }
            val builder = TitleDialog.Builder(this)
            builder.setTitle("提交评论").setMessage("您是否确认提交此评论?")
                .setPositiveButton(
                    "确认"
                ) { dialog: DialogInterface, _: Int ->
                    //                        ;
                    LoadingBar.dialog(this).extras(arrayOf<Any>("反馈提交中..")).show()
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            submitComment()
                        }
                    }, 1000)
                    dialog.dismiss()
                }
                .setNegativeButton(
                    "取消"
                ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                .create().show()
        }

        binding.ivBack.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.time.setOnClickListener { view: View ->
            val imm =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            Objects.requireNonNull(imm)
                .hideSoftInputFromWindow(view.windowToken, 0) //强制隐藏键盘
            showTimePickerView(binding.time)
        }
    }

    private fun submitComment() {

        LoadingBar.dialog(this).cancel()
        setResult(Activity.RESULT_OK)
        finish()
//        String token = MyApplication.mSp.getStr(ConstUtils.USER_TOKEN);
//
//        Gson gson = new Gson();
//        MakeCommentBean bean = new MakeCommentBean();
//        bean.setEvaComment(mComment);
//        bean.setEvaFwtdLevel(aStar);
//        bean.setOrderNo(orderInfoBean.getOrderNo());
//        bean.setEvaFwzlLevel(qStar);
//        bean.setEvaSmzsLevel(pStar);
//        String json = gson.toJson(bean);
//        //Logger.i("===上传最后结果===" + json);
//        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
//        //String token = MyApplication.mSp.getStr(ConstUtils.USER_TOKEN);
//        MyApplication.api.submitComment(token, requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LoadingDialog.cancel();
//                        Logger.e("==onError==" + e.getMessage());
//                        showLongToast("提交评论失败," + e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody responseBody) {
//
//                        LoadingDialog.cancel();
//                        try {
//                            String json = responseBody.string();
//                            // Logger.i("===json===" + json);
//                            ResponseBean rawBean = new Gson().fromJson(json, ResponseBean.class);
//                            if (rawBean.isStatus()) {
//
////                                    ResponseBean<String> bean = new Gson().fromJson(json, new TypeToken<ResponseBean<String>>() {}.getType());
//                                setResult(RESULT_OK);
//                                finish();
//                            } else
//                                showLongToast("提交评论失败," + rawBean.getMsg());
//
//                            //showShareDialog(bean.store, bean.id);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
    }

    private fun showTimePickerView(time: TextView) { // 弹出时间选择器
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()

        //正确设置方式 原因：注意事项有说明
        startDate.add(Calendar.MONTH, -20)
//        endDate.add(Calendar.DAY_OF_YEAR, 7)
        val pvTime = TimePickerBuilder(
            this
        ) { date: Date, _: View? ->
            try {
                selectedDate.time = date
                time.text = date2String(date, DateFormatUtil.YMD_FORMAT)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
            .setType(booleanArrayOf(true, true, true, false, false, false)) // 默认全部显示
            .setTitleText("日期")
            .setDate(selectedDate)
            .isCyclic(false) //是否循环滚动
            .setRangDate(startDate, endDate) //起始终止年月日设定
            .setLabel("-", "-", " ", ":", "", "") //默认设置为年月日时分秒
            .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            .setOutSideCancelable(false) //点击屏幕，点在控件外部范围时，是否取消显示
            .setSubmitText("确定") //确定按钮文字
            .setCancelText("取消") //取消按钮文字
            //.isDialog(true); //是否显示为对话框样式
            .build()
        pvTime.show()
    }

    override fun onTouch(
        view: View,
        motionEvent: MotionEvent
    ): Boolean { //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if (view.id == R.id.edit_comment && canVerticalScroll(binding.editComment)) {
            view.parent.requestDisallowInterceptTouchEvent(true)
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return false
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private fun canVerticalScroll(editText: EditText?): Boolean { //滚动的距离
        val scrollY = editText!!.scrollY
        //控件内容的总高度
        val scrollRange = editText.layout.height
        //控件实际显示的高度
        val scrollExtent =
            editText.height - editText.compoundPaddingTop - editText.compoundPaddingBottom
        //控件内容总高度与实际显示高度的差值
        val scrollDifference = scrollRange - scrollExtent
        return if (scrollDifference == 0) {
            false
        } else scrollY > 0 || scrollY < scrollDifference - 1
    }
}