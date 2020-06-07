package dk.shape.forecast.utils.ui.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Long.toDateString() : String{
    val date = Date(this * 1000L)
    val sdf = SimpleDateFormat("dd-MMM HH:mm", Locale.getDefault())
    return sdf.format(date)
}

fun Long.toWeekDayString() : String{
    val date = Date(this * 1000L)
    val sdf = SimpleDateFormat("EEE", Locale.getDefault())
    return sdf.format(date)
}