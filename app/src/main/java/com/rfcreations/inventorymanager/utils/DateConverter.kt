package com.rfcreations.inventorymanager.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object DateConverter {
    /**
     *@param dateInMilli The time in milliseconds to  be converted
     */
    fun convertLongToDate(dateInMilli: Long, pattern: String): String {
        val date = Date(dateInMilli)
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(date)
    }

}
