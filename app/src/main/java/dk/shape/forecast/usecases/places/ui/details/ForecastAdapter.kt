package dk.shape.forecast.usecases.places.ui.details

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import dk.shape.forecast.R
import dk.shape.forecast.entities.ForecastThumbnail
import dk.shape.forecast.utils.ui.extension.inflate
import dk.shape.forecast.utils.ui.extension.loadImageWithFitCenter
import kotlinx.android.synthetic.main.view_forecast_cardview.view.ivIcon
import kotlinx.android.synthetic.main.view_forecast_cardview.view.tvDay
import kotlinx.android.synthetic.main.view_forecast_cardview.view.tvDegrees
import kotlin.properties.Delegates

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    var results: List<ForecastThumbnail> by Delegates.observable(
            emptyList()
    ) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder =
            ForecastViewHolder(
                    parent.inflate(R.layout.view_forecast_cardview)
            )

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int {
        return 5
    }

    class ForecastViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ForecastThumbnail) {
            view.tvDegrees.text = "${item.temperature.value}${item.temperature.unit.postFix}"
            view.ivIcon.loadImageWithFitCenter( item.iconUrl)
            view.tvDay.text = item.dayOfTheWeek
        }
    }
}
