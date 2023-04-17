package sk.styk.martin.apkanalyzer.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import pl.droidsonroids.gif.GifImageView
import sk.styk.martin.apkanalyzer.R

class FancyDialog(private val activity: Activity) {
    interface FancyDialogAction {
        fun onPositiveButtonClicked(context: Activity)
        fun onNegativeButtonClicked(context: Activity)
    }

    @StringRes
    var title: Int? = null

    @StringRes
    var message: Int? = null

    @StringRes
    var positiveBtnText: Int? = null

    @StringRes
    var negativeBtnText: Int? = null

    @ColorRes
    var positiveButtonColor: Int = R.color.accent

    @ColorRes
    var positiveButtonTextColor: Int = R.color.colorWhite

    @ColorRes
    var negativeButtonColor: Int = R.color.grey_500

    @ColorRes
    var negativeButtonTextColor: Int = R.color.colorWhite

    @DrawableRes
    var gifImageResource: Int = 0
    var cancelable: Boolean = false
    var actionListener: FancyDialogAction = object : FancyDialogAction {
        override fun onPositiveButtonClicked(context: Activity) {}
        override fun onNegativeButtonClicked(context: Activity) {}
    }

    private lateinit var dialog: Dialog

    fun build(): Dialog {
        dialog = Dialog(this.activity).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(cancelable)
            setContentView(R.layout.dialog_fancy)
        }

        dialog.findViewById<TextView>(R.id.title).apply {
            text = title?.let { activity.getText(it) } ?: ""
        }
        dialog.findViewById<TextView>(R.id.message).apply {
            text = message?.let { activity.getText(it) } ?: ""
        }
        dialog.findViewById<GifImageView>(R.id.gifImageView).apply {
            setImageResource(gifImageResource)
        }
        dialog.findViewById<Button>(R.id.negativeBtn).apply {
            text = negativeBtnText?.let { activity.getText(it) } ?: ""
            setTextColor(ContextCompat.getColor(activity, negativeButtonTextColor))
            (background as GradientDrawable).setColor(ContextCompat.getColor(activity, negativeButtonColor))
            setOnClickListener {
                actionListener.onNegativeButtonClicked(activity)
                dialog.dismiss()
            }
        }
        dialog.findViewById<Button>(R.id.positiveBtn).apply {
            text = positiveBtnText?.let { activity.getText(it) } ?: ""
            setTextColor(ContextCompat.getColor(activity, positiveButtonTextColor))
            (background as GradientDrawable).setColor(ContextCompat.getColor(activity, positiveButtonColor))
            setOnClickListener {
                actionListener.onPositiveButtonClicked(activity)
                dialog.dismiss()
            }
        }
        return dialog
    }
}
