package dk.shape.forecast.usecases.places.repository

import dk.shape.forecast.AppConfig
import dk.shape.forecast.api.WeatherAPI
import dk.shape.forecast.entities.CurrentWeather
import dk.shape.forecast.entities.Forecast
import dk.shape.forecast.entities.ForecastThumbnail
import dk.shape.forecast.entities.Place
import dk.shape.forecast.entities.Temperature
import dk.shape.forecast.entities.TemperatureUnit
import dk.shape.forecast.framework.Promise
import dk.shape.forecast.usecases.Constants
import dk.shape.forecast.usecases.exception.EmptyResponseBodyException
import dk.shape.forecast.usecases.exception.UnSuccessfullResponseException
import dk.shape.forecast.usecases.places.repository.mapping.ForecastResponse
import dk.shape.forecast.usecases.places.repository.mapping.Location
import dk.shape.forecast.usecases.places.repository.mapping.SimplePlace
import dk.shape.forecast.usecases.places.repository.mapping.SimplePlaceGroup
import dk.shape.forecast.utils.ui.extension.toDateString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Locale

class PlacesRepository(
        private val weatherAPI: WeatherAPI
) {
    fun getPlaces(woeIds: List<String> = emptyList()): Promise<List<Place>> {
            val promise = Promise<List<Place>>()

            val ids: List<String> = if(woeIds.isEmpty()) AppConfig.woeIds else woeIds

            weatherAPI.placesQuery(ids = ids.joinToString(separator = ","))
                    .enqueue(object : Callback<SimplePlaceGroup> {
                        override fun onResponse(call: Call<SimplePlaceGroup>, response: Response<SimplePlaceGroup>) {
                            if (!response.isSuccessful) {
                                promise.error(IOException("Response was unsuccessful: ${response.code()}"))
                                return
                            }

                            val result = response.body()
                            if (result == null) {
                                promise.error(IOException("Response has no body"))
                                return
                            }

                            result.let { placeGroup ->
                                promise.success(placeGroup.asPlaces())
                            }
                        }

                        override fun onFailure(call: Call<SimplePlaceGroup>, t: Throwable) {
                            promise.error(t)
                        }
                    })

            return promise
        }

fun getWeatherForecast(lon: Double, lat: Double) : Promise<Forecast>{
        val promise = Promise<Forecast>()

        weatherAPI.getWeatherForecast(lon, lat).enqueue(
            object : Callback<ForecastResponse> {
                override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {

                    when(response.isSuccessful) {
                        false -> promise.error(UnSuccessfullResponseException(response.code().toString()))
                        true -> {
                            response.body().let {
                                if (it != null) {
                                    promise.success(it.asForecast())
                                } else {
                                    promise.error(EmptyResponseBodyException())
                                }
                            }
                        }
                    }

                }
                override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                    promise.error(t)
                }
            }
       )
        return promise
    }
}

private fun SimplePlaceGroup.asPlaces(): List<Place> {
    return places.orEmpty().map { it.asPlace() }
}


private fun SimplePlace.asPlace(): Place {
    val country = getCountryNameFromCountryCode(details?.countryCode)
    val temperature = parameters?.temperature?.toInt() ?: 0

    return Place(
            woeId = id.toString(),
            location = location!!.copy(),
            city = name ?: "",
            country = country ?:"",
            temperature = Temperature(
                    value = temperature,
                    unit = TemperatureUnit.Celsius),
            weatherCode = weathers?.firstOrNull()?.id ?: 0)
}


private fun ForecastResponse.asForecast(): Forecast {
    val startDate = hourlyForecast!!.first().dateTime!!
    val currentDate = currentWeather!!.dateTime!!
    val endDate = hourlyForecast[Constants.LIMIT_HOURLY_FORECAST-1].dateTime!!
    val feelsLike = currentWeather.feelsLike?.toInt() ?: 0
    val dailyWeatherForecast = dailyWeather!!.map {
        ForecastThumbnail(
                dayOfTheWeek = it.dateTime!!.toDateString(),
                iconUrl = "http://openweathermap.org/img/w/${it.weathers?.firstOrNull()?.icon}.png"
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
            weatherCondition = this.currentWeather.weathers?.firstOrNull()?.main!!,
            iconUrl = "http://openweathermap.org/img/w/${currentWeather.weathers?.firstOrNull()?.icon}.png",
            hourly = list,
            currentDateString = currentDate.toDateString(),
            startDateString = startDate.toDateString(),
            endDateString = endDate.toDateString(),
            currentWeather = CurrentWeather(
                    feelsLike = Temperature(
                            value = feelsLike,
                            unit = TemperatureUnit.Celsius)
            ),
            dailyWeather = dailyWeatherForecast

    )
}

private fun getCountryNameFromCountryCode(countryCode: String?): String? {
    val code = countryCode ?: return null
    return Locale("", code).displayCountry
}
