package com.kira.mypublishplatform.base

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseViewHolder

class DataBindBaseViewHolder(view: View) : BaseViewHolder(view){
    private var binding: ViewDataBinding? = null

    init {
        binding = DataBindingUtil.bind(itemView)
    }

    fun getBinding(): ViewDataBinding? {
        return binding
    }
}