package dk.shape.forecast.usecases.places

import android.app.Activity
import dk.shape.forecast.entities.Place
import dk.shape.forecast.usecases.places.ui.details.PlaceDetailsActivity
import java.lang.ref.WeakReference


interface PlacesRouter {
    fun onPlaceSelected(
            woeId: String,
            name: String,
            lon: Double,
            lat: Double
    )
}

class PlacesRouterImpl(activity: Activity): PlacesRouter {
    override fun onPlaceSelected(woeId: String, name: String,lon: Double,
                                 lat: Double) {
        activityRef.get()?.let { PlaceDetailsActivity.startActivity(
                it, woeId, name, lon, lat)
        }
    }

    val activityRef = WeakReference(activity)
}