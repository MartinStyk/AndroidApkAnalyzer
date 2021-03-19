package sk.styk.martin.apkanalyzer.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.view_chart_marker.view.*
import sk.styk.martin.apkanalyzer.R

@SuppressLint("ViewConstructor")
class ClickableMarkerView(context: Context, private val callback: OnMarkerClickListener) : MarkerView(context, R.layout.view_chart_marker) {

    interface OnMarkerClickListener{
        fun onMarkerClick(apps: List<String>)
    }

    var drawingPosX: Float = 0.toFloat()
    var drawingPosY: Float = 0.toFloat()
    private val MAX_CLICK_DURATION = 500
    private var startClickTime: Long = 0

    init {
        chart_marker_container.isClickable = true
    }

    override fun refreshContent(e: Entry, highlight: Highlight) {
        e.data ?: return
        val appList = e.data as List<String>
        tvContent.text = String.format(context.getString(R.string.show_apps, appList.size))

        chart_marker_container.setOnClickListener {
            callback.onMarkerClick(appList)
        }


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
                    chart_marker_container.performClick()
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