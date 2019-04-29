package com.isidroid.utils.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object YTimeUtils {
    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        val d1 = date1 ?: Date()
        val d2 = date2 ?: Date()
        val sf = SimpleDateFormat("yyyyMMdd")
        return sf.format(d1) == sf.format(d2)
    }

    fun nextQuarterHour(now: Date = Date()): Date {
        val calendar = Calendar.getInstance().apply { time = now }
        val minute = calendar.get(Calendar.MINUTE)
        val quoter = when (minute) {
            in 0..14 -> 15
            in 15..29 -> 30
            in 30..44 -> 45
            else -> 0
        }

        calendar.set(Calendar.MINUTE, quoter)
        if (quoter == 0) calendar.add(Calendar.HOUR, 1)
        return calendar.time
    }

    fun tsToHoursMinutes(millis: Long?): TimePeriod {
        millis ?: return TimePeriod()

        val days = TimeUnit.MILLISECONDS.toDays(millis)
        val hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(days)
        val fullMinutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        val fullSeconds = TimeUnit.MILLISECONDS.toSeconds(millis)

        val hoursInMinutes = TimeUnit.HOURS.toMinutes(hours)
        val minutes = fullMinutes - hoursInMinutes - TimeUnit.DAYS.toMinutes(days)
        val seconds = fullSeconds - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days)

        val string = String.format("%d:%d:%d", hours, minutes, seconds)
        val data = string.split(":")

        return TimePeriod(days.toInt(), data[0].toInt(), data[1].toInt() , data[2].toInt())
    }

    fun difference(startDate: Date, endDate: Date): TimePeriod {
        var different = endDate.time - startDate.time

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = different / daysInMilli
        different %= daysInMilli

        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli

        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli

        val elapsedSeconds = different / secondsInMilli

        return TimePeriod(elapsedDays.toInt(), elapsedHours.toInt(), elapsedMinutes.toInt(), elapsedSeconds.toInt())
    }
}

data class TimePeriod(val days: Int = 0, val hours: Int = 0, val minutes: Int = 0, val seconds: Int = 0)