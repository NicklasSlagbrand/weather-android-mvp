package dk.nicklasslagbrand.forecast.data.repository.mapping

import com.google.gson.annotations.SerializedName

data class SimplePlaceDetails(
        @SerializedName("type") val type: Int?,
        @SerializedName("id") val id: Int?,
        @SerializedName("country") val countryCode: String?,
        @SerializedName("sunrise") val sunrise: Long?,
        @SerializedName("sunset") val sunset: Long?
)