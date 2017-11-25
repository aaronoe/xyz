package de.aaronoe.xyz.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility methods for dealing with dates
 * Created by aoe on 11/25/17.
 */
object DateUtils {

    private val defaultDatePattern = "dd. MMM yyyy"
    private val defaultDateFormat = SimpleDateFormat(defaultDatePattern, Locale.ENGLISH)

    fun getDefaultDateString(timestamp : Long) : String {
        try {
            return defaultDateFormat.format(Date(timestamp))
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return ""
    }

}