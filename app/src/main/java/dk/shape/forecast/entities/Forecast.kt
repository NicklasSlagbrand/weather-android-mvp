package dk.shape.forecast.entities

import dk.shape.forecast.usecases.places.repository.mapping.Location

data class Forecast(
        val temperature: Temperature,
        val location: Location,
        val iconUrl: String,
        val hourly: List<Temperature>,
        val weatherDescription: String,
        val weatherCondition: String,
        val currentDateString: String,
        val startDateString: String,
        val endDateString: String,
        val currentWeather: CurrentWeather
)

data class CurrentWeather(
    val feelsLike: Temperature
)
