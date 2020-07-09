package dk.nicklasslagbrand.forecast.presentation.places.ui.list


import dk.nicklasslagbrand.forecast.domain.entities.Place
import dk.nicklasslagbrand.forecast.domain.interactors.PlacesInteractor

interface PlacesPresenter {
    var onStateChanged: (State) -> Unit
    fun loadPlaces()

    sealed class State {
        class Content(val places: List<Place>) : State()
        object Loading : State()
        class Error(val onRetry: () -> Unit) : State()
    }
}

class PlacesPresenterImpl(val interactor: PlacesInteractor) : PlacesPresenter {

    override var onStateChanged: (PlacesPresenter.State) -> Unit = {}
        set(value) {
            field = value
            onStateChanged(state)
        }

    private var state: PlacesPresenter.State = PlacesPresenter.State.Loading
        set(value) {
            if (field != value) {
                field = value
                onStateChanged(value)
            }
        }


    init {
        interactor.onStateChanged = { state ->
            this.state = when (state) {
                is PlacesInteractor.State.Content -> state.map()
                is PlacesInteractor.State.Loading -> PlacesPresenter.State.Loading
                is PlacesInteractor.State.Error -> PlacesPresenter.State.Error(
                        onRetry = { state.onRetry() }
                )
            }
        }
    }

    override fun loadPlaces() {
        interactor.loadPlaces()
    }
}

private fun PlacesInteractor.State.Content.map(): PlacesPresenter.State.Content {
    val place = places.map { it }
    return PlacesPresenter.State.Content(places = place)
}