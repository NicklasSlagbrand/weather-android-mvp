package dk.shape.forecast.utils.ui.extension

import com.github.mikephil.charting.charts.LineChart

fun LineChart.applyLocationDetailsStyle() {
     setViewPortOffsets(50f, 50f, 50f, 50f)
     description.isEnabled = false
     setTouchEnabled(false)
     isDragEnabled = false
     setScaleEnabled(false)
     setPinchZoom(false)
     setDrawGridBackground(false)
     maxHighlightDistance = 300f
     axisLeft.isEnabled = false
     xAxis.isEnabled = false
     axisRight.isEnabled = false
}