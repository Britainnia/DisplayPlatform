package com.kira.mypublishplatform.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.model.SubModel

class SubAdapter(layoutResId: Int, data: List<SubModel?>?) :
    BaseQuickAdapter<SubModel?, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: SubModel?) {
        helper.setText(R.id.grid_item_name, item!!.name)

//        val imageView: ImageView = helper!!.getView(R.id.grid_item_image)

        Glide.with(mContext)
            .load(item.icon)
            .into(helper.getView(R.id.grid_item_image))
    }
}