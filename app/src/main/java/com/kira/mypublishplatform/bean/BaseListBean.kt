package com.kira.mypublishplatform.bean

import java.io.Serializable

class BaseListBean<T> : Serializable {
    var total = 0
    var rows: MutableList<T>? = arrayListOf()

    constructor()
    constructor(count: Int, data: MutableList<T>?) : super() {
        total = count
        rows = data
    }

}