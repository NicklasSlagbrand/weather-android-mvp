package dk.shape.forecast.usecases.places.ui.details

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import dk.shape.forecast.AppConfig
import dk.shape.forecast.R
import dk.shape.forecast.entities.Forecast
import dk.shape.forecast.entities.ForecastThumbnail
import dk.shape.forecast.entities.Temperature
import dk.shape.forecast.entities.toFloat
import dk.shape.forecast.usecases.places.ui.list.PlacesAdapter
import dk.shape.forecast.utils.ui.BaseActivity
import dk.shape.forecast.utils.ui.celsiusToColor
import dk.shape.forecast.utils.ui.extension.applyLocationDetailsStyle
import dk.shape.forecast.utils.ui.extension.loadImage
import dk.shape.forecast.utils.ui.extension.toTitleCase
import dk.shape.forecast.utils.ui.extension.visible
import dk.shape.forecast.utils.ui.toImageUrl
import kotlinx.android.synthetic.main.activity_place_details.chart1
import kotlinx.android.synthetic.main.activity_place_details.clRoot
import kotlinx.android.synthetic.main.activity_place_details.ivWeatherBackground
import kotlinx.android.synthetic.main.activity_place_details.ivWeatherIcon
import kotlinx.android.synthetic.main.activity_place_details.rvForecast
import kotlinx.android.synthetic.main.activity_place_details.tvDate
import kotlinx.android.synthetic.main.activity_place_details.tvDegrees
import kotlinx.android.synthetic.main.activity_place_details.tvEndTime
import kotlinx.android.synthetic.main.activity_place_details.tvFeelsLike
import kotlinx.android.synthetic.main.activity_place_details.tvLocationName
import kotlinx.android.synthetic.main.activity_place_details.tvStartTime
import kotlinx.android.synthetic.main.activity_place_details.tvWeatherDescription
import kotlinx.android.synthetic.main.places.placesRecyclerView
import java.util.ArrayList

class PlaceDetailsActivity : BaseActivity() {

    private val config by lazy { AppConfig.initPlaceDetailsConfig(activity = this) }
    private var woeId: String? = null
    private var locationName: String? = null
    private var lon: Double? = null
    private var lat: Double? = null

    override fun applyExternalArguments(args: Bundle) {
        woeId = args.getString(WOE_ID)
        lon = args.getDouble(LON_ID)
        lat = args.getDouble(LAT_ID)
        locationName = args.getString(NAME_ID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)
        initViews()

        if (lon != null && lat != null){
            config.interactor.getForecast(lon!!, lat!!)
        } else {
            finish()
        }
    }

    private fun initViews() {
        setupChart()
        config.presenter.onStateChanged = { state ->
            when (state) {
                is PlaceDetailsPresenter.State.ShowForecast -> {
                    setForecastData(state.forecast)
                    showDailyForecast(state.forecast.dailyWeather)
                    showDailyTemperature(state.forecast.hourly)
                }
            }
        }
    }

    private fun setupChart() {
        chart1.applyLocationDetailsStyle()
    }

    private fun setForecastData(forecast: Forecast) {
        ivWeatherBackground.loadImage(forecast.weatherCondition.toImageUrl())

        val resource = forecast.temperature.value.celsiusToColor()
        clRoot.setBackgroundColor(ContextCompat.getColor(this, resource))
        tvLocationName.text = locationName
        tvWeatherDescription.text = forecast.weatherDescription.toTitleCase()
        tvDegrees.text = getString(R.string.forecast_degrees,
                forecast.temperature.value,
                forecast.temperature.unit.postFix)

        ivWeatherIcon.loadImage(url = forecast.iconUrl)

        tvStartTime.text = forecast.startDateString
        tvEndTime.text = forecast.endDateString
        tvFeelsLike.text = getString(R.string.forecast_feels_like,
                forecast.currentWeather.feelsLike.value,
                forecast.currentWeather.feelsLike.unit.postFix)

        tvDate.text = forecast.currentDateString

    }

    fun showDailyTemperature(hourlyTemperature: List<Temperature>){
        val values = ArrayList<Entry>()
        for (i in hourlyTemperature.indices){
            values.add(Entry(i.toFloat(), hourlyTemperature[i].toFloat()))
        }

        LineData(ForeCastLineSet(values)).apply {
            setValueTextSize(12f)
            setValueFormatter(DefaultValueFormatter (0))
            setValueTextColor(Color.WHITE)
        }.also {
            chart1.data = it
        }
        chart1.apply {
            visible()
            legend.isEnabled = false
        }
    }

    fun showDailyForecast(dailyForecast: List<ForecastThumbnail>){
        rvForecast.adapter = ForecastAdapter().apply {
            results = dailyForecast
        }
    }

    companion object {
        private const val WOE_ID = "woe_Id"
        private const val NAME_ID = "name_Id"
        private const val LON_ID = "lon_Id"
        private const val LAT_ID = "lat_Id"

        fun startActivity(
                context: Context,
                woeId: String,
                name: String,
                lon: Double,
                lat: Double
        ) {
            context.startActivity(
                    Intent(context, PlaceDetailsActivity::class.java)
                            .putExtra(WOE_ID, woeId)
                            .putExtra(NAME_ID, name)
                            .putExtra(LON_ID, lon)
                            .putExtra(LAT_ID, lat)
            )
        }
    }
}
