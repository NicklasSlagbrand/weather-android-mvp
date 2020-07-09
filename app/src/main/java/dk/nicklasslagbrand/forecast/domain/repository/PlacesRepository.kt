package dk.nicklasslagbrand.forecast.domain.repository

import dk.nicklasslagbrand.forecast.data.Promise
import dk.nicklasslagbrand.forecast.domain.entities.Forecast
import dk.nicklasslagbrand.forecast.domain.entities.Place

interface PlacesRepository {
    fun getPlaces(woeIds: List<String> = emptyList()): Promise<List<Place>>
    fun getWeatherForecast(lon: Double, lat: Double) : Promise<Forecast>

}