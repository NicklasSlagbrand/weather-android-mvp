package dk.nicklasslagbrand.forecast.usecases.places.ui.details

import android.support.v7.app.AppCompatActivity
import dk.nicklasslagbrand.forecast.usecases.places.PlacesRouter
import dk.nicklasslagbrand.forecast.usecases.places.PlacesRouterImpl
import dk.nicklasslagbrand.forecast.usecases.places.repository.PlacesRepository

/**
 * Configures the Places Use Case.
 *
 * @param activity The parent activity used to launch new activities and manage lifecycle events.
 * @param placesRepository Repository used to fetch Places.
 */
class PlaceDetailsConfig(activity: AppCompatActivity,
                         placesRepository: PlacesRepository) {

    /**
     * Provides routing from the Places screen to any other screens.
     */
    val router: PlacesRouter = PlacesRouterImpl(
            activity = activity
    )

    /**
     * Handles business logic to manipulate model objects and carry out tasks for
     * the Places use case.
     */
    val interactor: PlaceDetailsInteractor = run {
        val interactor = PlaceDetailsInteractorImpl(
                repository = placesRepository)
        interactor
    }

    /**
     * Converts data states from the Interactor into view states that are ready to be presented on
     * screen.
     */
    val presenter: PlaceDetailsPresenter = PlaceDetailsPresenterImpl(
            interactor = interactor
    )
}