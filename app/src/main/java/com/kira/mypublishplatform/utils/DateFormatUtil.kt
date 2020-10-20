package com.kira.mypublishplatform.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateFormatUtil {
    /**
     * 将0时区时间转换成系统所在时区
     * 已经封装好我们服务器的时间转换
     * 返回的时间 只有日期 没有小时
     *
     * @param strDate
     * @return
     */
//    public static String dateTransformBetweenTimeZone(String strDate) {
//
//        DateFormat formatter = null;
//        Long targetTime = null;
//        try {
//            Date sourceDate = ConvertToDate(strDate);
//            TimeZone sourceTimeZone = TimeZone.getTimeZone("GMT");
//            TimeZone targetTimeZone = TimeZone.getDefault();
//            formatter = new SimpleDateFormat(DATE_FORMAT_EN);
//            targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset()
//                    + targetTimeZone.getRawOffset();
//        } catch (Exception e) {
////			e.printStackTrace();
//            Log.i("DateFormat", "有些日期还未生成，导致日期格式化错误，正常现象。。");
//            return "";
//        }
//        return getTime(new Date(targetTime), formatter);
//    }

    companion object {
        const val TimeStamp = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val YMD_FORMAT = "yyyy-MM-dd"
        const val YMD_FORMAT_2 = "yyyy.MM.dd"
        const val YMD_FORMAT_3 = "yyyy年MM月dd日"
        const val YMD_FORMAT_H = "yyyyMMdd"
        const val YM_FORMAT = "yyyy-MM"
        const val MD_FORMAT = "MM-dd"
        const val MD_FORMAT_2 = "MM.dd"
        const val HMS_FORMAT = "HH:mm:ss"
        const val HM_FORMAT = "HH:mm"
        const val MDHM_FORMAT = "MM.dd HH:mm"
        const val YMD_HMS_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val YMD_HM_FORMAT = "yyyy-MM-dd HH:mm"
        const val YMD_HMS_FORMAT_2 = "yyyy年MM月dd日 HH:mm:ss"
        const val YMD_HM_FORMAT_2 = "yyyy年MM月dd日 HH:mm"
        const val YMDHMS_FORMAT = "yyyyMMddHHmmss"

        /**
         * 将时间戳转换成字符串
         *
         * @param dateTime
         * @param format   格式
         * @return
         */
        fun formatDateTimeMill(
            dateTime: Long,
            format: String
        ): String? {
            val date = Date(dateTime)
            val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
            return simpleDateFormat.format(date)
        }

        fun convertToDate(
            strDate: String?,
            strFormat: String = YMD_HMS_FORMAT
        ): String? {
            val sfFrom = SimpleDateFormat(TimeStamp, Locale.getDefault())
            val sfTo = SimpleDateFormat(strFormat, Locale.getDefault())
            sfFrom.timeZone = TimeZone.getTimeZone("UTC")
            try {
                val ss = sfFrom.parse(strDate)
                return sfTo.format(ss)
            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 将date转换成String
         *
         * @param date
         * @param formatter
         * @return
         */
        fun date2String(date: Date, formatter: String): String? {
    //        val formatter: String = formatter
            val sdf = SimpleDateFormat(formatter, Locale.getDefault())
            return sdf.format(date)
        }

        /**
         * 把String转换成date
         *
         * @param strDate
         * @return
         * @throws ParseException
         * @throws Exception      2015-11-12T04:55:56.000Z
         */
        fun string2Date(strDate: String, format: String): Date? {
            val inputDf = SimpleDateFormat(format, Locale.getDefault())
            inputDf.timeZone = TimeZone.getTimeZone("UTC")
            var result: Date? = null
            try {
                result = inputDf.parse(strDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }
    }
}