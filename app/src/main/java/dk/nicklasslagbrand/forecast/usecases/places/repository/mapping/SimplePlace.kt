package dk.nicklasslagbrand.forecast.usecases.places.repository.mapping

import com.google.gson.annotations.SerializedName
import dk.nicklasslagbrand.forecast.entities.Place
import dk.nicklasslagbrand.forecast.entities.Temperature
import dk.nicklasslagbrand.forecast.entities.TemperatureUnit
import java.util.Locale

data class SimplePlace(
        @SerializedName("coord") val location: Location?,
        @SerializedName("sys") val details: SimplePlaceDetails?,
        @SerializedName("main") val parameters: SimplePlaceParameters?,
        @SerializedName("weather") val weathers: List<SimplePlaceWeather>?,
        @SerializedName("id") val id: Int?,
        @SerializedName("name") val name: String?
)

fun SimplePlace.asPlace(): Place {
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

private fun getCountryNameFromCountryCode(countryCode: String?): String? {
    val code = countryCode ?: return null
    return Locale("", code).displayCountry
}