package com.isidroid.utilsmodule.utils

import java.text.SimpleDateFormat
import java.util.*

object YTimeUtils {
    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        var date1 = date1 ?: Date()
        var date2 = date2 ?: Date()
        var sf = SimpleDateFormat("yyyyMMdd")
        return sf.format(date1) == sf.format(date2)
    }

    fun nextQuarterHour(now: Date = Date()): Date {
        val calendar = Calendar.getInstance().apply { time = now }
        val minute = calendar.get(Calendar.MINUTE)
        var quoter = when (minute) {
            in 0..14 -> 15
            in 15..29 -> 30
            in 30..44 -> 45
            else -> 0
        }

        calendar.set(Calendar.MINUTE, quoter)
        if (quoter == 0) calendar.add(Calendar.HOUR, 1)
        return calendar.time
    }


}