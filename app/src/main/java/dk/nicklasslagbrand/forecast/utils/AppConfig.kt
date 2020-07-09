package dk.nicklasslagbrand.forecast.utils

import dk.nicklasslagbrand.forecast.data.network.initHttpClient
import dk.nicklasslagbrand.forecast.data.network.initWeatherAPI
import dk.nicklasslagbrand.forecast.presentation.places.PlacesConfig
import dk.nicklasslagbrand.forecast.data.repository.PlacesRepositoryImpl
import dk.nicklasslagbrand.forecast.presentation.places.ui.details.PlaceDetailsConfig
import dk.nicklasslagbrand.forecast.presentation.BaseActivity

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
    fun initPlacesConfig() =
            PlacesConfig(
                    placesRepositoryImpl = PlacesRepositoryImpl(
                            weatherAPI = weatherAPI
                    )
            )
    fun initPlaceDetailsConfig() =
            PlaceDetailsConfig(
                    placesRepositoryImpl = PlacesRepositoryImpl(
                            weatherAPI = weatherAPI
                    )
            )
}