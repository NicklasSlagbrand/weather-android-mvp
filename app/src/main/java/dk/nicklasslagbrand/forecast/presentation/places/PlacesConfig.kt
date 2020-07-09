package dk.nicklasslagbrand.forecast.presentation.places

import dk.nicklasslagbrand.forecast.data.repository.PlacesRepositoryImpl
import dk.nicklasslagbrand.forecast.domain.interactors.PlacesInteractor
import dk.nicklasslagbrand.forecast.domain.interactors.PlacesInteractorImpl
import dk.nicklasslagbrand.forecast.presentation.places.ui.list.PlacesPresenter
import dk.nicklasslagbrand.forecast.presentation.places.ui.list.PlacesPresenterImpl
import dk.nicklasslagbrand.forecast.presentation.BaseActivity

/**
 * Configures the Places Use Case.
 *
 * @param activity The parent activity used to launch new activities and manage lifecycle events.
 * @param placesRepositoryImpl Repository used to fetch Places.
 */
class PlacesConfig(placesRepositoryImpl: PlacesRepositoryImpl) {

    /**
     * Handles business logic to manipulate model objects and carry out tasks for
     * the Places use case.
     */
    val interactor: PlacesInteractor = run {
        val interactor = PlacesInteractorImpl(
                repositoryImpl = placesRepositoryImpl)
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