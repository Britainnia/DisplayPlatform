package com.kira.mypublishplatform.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.DataBindBaseViewHolder
import com.kira.mypublishplatform.bean.ScrollBean
import com.kira.mypublishplatform.databinding.ScrollHeadBinding
import com.kira.mypublishplatform.databinding.ScrollRightBinding

/**
 * Created by Raul_lsj on 2018/3/28.
 */
class ScrollRightAdapter(data: List<ScrollBean>?) :
    BaseSectionQuickAdapter<ScrollBean, BaseViewHolder>(
        R.layout.scroll_right,
        R.layout.scroll_head,
        data
    ) {
    override fun convertHead(helper: BaseViewHolder, item: ScrollBean) {
//        val binding: ScrollHeadBinding = helper.getBinding() as ScrollHeadBinding
//        binding.leftText.text = item.header
        helper.setText(R.id.head_text, item.header)
    }

    override fun convert(helper: BaseViewHolder, item: ScrollBean) {
//        val binding: ScrollRightBinding = helper.getBinding() as ScrollRightBinding
        val t: ScrollBean.ScrollItemBean = item.t as ScrollBean.ScrollItemBean
//        binding.realName.text = t.text
        helper.setText(R.id.real_name, t.text)

        val options = RequestOptions()
            .placeholder(R.mipmap.no_pic)     //占位图
            .error(R.mipmap.no_resource)	  //异常占位图
//            .override(200, 100)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)//不进行磁盘缓存

        Glide.with(mContext)
            .load(t.url)
            .apply(options)
            .into(helper.getView(R.id.head_image))
//            .into(binding.headImage)
    }
}