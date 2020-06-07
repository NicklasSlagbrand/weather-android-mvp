package dk.nicklasslagbrand.forecast.utils.ui

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import dk.nicklasslagbrand.forecast.R
import kotlinx.android.synthetic.main.view_forecast_cardview.view.tvDay
import kotlinx.android.synthetic.main.view_forecast_cardview.view.tvDegrees

class ForecastCardView : CardView {
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        View.inflate(context, R.layout.view_forecast_cardview, this)
    }

    var day: String
        set(value) {
            tvDay?.text = value
        }
        get() {
            return tvDay?.text.toString()
        }

    var degrees: String
        set(value) {
            tvDegrees?.text = value
        }
        get() {
            return tvDegrees?.text.toString()
        }
}
