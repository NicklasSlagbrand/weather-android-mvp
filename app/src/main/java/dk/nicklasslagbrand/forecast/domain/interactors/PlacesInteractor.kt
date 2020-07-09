package dk.nicklasslagbrand.forecast.domain.interactors

import dk.nicklasslagbrand.forecast.domain.entities.Place
import dk.nicklasslagbrand.forecast.data.repository.PlacesRepositoryImpl

interface PlacesInteractor {
    var onStateChanged: (State) -> Unit
    fun loadPlaces()

    sealed class State {
        class Content(val places: List<Place>) : State()
        object Loading : State()
        class Error(val onRetry: () -> Unit) : State()
    }
}

class PlacesInteractorImpl(
        private val repositoryImpl: PlacesRepositoryImpl
) : PlacesInteractor{

    override var onStateChanged: (PlacesInteractor.State) -> Unit = {}
        set(value) {
            field = value
            onStateChanged(state)
        }

    private var state: PlacesInteractor.State = PlacesInteractor.State.Loading
        set(value) {
            if (field != value) {
                field = value
                onStateChanged(value)
            }
        }
    
    override fun loadPlaces() {
        state = PlacesInteractor.State.Loading
        repositoryImpl.getPlaces()
                .onSuccess { places ->
                    state = PlacesInteractor.State.Content(
                            places = places.map { it
                            })
                }
                .onError {
                    state = PlacesInteractor.State.Error(
                            onRetry = {
                                loadPlaces()
                            }
                    )
                }
    }
}