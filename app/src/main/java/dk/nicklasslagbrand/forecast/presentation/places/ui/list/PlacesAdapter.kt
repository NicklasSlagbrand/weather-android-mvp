package dk.nicklasslagbrand.forecast.presentation.places.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.nicklasslagbrand.forecast.R
import dk.nicklasslagbrand.forecast.domain.entities.Place
import dk.nicklasslagbrand.forecast.utils.extension.colorResourceToStateList
import dk.nicklasslagbrand.forecast.utils.extension.toColorResource
import kotlinx.android.synthetic.main.place_item.view.*

class PlacesAdapter(private val configs: List<Place>) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    var clickListener: (Place) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.place_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val config = configs[position]
        val temperatureColorResource =  config.temperature.toColorResource()

        holder.view.backgroundTemperature.imageTintList = temperatureColorResource.colorResourceToStateList(context = holder.view.context)
        holder.view.textTemperature.text = "${config.temperature.value}${config.temperature.unit.postFix}"
        holder.view.textCity.text = config.city
        holder.view.textCountry.text = config.country
        holder.view.setOnClickListener {clickListener(config)}

    }

    override fun getItemCount(): Int {
        return configs.size
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
