package com.kira.mypublishplatform.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.util.*

class MultipleItem(private val itemType: Int) : MultiItemEntity {

    var type: String? = null
    var items: List<SubModel> = ArrayList()

    override fun getItemType(): Int {
        return itemType
    }

    companion object {
        const val HEADER = 1
        const val FOOTER = 2
        const val ITEM = 3
    }

}