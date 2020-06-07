package dk.nicklasslagbrand.forecast.usecases.places.ui.details

import android.graphics.Color
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import java.util.ArrayList

class ForeCastLineSet(values: ArrayList<Entry>): LineDataSet(values, "") {
    init {
        mode = Mode.CUBIC_BEZIER
        cubicIntensity = 0.2f
        setDrawFilled(true)
        mode = LineDataSet.Mode.CUBIC_BEZIER
        cubicIntensity = 0.2f
        setDrawFilled(true)
        setDrawCircles(true)
        lineWidth = 1.8f
        circleRadius = 4f
        setCircleColor(Color.WHITE)
//        highLightColor = Color.rgb(244, 117, 117)
        color = Color.WHITE
//        fillColor = Color.BLACK
        fillAlpha = 0
        setDrawHorizontalHighlightIndicator(false)
    }
}

