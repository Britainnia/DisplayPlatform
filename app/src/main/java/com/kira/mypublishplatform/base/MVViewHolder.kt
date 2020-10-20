package com.kira.mypublishplatform.base

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MVViewHolder<T : ViewDataBinding?>(binding: T) : BaseViewHolder(binding!!.root) {
    var binding: T? = null
    fun setQAdapter(adapter: BaseQuickAdapter<*, *>?): BaseViewHolder {
        super.setAdapter(adapter)
        return this
    }

    val dataViewBinding: T?
        get() = binding

    init {
        binding!!.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        this.binding = binding
    }
}