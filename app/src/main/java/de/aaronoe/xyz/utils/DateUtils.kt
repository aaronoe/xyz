package de.aaronoe.xyz.utils

import android.content.Context
import de.aaronoe.xyz.R
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

    private val DEFAULT_DATEFORMAT = "MMMM d, yyyy"
    private val DEFAULT_FORMAT = SimpleDateFormat(DEFAULT_DATEFORMAT, Locale.ENGLISH)
    private val TODAY_FORMAT = SimpleDateFormat("HH:mm", Locale.ENGLISH)

    fun getGroupItemString(context: Context, timestamp: Long): String {
        val c1 = Calendar.getInstance() // today
        c1.add(Calendar.DAY_OF_YEAR, -1) // yesterday

        val c2 = Calendar.getInstance()
        c2.time = Date(timestamp) // your date

        val c3 = Calendar.getInstance() // today

        if (c3.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
            if (c3.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                return TODAY_FORMAT.format(Date(timestamp))
            } else if (c2.get(Calendar.DAY_OF_YEAR) == c1.get(Calendar.DAY_OF_YEAR)) {
                return "Yesterday"
            }
        }

        return DEFAULT_FORMAT.format(Date(timestamp))

    }

}