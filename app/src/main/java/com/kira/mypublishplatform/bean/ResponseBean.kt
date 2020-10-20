package com.kira.mypublishplatform.bean

import java.io.Serializable

/**
 * @program: 测试
 * @description:返回的JSON数据结构标准
 * @author:
 * @create: 2018-10-17 09:01
 */
class ResponseBean<T> : Serializable {
    var isStatus = false
    var data: T? = null

    var code: String? = null
    var msg: String? = null

    constructor()
    constructor(status: Boolean, data: T) : super() {
        isStatus = status
        this.data = data
    }

    constructor(
        status: Boolean,
        data: T,
        code: String?,
        msg: String?
    ) : super() {
        isStatus = status
        this.data = data
        this.code = code
        this.msg = msg
    }

    constructor(status: Boolean, code: String?, msg: String?) {
        isStatus = status
        this.code = code
        this.msg = msg
    }

}