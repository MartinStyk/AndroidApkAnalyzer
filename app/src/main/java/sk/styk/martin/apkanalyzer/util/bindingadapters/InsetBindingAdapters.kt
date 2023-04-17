package sk.styk.martin.apkanalyzer.util.bindingadapters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter
import kotlin.math.roundToInt

// inspired by https://medium.com/androiddevelopers/windowinsets-listeners-to-layouts-8f9ccc8fa4d1
@BindingAdapter(
    "paddingLeftSystemWindowInsets",
    "paddingTopSystemWindowInsets",
    "paddingRightSystemWindowInsets",
    "paddingBottomSystemWindowInsets",
    requireAll = false,
)
fun applySystemWindowsPadding(
    view: View,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean,
) {
    val initialPadding = recordInitialPadding(view)
    view.doOnApplyWindowInsets(view) { insets ->
        val left = if (applyLeft) insets.systemWindowInsetLeft else 0
        val top = if (applyTop) insets.systemWindowInsetTop else 0
        val right = if (applyRight) insets.systemWindowInsetRight else 0
        val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0
        view.setPadding(
            initialPadding.left + left,
            initialPadding.top + top,
            initialPadding.right + right,
            initialPadding.bottom + bottom,
        )
    }
}

@BindingAdapter(
    "marginLeftSystemWindowInsets",
    "marginTopSystemWindowInsets",
    "marginRightSystemWindowInsets",
    "marginBottomSystemWindowInsets",
    requireAll = false,
)
fun applySystemWindowsMargin(
    view: View,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean,
) {
    val layoutParams = view.layoutParams as? ViewGroup.MarginLayoutParams
    layoutParams?.let {
        val initialMargin = recordInitialMargin(it)
        view.doOnApplyWindowInsets(view) { insets ->
            val left = if (applyLeft) insets.systemWindowInsetLeft else 0
            val top = if (applyTop) insets.systemWindowInsetTop else 0
            val right = if (applyRight) insets.systemWindowInsetRight else 0
            val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0
            it.setMargins(
                initialMargin.left + left,
                initialMargin.top + top,
                initialMargin.right + right,
                initialMargin.bottom + bottom,
            )
        }
    }
}

@BindingAdapter(
    "paddingLeftSystemWindowInsetsFraction",
    "paddingTopSystemWindowInsetsFraction",
    "paddingRightSystemWindowInsetsFraction",
    "paddingBottomSystemWindowInsetsFraction",
    requireAll = false,
)
fun applySystemWindowsPaddingFraction(
    view: View,
    applyLeftFraction: Float?,
    applyTopFraction: Float?,
    applyRightFraction: Float?,
    applyBottomFraction: Float?,
) {
    view.doOnApplyWindowInsets(view) { insets ->
        val left = applyLeftFraction?.let { (insets.systemWindowInsetLeft * it).roundToInt() }
            ?: view.paddingLeft
        val top = applyTopFraction?.let { (insets.systemWindowInsetTop * it).roundToInt() }
            ?: view.paddingTop
        val right = applyRightFraction?.let { (insets.systemWindowInsetRight * it).roundToInt() }
            ?: view.paddingRight
        val bottom = applyBottomFraction?.let { (insets.systemWindowInsetBottom * it).roundToInt() }
            ?: view.paddingBottom
        view.setPadding(left, top, right, bottom)
    }
}

fun View.doOnApplyWindowInsets(view: View, f: (WindowInsetsCompat) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
        f(insets)
        insets
    }
    requestApplyInsetsWhenAttached()
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        ViewCompat.requestApplyInsets(this)
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                ViewCompat.requestApplyInsets(v)
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

@BindingAdapter("matchTopInsetHeight")
fun matchTopInsetHeight(view: View, matchTopInsetHeight: Boolean) {
    if (!matchTopInsetHeight) {
        return
    }

    val layoutParams = view.layoutParams as? ViewGroup.MarginLayoutParams
    layoutParams?.let {
        view.doOnApplyWindowInsets(view) { insets ->
            it.height = insets.systemWindowInsetTop
        }
    }
}

@BindingAdapter("minHeightTopSystemWindowInsets")
fun setMinHeighTopSystemWindowInsets(view: View, minHeightTopSystemWindowInsets: Boolean) {
    if (!minHeightTopSystemWindowInsets) {
        return
    }

    view.doOnApplyWindowInsets(view) { insets ->
        view.minimumHeight = insets.systemWindowInsetTop
        view.requestLayout()
    }
}

data class ValuesHolder(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun recordInitialPadding(view: View) = ValuesHolder(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

private fun recordInitialMargin(layoutParams: ViewGroup.MarginLayoutParams) = ValuesHolder(layoutParams.marginStart, layoutParams.topMargin, layoutParams.marginEnd, layoutParams.bottomMargin)
