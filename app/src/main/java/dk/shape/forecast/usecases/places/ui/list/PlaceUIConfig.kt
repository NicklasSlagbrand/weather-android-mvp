package dk.shape.forecast.usecases.places.ui.list

class PlaceUIConfig(val city: String,
                    val country: String,
                    val temperature: String,
                    val temperatureColorResource: Int,
                    val onClick: () -> Unit)