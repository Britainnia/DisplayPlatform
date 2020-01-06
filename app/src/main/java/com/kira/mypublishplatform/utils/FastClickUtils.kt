package com.kira.mypublishplatform.utils

/**
 * Created by Kira on 2017/2/23.
 */
class FastClickUtils {

    companion object {
        private var lastClickTime: Long = 0
        val isValidClick: Boolean
            get() {
                val time = System.currentTimeMillis()
                val timeD = time - lastClickTime
                if (timeD in 1..499) {
                    return true
                }
                lastClickTime = time
                return false
            }
    }

}