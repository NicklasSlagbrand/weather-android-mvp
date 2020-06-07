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
import dk.shape.forecast.usecases.places.repository.mapping.asForecast
import dk.shape.forecast.usecases.places.repository.mapping.asPlaces
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




