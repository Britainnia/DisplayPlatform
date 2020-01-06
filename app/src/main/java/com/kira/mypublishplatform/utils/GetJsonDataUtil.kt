package com.kira.mypublishplatform.utils

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * 作者       ：潘志仁
 * 创建时间   ：2019/9/19
 * 邮箱       770440536@qq.com
 */
class GetJsonDataUtil {
    fun getJson(context: Context, fileName: String?): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(fileName!!)
                )
            )
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}