package sk.styk.martin.apkanalyzer.views.chart

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.Entry

class MyBarChart : BarChart {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    var onMarkerClickListener: ClickableMarkerView.OnMarkerClickListener? = null

    override fun init() {
        super.init()
        marker = ClickableMarkerView(
            context,
            object : ClickableMarkerView.OnMarkerClickListener {
                override fun onMarkerClick(entry: Entry) {
                    onMarkerClickListener?.onMarkerClick(entry)
                }
            },
        ).apply { chartView = this@MyBarChart }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var handled = true
        // if there is no marker view or drawing marker is disabled
        if (isShowingMarker && this.marker is ClickableMarkerView) {
            val markerView = this.marker as ClickableMarkerView
            val rect = Rect(markerView.drawingPosX.toInt(), markerView.drawingPosY.toInt(), markerView.drawingPosX.toInt() + markerView.width, markerView.drawingPosY.toInt() + markerView.height)
            if (rect.contains(event.x.toInt(), event.y.toInt())) {
                // touch on marker -> dispatch touch event in to marker
                markerView.dispatchTouchEvent(event)
            } else {
                handled = super.onTouchEvent(event)
            }
        } else {
            handled = super.onTouchEvent(event)
        }
        return handled
    }

    private val isShowingMarker: Boolean
        get() = mMarker != null && isDrawMarkersEnabled && valuesToHighlight()
}
