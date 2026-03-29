package sk.styk.martin.apkanalyzer.util.bindingadapters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter


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
    view.doOnApplyWindowInsets { insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
        val left = if (applyLeft) systemBars.left else 0
        val top = if (applyTop) systemBars.top else 0
        val right = if (applyRight) systemBars.right else 0
        val bottom = if (applyBottom) systemBars.bottom else 0
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
        view.doOnApplyWindowInsets { insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            val left = if (applyLeft) systemBars.left else 0
            val top = if (applyTop) systemBars.top else 0
            val right = if (applyRight) systemBars.right else 0
            val bottom = if (applyBottom) systemBars.bottom else 0
            it.setMargins(
                initialMargin.left + left,
                initialMargin.top + top,
                initialMargin.right + right,
                initialMargin.bottom + bottom,
            )
        }
    }
}

fun View.doOnApplyWindowInsets(f: (WindowInsetsCompat) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
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

data class ValuesHolder(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun recordInitialPadding(view: View) = ValuesHolder(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

private fun recordInitialMargin(layoutParams: ViewGroup.MarginLayoutParams) = ValuesHolder(layoutParams.marginStart, layoutParams.topMargin, layoutParams.marginEnd, layoutParams.bottomMargin)
