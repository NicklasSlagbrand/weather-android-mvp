package dk.nicklasslagbrand.forecast.usecases.places.ui.details

import dk.nicklasslagbrand.forecast.usecases.places.repository.PlacesRepository

/**
 * Configures the Places Use Case.
 *
 * @param placesRepository Repository used to fetch Places.
 */
class PlaceDetailsConfig(placesRepository: PlacesRepository) {

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