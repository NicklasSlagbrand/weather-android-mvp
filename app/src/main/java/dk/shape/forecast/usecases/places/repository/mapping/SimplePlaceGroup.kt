package dk.shape.forecast.usecases.places.repository.mapping

import com.google.gson.annotations.SerializedName
import dk.shape.forecast.entities.Place

data class SimplePlaceGroup(
        @SerializedName("cnt") val count: Int?,
        @SerializedName("list") val places: List<SimplePlace>?
)

fun SimplePlaceGroup.asPlaces(): List<Place> {
    return places.orEmpty().map { it.asPlace() }
}