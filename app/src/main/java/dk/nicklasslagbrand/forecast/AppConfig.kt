package dk.nicklasslagbrand.forecast

import android.support.v7.app.AppCompatActivity
import dk.nicklasslagbrand.forecast.api.initHttpClient
import dk.nicklasslagbrand.forecast.api.initWeatherAPI
import dk.nicklasslagbrand.forecast.usecases.places.PlacesConfig
import dk.nicklasslagbrand.forecast.usecases.places.repository.PlacesRepository
import dk.nicklasslagbrand.forecast.usecases.places.ui.details.PlaceDetailsConfig

object AppConfig {
    val woeIds = listOf(
            "2643743",
            "2950159",
            "3128760",
            "2267057",
            "2964574",
            "2618425",
            "524901",
            "5128581",
            "5375480",
            "2147714",
            "292223",
            "2988507")

    private val apiKey = "07cf6203f5e4e4a723911f15385499c5"

    private val httpClient by lazy { initHttpClient(apiKey) }
    private val weatherAPI by lazy { initWeatherAPI(httpClient) }

    /**
     * Initializes the Places use case configuration.
     *
     * @param The parent activity used to launch new activities and manage lifecycle events.
     */
    fun initPlacesConfig(activity: AppCompatActivity) =
            PlacesConfig(
                    activity = activity,
                    placesRepository = PlacesRepository(
                            weatherAPI = weatherAPI
                    )
            )
    fun initPlaceDetailsConfig() =
            PlaceDetailsConfig(
                    placesRepository = PlacesRepository(
                            weatherAPI = weatherAPI
                    )
            )
}