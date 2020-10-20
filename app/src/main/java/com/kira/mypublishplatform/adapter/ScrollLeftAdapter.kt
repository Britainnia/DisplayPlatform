package com.kira.mypublishplatform.adapter

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.DataBindBaseViewHolder
import com.kira.mypublishplatform.databinding.ScrollLeftBinding
import java.util.*

/**
 * Created by Raul_lsj on 2018/3/22.
 */
class ScrollLeftAdapter(data: List<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.scroll_left, data) {
    private var tv: MutableList<TextView>? = ArrayList()

    override fun convert(helper: BaseViewHolder, item: String) {

        helper.addOnClickListener(R.id.item)
            .setText(R.id.left_text, item)

        //将左侧item中的TextView添加到集合中
        tv!!.add(helper.getView<View>(R.id.left_text) as TextView)
        //设置进入页面之后,左边列表的初始状态
        if (tv != null  && tv!!.size == data.size) {
            selectItem(0)
        }
        helper.getView<View>(R.id.item).isSelected = true
    }

    //传入position,设置左侧列表相应item高亮
    fun selectItem(position: Int) {
        for (i in data.indices) {
            if (position == i) {
                tv!![i].setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
                tv!![i].setTextColor(ContextCompat.getColor(mContext, R.color.bs_blue))
                tv!![i].paint.isFakeBoldText = true

                //以下是指定某一个TextView滚动的效果
                tv!![i].ellipsize = TextUtils.TruncateAt.MARQUEE
                tv!![i].isFocusable = true
                tv!![i].isFocusableInTouchMode = true
                tv!![i].marqueeRepeatLimit = -1
            } else {
                tv!![i].setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray_f0))
                tv!![i].setTextColor(ContextCompat.getColor(mContext, R.color.text_selected))
                tv!![i].paint.isFakeBoldText = false

                //失去焦点则停止滚动
                tv!![i].ellipsize = TextUtils.TruncateAt.END
                tv!![i].isFocusable = false
                tv!![i].isFocusableInTouchMode = false
                tv!![i].marqueeRepeatLimit = 0
            }
        }
    }
}