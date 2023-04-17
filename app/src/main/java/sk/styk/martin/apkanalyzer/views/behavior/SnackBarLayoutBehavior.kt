package sk.styk.martin.apkanalyzer.views.behavior

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import kotlin.math.min

class SnackBarLayoutBehavior : CoordinatorLayout.Behavior<View> {

    constructor() : super()
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val translationY = min(0f, dependency.translationY - dependency.height)
        child.translationY = translationY
        return true
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: View, dependency: View) {
        if (child.translationY != 0.0f) {
            val viewPropertyAnimator = child.animate().translationY(0.0f)
            viewPropertyAnimator.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    viewPropertyAnimator.setListener(null)

                    // This method manually triggers the CoordinatorLayout to dispatch actual view state
                    parent.dispatchDependentViewsChanged(child)
                }
            })
            viewPropertyAnimator.start()
        }
    }
}
