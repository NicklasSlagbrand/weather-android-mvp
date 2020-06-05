package dk.shape.forecast.usecases.places.ui.list

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import dk.shape.forecast.entities.Place
import dk.shape.forecast.usecases.places.PlacesRouter
import dk.shape.forecast.usecases.places.repository.PlacesRepository

interface PlacesInteractor {
    var onStateChanged: (State) -> Unit

    class PlaceHandler(val place: Place,
                       val onClick: () -> Unit)

    sealed class State {
        class Content(val placeHandlers: List<PlaceHandler>) : State()
        object Loading : State()
        class Error(val onRetry: () -> Unit) : State()
    }
}

class PlacesInteractorImpl(private val router: PlacesRouter,
                           private val repository: PlacesRepository) : PlacesInteractor, LifecycleObserver {

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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        loadData()
    }
    
    private fun loadData() {
        state = PlacesInteractor.State.Loading
        repository.getPlaces()
                .onSuccess { places ->
                    state = PlacesInteractor.State.Content(
                            placeHandlers = places.map { place ->
                                PlacesInteractor.PlaceHandler(
                                        place = place,
                                        onClick = {
                                            router.onPlaceSelected(
                                                    place.woeId, place.city,
                                                    place.location.longitude!!,
                                                    place.location.latitude!!
                                            )
                                        })
                            })
                }
                .onError {
                    state = PlacesInteractor.State.Error(
                            onRetry = {
                                loadData()
                            }
                    )
                }
    }
}