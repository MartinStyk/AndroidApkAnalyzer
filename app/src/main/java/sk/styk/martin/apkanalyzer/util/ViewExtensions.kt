package sk.styk.martin.apkanalyzer.util

import android.view.View
import android.view.ViewTreeObserver

inline fun View.onViewLaidOut(crossinline onLayoutAction: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            onLayoutAction()
        }
    })
}