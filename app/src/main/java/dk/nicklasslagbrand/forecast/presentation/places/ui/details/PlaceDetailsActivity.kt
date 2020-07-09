package dk.nicklasslagbrand.forecast.presentation.places.ui.details

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import dk.nicklasslagbrand.forecast.utils.AppConfig
import dk.nicklasslagbrand.forecast.R
import dk.nicklasslagbrand.forecast.domain.entities.Forecast
import dk.nicklasslagbrand.forecast.domain.entities.ForecastThumbnail
import dk.nicklasslagbrand.forecast.domain.entities.Temperature
import dk.nicklasslagbrand.forecast.domain.entities.toFloat
import dk.nicklasslagbrand.forecast.presentation.BaseActivity
import dk.nicklasslagbrand.forecast.utils.extension.celsiusToColor
import dk.nicklasslagbrand.forecast.utils.extension.ForeCastLineSet
import dk.nicklasslagbrand.forecast.utils.extension.applyLocationDetailsStyle
import dk.nicklasslagbrand.forecast.utils.extension.gone
import dk.nicklasslagbrand.forecast.utils.extension.loadImage
import dk.nicklasslagbrand.forecast.utils.extension.toTitleCase
import dk.nicklasslagbrand.forecast.utils.extension.visible
import dk.nicklasslagbrand.forecast.utils.extension.toImageUrl
import kotlinx.android.synthetic.main.activity_place_details.chart1
import kotlinx.android.synthetic.main.activity_place_details.clCurrentWeather
import kotlinx.android.synthetic.main.activity_place_details.clRoot
import kotlinx.android.synthetic.main.activity_place_details.clTemp
import kotlinx.android.synthetic.main.activity_place_details.ivWeatherBackground
import kotlinx.android.synthetic.main.activity_place_details.ivWeatherIcon
import kotlinx.android.synthetic.main.activity_place_details.pbPlaceDetails
import kotlinx.android.synthetic.main.activity_place_details.rvDailyForecast
import kotlinx.android.synthetic.main.activity_place_details.rvForecast
import kotlinx.android.synthetic.main.activity_place_details.tvDate
import kotlinx.android.synthetic.main.activity_place_details.tvDegrees
import kotlinx.android.synthetic.main.activity_place_details.tvEndTime
import kotlinx.android.synthetic.main.activity_place_details.tvFeelsLike
import kotlinx.android.synthetic.main.activity_place_details.tvStartTime
import kotlinx.android.synthetic.main.activity_place_details.tvWeatherDescription
import java.util.ArrayList

class PlaceDetailsActivity : BaseActivity() {

    private val config by lazy { AppConfig.initPlaceDetailsConfig() }
    private var lon: Double? = null
    private var lat: Double? = null

    override fun applyExternalArguments(args: Bundle) {
        lon = args.getDouble(LON_ID)
        lat = args.getDouble(LAT_ID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)
        initViews()

        if (lon != null && lat != null){
            config.presenter.getForecast(lon!!, lat!!)
        } else {
            finish()
        }
        subscribeToData()

    }

    private fun subscribeToData() {
        config.presenter.onStateChanged = { state ->
            when (state) {
                is PlaceDetailsPresenter.State.ShowForecast -> {
                    setForecastData(state.forecast)
                    showDailyForecast(state.forecast.dailyWeather)
                    showDailyTemperature(state.forecast.hourly)
                    pbPlaceDetails.gone()
                    clCurrentWeather.visible()
                    clTemp.visible()
                    rvDailyForecast.visible()
                }
                is PlaceDetailsPresenter.State.Loading ->{
                    pbPlaceDetails.visible()
                }
            }
        }
    }

    private fun initViews() {
        setupChart()
    }

    private fun setupChart() {
        chart1.applyLocationDetailsStyle()
    }

    private fun setForecastData(forecast: Forecast) {
        ivWeatherBackground.loadImage(forecast.weatherCondition.toImageUrl())

        val resource = forecast.temperature.value.celsiusToColor()
        clRoot.setBackgroundColor(ContextCompat.getColor(this, resource))
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

    private fun showDailyTemperature(hourlyTemperature: List<Temperature>){
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

    private fun showDailyForecast(dailyForecast: List<ForecastThumbnail>){
        rvForecast.adapter = ForecastAdapter().apply {
            results = dailyForecast
        }
    }

    companion object {
        private const val LON_ID = "lon_Id"
        private const val LAT_ID = "lat_Id"

        fun startActivity(
                context: Context,
                lon: Double,
                lat: Double
        ) {
            context.startActivity(
                    Intent(context, PlaceDetailsActivity::class.java)
                            .putExtra(LON_ID, lon)
                            .putExtra(LAT_ID, lat)
            )
        }
    }
}
