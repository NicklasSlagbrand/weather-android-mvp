package dk.nicklasslagbrand.forecast.usecases.places.repository

import dk.nicklasslagbrand.forecast.AppConfig
import dk.nicklasslagbrand.forecast.api.WeatherAPI
import dk.nicklasslagbrand.forecast.entities.Forecast
import dk.nicklasslagbrand.forecast.entities.Place
import dk.nicklasslagbrand.forecast.framework.Promise
import dk.nicklasslagbrand.forecast.usecases.exception.EmptyResponseBodyException
import dk.nicklasslagbrand.forecast.usecases.exception.UnSuccessfullResponseException
import dk.nicklasslagbrand.forecast.usecases.places.repository.mapping.ForecastResponse
import dk.nicklasslagbrand.forecast.usecases.places.repository.mapping.SimplePlaceGroup
import dk.nicklasslagbrand.forecast.usecases.places.repository.mapping.asForecast
import dk.nicklasslagbrand.forecast.usecases.places.repository.mapping.asPlaces
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

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




