package sk.styk.martin.apkanalyzer.views.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import sk.styk.martin.apkanalyzer.R

private const val MAX_CLICK_DURATION = 500

@SuppressLint("ViewConstructor")
class ClickableMarkerView(context: Context, private val callback: OnMarkerClickListener) : MarkerView(context, R.layout.view_chart_marker) {

    interface OnMarkerClickListener {
        fun onMarkerClick(entry: Entry)
    }

    var drawingPosX = 0f
        private set
    var drawingPosY = 0f
        private set

    private var startClickTime: Long = 0

    override fun refreshContent(e: Entry, highlight: Highlight) {
        e.data ?: return
        val appList = e.data as List<*>
        rootView.findViewById<TextView>(R.id.tvContent).text = String.format(context.getString(R.string.show_apps, appList.size))

        rootView.setOnClickListener { callback.onMarkerClick(entry = e) }

        super.refreshContent(e, highlight)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startClickTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_UP -> {
                val clickDuration = System.currentTimeMillis() - startClickTime
                if (clickDuration < MAX_CLICK_DURATION) {
                    rootView.performClick()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        super.draw(canvas, posX, posY)
        val offset = getOffsetForDrawingAtPoint(posX, posY)
        this.drawingPosX = posX + offset.x
        this.drawingPosY = posY + offset.y
    }
}
