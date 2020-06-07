package dk.nicklasslagbrand.forecast.entities

data class Temperature(val value: Int,
                       val unit: TemperatureUnit)

sealed class TemperatureUnit(val postFix: String) {
    object Celsius : TemperatureUnit(postFix = "°C")
}

fun Temperature.toFloat(): Float {
    return value.toFloat()
}