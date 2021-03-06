package dk.nicklasslagbrand.forecast.domain.interactors

import dk.nicklasslagbrand.forecast.domain.entities.Forecast
import dk.nicklasslagbrand.forecast.data.repository.PlacesRepositoryImpl

interface PlaceDetailsInteractor {
    var onStateChanged: (State) -> Unit
    fun getForecast(lon: Double, lat: Double)

    sealed class State {
        class ShowForecast(val forecast: Forecast) : State()
        object Loading : State()
        class Error(val onRetry: () -> Unit) : State()
    }
}

class PlaceDetailsInteractorImpl(
        private val repositoryImpl: PlacesRepositoryImpl
) : PlaceDetailsInteractor {
    override fun getForecast(lon: Double, lat: Double) {
        state = PlaceDetailsInteractor.State.Loading
        repositoryImpl.getWeatherForecast(lon,lat)
                .onSuccess {
                    state = PlaceDetailsInteractor.State.ShowForecast(
                            it
                    )
                }
                .onError {
                    state = PlaceDetailsInteractor.State.Error(
                            onRetry = {
                                getForecast(lon, lat)
                            }
                    )
                }
    }

    override var onStateChanged: (PlaceDetailsInteractor.State) -> Unit = {}
        set(value) {
            field = value
            onStateChanged(state)
        }

    private var state: PlaceDetailsInteractor.State = PlaceDetailsInteractor.State.Loading
        set(value) {
            if (field != value) {
                field = value
                onStateChanged(value)
            }
        }

}