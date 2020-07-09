package dk.nicklasslagbrand.forecast.presentation.places.ui.list

import android.os.Bundle
import android.view.View
import dk.nicklasslagbrand.forecast.R
import dk.nicklasslagbrand.forecast.domain.entities.Place
import dk.nicklasslagbrand.forecast.utils.AppConfig
import dk.nicklasslagbrand.forecast.presentation.BaseActivity
import dk.nicklasslagbrand.forecast.presentation.places.ui.details.PlaceDetailsActivity
import kotlinx.android.synthetic.main.places.*

/**
 * PlacesActivity is the starting point of the Places use case. It represents the link to the ui and
 * lifecycle of the Places use case. After initializing the PlacesConfig, the PlacesActivity will
 * react to UI state changes from the PlacesPresenter and display the given UI on screen.
 */
class PlacesActivity : BaseActivity() {

    private val placesConfig by lazy { AppConfig.initPlacesConfig() }

    override fun provideLayoutId(): Int? = R.layout.page_places

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToData()
        placesConfig.presenter.loadPlaces()
    }

    private fun subscribeToData() {
        placesConfig.presenter.onStateChanged = { state ->
            when (state) {
                is PlacesPresenter.State.Content -> {
                    placesRecyclerView.adapter = PlacesAdapter(state.places).apply {
                        clickListener = {
                            navigateToForecastDetails(it)
                        }
                    }
                    placesRecyclerView.visibility = View.VISIBLE
                    loadingView.visibility = View.GONE
                    errorView.visibility = View.GONE
                }
                is PlacesPresenter.State.Loading -> {
                    placesRecyclerView.visibility = View.GONE
                    loadingView.visibility = View.VISIBLE
                    errorView.visibility = View.GONE
                }
                is PlacesPresenter.State.Error -> {
                    placesRecyclerView.visibility = View.GONE
                    loadingView.visibility = View.GONE
                    errorView.visibility = View.VISIBLE

                    errorRetryButton.setOnClickListener {
                        state.onRetry()
                    }
                }
            }
        }
    }

    private fun navigateToForecastDetails(place: Place){
        PlaceDetailsActivity.startActivity(
                context = this,
                lat = place.location.latitude!!,
                lon = place.location.longitude!!
        )
    }
}