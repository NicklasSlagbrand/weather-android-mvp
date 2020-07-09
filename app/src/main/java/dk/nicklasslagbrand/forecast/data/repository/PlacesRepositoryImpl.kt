package dk.nicklasslagbrand.forecast.data.repository

import dk.nicklasslagbrand.forecast.utils.AppConfig
import dk.nicklasslagbrand.forecast.data.network.WeatherAPI
import dk.nicklasslagbrand.forecast.domain.entities.Forecast
import dk.nicklasslagbrand.forecast.domain.entities.Place
import dk.nicklasslagbrand.forecast.data.Promise
import dk.nicklasslagbrand.forecast.data.exception.EmptyResponseBodyException
import dk.nicklasslagbrand.forecast.data.exception.UnSuccessfullResponseException
import dk.nicklasslagbrand.forecast.data.repository.mapping.ForecastOneCallResponse
import dk.nicklasslagbrand.forecast.data.repository.mapping.SimplePlaceGroup
import dk.nicklasslagbrand.forecast.data.repository.mapping.asForecast
import dk.nicklasslagbrand.forecast.data.repository.mapping.asPlaces
import dk.nicklasslagbrand.forecast.domain.repository.PlacesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class PlacesRepositoryImpl(
        private val weatherAPI: WeatherAPI
) : PlacesRepository{
    override fun getPlaces(woeIds: List<String>): Promise<List<Place>> {
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

    override fun getWeatherForecast(lon: Double, lat: Double) : Promise<Forecast> {
        val promise = Promise<Forecast>()

        weatherAPI.getWeatherForecast(lon, lat).enqueue(
            object : Callback<ForecastOneCallResponse> {
                override fun onResponse(call: Call<ForecastOneCallResponse>, response: Response<ForecastOneCallResponse>) {

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
                override fun onFailure(call: Call<ForecastOneCallResponse>, t: Throwable) {
                    promise.error(t)
                }
            }
       )
        return promise
    }
}




