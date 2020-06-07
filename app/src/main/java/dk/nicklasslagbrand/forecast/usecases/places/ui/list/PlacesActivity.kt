package dk.nicklasslagbrand.forecast.usecases.places.ui.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import dk.nicklasslagbrand.forecast.R
import dk.nicklasslagbrand.forecast.AppConfig
import kotlinx.android.synthetic.main.places.*

/**
 * PlacesActivity is the starting point of the Places use case. It represents the link to the ui and
 * lifecycle of the Places use case. After initializing the PlacesConfig, the PlacesActivity will
 * react to UI state changes from the PlacesPresenter and display the given UI on screen.
 */
class PlacesActivity : AppCompatActivity() {

    private val placesConfig by lazy { AppConfig.initPlacesConfig(activity = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_places)

        placesConfig.presenter.onStateChanged = { state ->
            when (state) {
                is PlacesPresenter.State.Content -> {
                    placesRecyclerView.adapter = PlacesAdapter(state.uiConfigs)
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
}