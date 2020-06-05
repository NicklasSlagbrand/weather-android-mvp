package dk.shape.forecast.utils.ui

fun String.toImageUrl(): String {
    return when {
        this.toLowerCase() == "rain" -> "https://firebasestorage.googleapis.com/v0/b/forecast-292f8.appspot.com/o/rain.jpg?alt=media&token=215ddb1a-6665-4dcc-8a83-53ce53a25a0a"
        this.toLowerCase() == "thunderstorm" -> "https://firebasestorage.googleapis.com/v0/b/forecast-292f8.appspot.com/o/thunderstorm.jpg?alt=media&token=caccdc8b-cd0e-4da2-8146-5020ed8c7446"
        this.toLowerCase() == "snow" -> "https://firebasestorage.googleapis.com/v0/b/forecast-292f8.appspot.com/o/snow.jpg?alt=media&token=cd595d59-ab1a-4362-9904-18ea810aae28"
        this.toLowerCase() == "clear" -> "https://firebasestorage.googleapis.com/v0/b/forecast-292f8.appspot.com/o/clear.png?alt=media&token=b834cf3f-05f9-4b14-9437-2fee972df426"
        this.toLowerCase() == "clouds" -> "https://firebasestorage.googleapis.com/v0/b/forecast-292f8.appspot.com/o/clouds.jpg?alt=media&token=abdbe30f-7bf7-4adb-b5b7-60064667c5f9"
        else -> ""
    }
}