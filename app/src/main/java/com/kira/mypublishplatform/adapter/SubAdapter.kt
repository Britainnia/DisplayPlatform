package com.kira.mypublishplatform.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.base.DataBindBaseViewHolder
import com.kira.mypublishplatform.databinding.ItemSubBinding
import com.kira.mypublishplatform.model.SubModel

class SubAdapter(data: MutableList<SubModel>?) :
    BaseQuickAdapter<SubModel, BaseViewHolder>(R.layout.item_sub, data) {

    override fun convert(helper: BaseViewHolder, item: SubModel) {

//        val binding: ItemSubBinding = helper.getBinding() as ItemSubBinding

//        helper.setText(R.id.grid_item_name, item.name)
        helper.setText(R.id.grid_item_name, item.name)
//        binding.gridItemName.text = item.name

//        val imageView: ImageView = helper!!.getView(R.id.grid_item_image)

        val options = RequestOptions()
            .placeholder(R.mipmap.no_pic)     //占位图
            .error(R.mipmap.no_resource)	  //异常占位图
//            .override(200, 100)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)//不进行磁盘缓存

        Glide.with(mContext)
            .load(item.icon)
            .apply(options)
            .into(helper.getView(R.id.grid_item_image))
//            .into(binding.gridItemImage)
    }
}