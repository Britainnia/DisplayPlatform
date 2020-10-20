package com.kira.mypublishplatform.bean

import com.chad.library.adapter.base.entity.SectionEntity
import com.kira.mypublishplatform.bean.ScrollBean.ScrollItemBean

/**
 * Created by Raul_lsj on 2018/3/22.
 */
class ScrollBean : SectionEntity<ScrollItemBean?> {
    constructor(isHeader: Boolean, header: String?) : super(isHeader, header) {}
    constructor(bean: ScrollItemBean?) : super(bean) {}

    class ScrollItemBean(var url: String, var text: String, var parent: String)
}