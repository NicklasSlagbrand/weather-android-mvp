package dk.nicklasslagbrand.forecast.usecases.places

import dk.nicklasslagbrand.forecast.usecases.places.repository.PlacesRepository
import dk.nicklasslagbrand.forecast.usecases.places.ui.list.PlacesInteractor
import dk.nicklasslagbrand.forecast.usecases.places.ui.list.PlacesInteractorImpl
import dk.nicklasslagbrand.forecast.usecases.places.ui.list.PlacesPresenter
import dk.nicklasslagbrand.forecast.usecases.places.ui.list.PlacesPresenterImpl
import dk.nicklasslagbrand.forecast.utils.ui.BaseActivity

/**
 * Configures the Places Use Case.
 *
 * @param activity The parent activity used to launch new activities and manage lifecycle events.
 * @param placesRepository Repository used to fetch Places.
 */
class PlacesConfig(activity: BaseActivity,
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
    val interactor: PlacesInteractor = run {
        val interactor = PlacesInteractorImpl(
                repository = placesRepository,
                router = router)
        activity.lifecycle.addObserver(interactor)
        interactor
    }

    /**
     * Converts data states from the Interactor into view states that are ready to be presented on
     * screen.
     */
    val presenter: PlacesPresenter = PlacesPresenterImpl(
            interactor = interactor
    )
}