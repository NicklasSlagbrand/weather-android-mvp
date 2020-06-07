package dk.nicklasslagbrand.forecast.utils.ui

import dk.nicklasslagbrand.forecast.R
import dk.nicklasslagbrand.forecast.entities.Temperature
import dk.nicklasslagbrand.forecast.entities.TemperatureUnit

fun Temperature.toColorResource(): Int {
    return when (unit) {
        TemperatureUnit.Celsius -> {
            value.celsiusToColor()
        }
    }
}

fun Int.celsiusToColor() = when (this) {
    in Int.MIN_VALUE..-5 -> R.color.temperature1
    in -4..2 -> R.color.temperature2
    in 3..14 -> R.color.temperature3
    in 15..26 -> R.color.temperature4
    else -> R.color.temperature5
}