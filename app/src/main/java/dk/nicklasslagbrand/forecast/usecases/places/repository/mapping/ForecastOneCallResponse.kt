package dk.nicklasslagbrand.forecast.usecases.places.repository.mapping

import com.google.gson.annotations.SerializedName
import dk.nicklasslagbrand.forecast.entities.CurrentWeather
import dk.nicklasslagbrand.forecast.entities.Forecast
import dk.nicklasslagbrand.forecast.entities.ForecastThumbnail
import dk.nicklasslagbrand.forecast.entities.Temperature
import dk.nicklasslagbrand.forecast.entities.TemperatureUnit
import dk.nicklasslagbrand.forecast.usecases.Constants
import dk.nicklasslagbrand.forecast.utils.ui.extension.toDateString
import dk.nicklasslagbrand.forecast.utils.ui.extension.toWeekDayString

data class ForecastOneCallResponse(
        @SerializedName("lat") val latitude: Double?,
        @SerializedName("lon") val longitude: Double?,
        @SerializedName("hourly") val hourlyForecast: List<HourlytWeatherForecast>?,
        @SerializedName("current") val currentWeather: CurrentWeatherForecast?,
        @SerializedName("daily") val dailyWeather: List<DailyWeatherForecast>?
)

data class HourlytWeatherForecast(
        @SerializedName("dt") val dateTime: Long?,
        @SerializedName("temp") val temperature: Float?
)
data class CurrentWeatherForecast(
        @SerializedName("dt") val dateTime: Long?,
        @SerializedName("temp") val temperature: Float?,
        @SerializedName("feels_like") val feelsLike: Float?,
        @SerializedName("weather") val weathers: List<SimplePlaceWeather>?)

data class DailyWeatherForecast(
        @SerializedName("dt") val dateTime: Long?,
        @SerializedName("temp") val dailyTemp: DailyTemperature?,
        @SerializedName("weather") val weathers: List<SimplePlaceWeather>?)


data class DailyTemperature(
        @SerializedName("day") val temperature: Float?
)

fun ForecastOneCallResponse.asForecast(): Forecast {
    val startDate = hourlyForecast!!.first().dateTime!!
    val currentDate = currentWeather!!.dateTime!!
    val endDate = hourlyForecast[Constants.LIMIT_HOURLY_FORECAST-1].dateTime!!
    val feelsLike = currentWeather.feelsLike?.toInt() ?: 0
    val dailyWeatherForecast = dailyWeather!!
            .take(6)
            .map {
        ForecastThumbnail(
                dayOfTheWeek = it.dateTime!!.toWeekDayString(),
                iconUrl = "http://openweathermap.org/img/w/${it.weathers?.firstOrNull()?.icon}.png",
                temperature = Temperature(
                        value = it.dailyTemp!!.temperature!!.toInt(),
                        unit = TemperatureUnit.Celsius)
        )
    }

    val list: List<Temperature> = hourlyForecast.map {
        val temperature = it.temperature?.toInt() ?: 0
        Temperature(
                value = temperature,
                unit = TemperatureUnit.Celsius)
    }.take(Constants.LIMIT_HOURLY_FORECAST)


    return Forecast(
            temperature = Temperature(
                    value = currentWeather.temperature?.toInt() ?: 0,
                    unit = TemperatureUnit.Celsius),
            location = Location(
                    longitude = longitude,
                    latitude = latitude
            ),
            weatherDescription = this.currentWeather.weathers?.firstOrNull()?.description!!,
            weatherCondition = this.currentWeather.weathers.firstOrNull()?.main!!,
            iconUrl = "http://openweathermap.org/img/w/${currentWeather.weathers.firstOrNull()?.icon}.png",
            hourly = list,
            currentDateString = currentDate.toDateString(),
            startDateString = startDate.toDateString(),
            endDateString = endDate.toDateString(),
            currentWeather = CurrentWeather(
                    feelsLike = Temperature(
                            value = feelsLike,
                            unit = TemperatureUnit.Celsius)
            ),
            dailyWeather = dailyWeatherForecast.takeLast(5)
    )
}

