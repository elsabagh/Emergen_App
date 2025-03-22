package com.example.emergen_app.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

fun getWorkingHours(startTime: String, endTime: String): String {
    val format = SimpleDateFormat("hh:mm", Locale.getDefault())
    try {
        val start = format.parse(startTime)
        val end = format.parse(endTime)

        // التأكد من أن النهاية لا تأتي قبل البداية، ثم استخدام القيمة المطلقة للحصول على فرق زمني موجب
        val diffInMillis = Math.abs(end.time - start.time)
        val diffInHours = diffInMillis / (1000 * 60 * 60)

        return "$diffInHours h"
    } catch (e: ParseException) {
        e.printStackTrace()
        return "Invalid time format"
    }
}
