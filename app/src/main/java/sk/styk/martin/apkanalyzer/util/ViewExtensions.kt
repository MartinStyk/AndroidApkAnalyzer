package sk.styk.martin.apkanalyzer.util

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import timber.log.Timber

inline fun View.onViewLaidOut(crossinline onLayoutAction: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            onLayoutAction()
        }
    })
}

fun View.hideKeyboard() {
    if (windowToken == null) {
        Timber.w("hideKeyboard failed: windowToken is null!")
    } else {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun View.isNightMode(): Boolean {
    return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}
