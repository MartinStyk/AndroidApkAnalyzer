package sk.styk.martin.apkanalyzer.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.BarChart;

public class MyBarChart extends BarChart {

    public MyBarChart(Context context) {
        super(context);
    }

    public MyBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = true;
        // if there is no marker view or drawing marker is disabled
        if (isShowingMarker() && this.getMarker() instanceof ClickableMarkerView) {
            ClickableMarkerView markerView = (ClickableMarkerView) this.getMarker();
            Rect rect = new Rect((int) markerView.getDrawingPosX(), (int) markerView.getDrawingPosY(), (int) markerView.getDrawingPosX() + markerView.getWidth(), (int) markerView.getDrawingPosY() + markerView.getHeight());
            if (rect.contains((int) event.getX(), (int) event.getY())) {
                // touch on marker -> dispatch touch event in to marker
                markerView.dispatchTouchEvent(event);
            } else {
                handled = super.onTouchEvent(event);
            }
        } else {
            handled = super.onTouchEvent(event);
        }
        return handled;
    }

    private boolean isShowingMarker() {
        return mMarker != null && isDrawMarkersEnabled() && valuesToHighlight();
    }
}

