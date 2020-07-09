package dk.nicklasslagbrand.forecast.presentation.places.ui.details

import dk.nicklasslagbrand.forecast.data.repository.PlacesRepositoryImpl
import dk.nicklasslagbrand.forecast.domain.interactors.PlaceDetailsInteractor
import dk.nicklasslagbrand.forecast.domain.interactors.PlaceDetailsInteractorImpl

/**
 * Configures the Places Use Case.
 *
 * @param placesRepositoryImpl Repository used to fetch Places.
 */
class PlaceDetailsConfig(placesRepositoryImpl: PlacesRepositoryImpl) {

    /**
     * Handles business logic to manipulate model objects and carry out tasks for
     * the Places use case.
     */
    val interactor: PlaceDetailsInteractor = run {
        val interactor = PlaceDetailsInteractorImpl(
                repositoryImpl = placesRepositoryImpl)
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