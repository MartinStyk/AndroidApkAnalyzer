package sk.styk.martin.apkanalyzer.util.components

import android.view.View
import com.google.android.material.snackbar.Snackbar
import sk.styk.martin.apkanalyzer.util.TextInfo

data class SnackBarComponent(
    val message: TextInfo,
    val duration: Int = Snackbar.LENGTH_LONG,
    val action: TextInfo? = null,
    val callback: View.OnClickListener? = null,
)

fun SnackBarComponent.toSnackbar(seekFromView: View): Snackbar {
    val snack = Snackbar.make(seekFromView, message.getText(seekFromView.context), duration)
    if (action != null) {
        snack.setAction(action.getText(seekFromView.context), callback)
    }
    return snack
}
