package dk.shape.forecast.usecases.places.repository.mapping

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
        @SerializedName("lat") val latitude: Double?,
        @SerializedName("lon") val longitude: Double?,
        @SerializedName("hourly") val hourlyForecast: List<HourlytWeatherForecast>?,
        @SerializedName("current") val currentWeather: CurrentWeatherForecast?
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
