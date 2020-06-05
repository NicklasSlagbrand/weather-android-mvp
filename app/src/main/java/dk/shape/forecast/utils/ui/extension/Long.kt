package dk.shape.forecast.utils.ui.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Long.toDateString() : String{
    val date = Date(this * 1000L)
    val sdf = SimpleDateFormat("dd-MMM HH:mm", Locale.getDefault())
//        val cal: Calendar = Calendar.getInstance()
//        sdf.timeZone = cal.timeZone
    return sdf.format(date)
}