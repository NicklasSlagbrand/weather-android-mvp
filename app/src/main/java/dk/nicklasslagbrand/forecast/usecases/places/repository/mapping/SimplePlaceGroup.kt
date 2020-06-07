package dk.nicklasslagbrand.forecast.usecases.places.repository.mapping

import com.google.gson.annotations.SerializedName
import dk.nicklasslagbrand.forecast.entities.Place

data class SimplePlaceGroup(
        @SerializedName("cnt") val count: Int?,
        @SerializedName("list") val places: List<SimplePlace>?
)

fun SimplePlaceGroup.asPlaces(): List<Place> {
    return places.orEmpty().map { it.asPlace() }
}