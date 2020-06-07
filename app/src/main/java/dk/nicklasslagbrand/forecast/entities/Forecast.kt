package dk.nicklasslagbrand.forecast.entities

import dk.nicklasslagbrand.forecast.usecases.places.repository.mapping.Location

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
        val currentWeather: CurrentWeather,
        val dailyWeather: List<ForecastThumbnail>
)

data class CurrentWeather(
    val feelsLike: Temperature
)

data class ForecastThumbnail(
        val temperature: Temperature,
        val iconUrl: String,
        val dayOfTheWeek: String
)


