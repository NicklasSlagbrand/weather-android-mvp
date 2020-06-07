package dk.nicklasslagbrand.forecast.usecases.places.ui.details

import dk.nicklasslagbrand.forecast.entities.Forecast

interface PlaceDetailsPresenter {
    var onStateChanged: (State) -> Unit
    fun getForecast(lon: Double, lat: Double)

    sealed class State {
        class ShowForecast(val forecast: Forecast) : State()
        object Loading : State()
        class Error(val onRetry: () -> Unit) : State()
    }
}

class PlaceDetailsPresenterImpl(val interactor: PlaceDetailsInteractor) : PlaceDetailsPresenter {

    override fun getForecast(lon: Double, lat: Double) {
        interactor.getForecast(lon, lat)
    }

    override var onStateChanged: (PlaceDetailsPresenter.State) -> Unit = {}
        set(value) {
            field = value
            onStateChanged(state)
        }

    private var state: PlaceDetailsPresenter.State = PlaceDetailsPresenter.State.Loading
        set(value) {
            if (field != value) {
                field = value
                onStateChanged(value)
            }
        }


    init {
        interactor.onStateChanged = { state ->
            this.state = when (state) {
                is PlaceDetailsInteractor.State.ShowForecast -> PlaceDetailsPresenter.State.ShowForecast(
                        state.forecast
                )
                is PlaceDetailsInteractor.State.Loading -> PlaceDetailsPresenter.State.Loading
                is PlaceDetailsInteractor.State.Error -> PlaceDetailsPresenter.State.Error(
                        onRetry = { state.onRetry() }
                )
            }
        }
    }
}
