package sk.styk.martin.apkanalyzer.ui.dialogs

import android.app.Activity
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.file.AppOperations

class PromoDialog {

    fun showPromoDialog(context: Activity) {
        FancyDialog(context).apply {
            title = R.string.upgrade_premium
            message = R.string.upgrade_premium_message
            positiveBtnText = R.string.upgrade
            negativeBtnText = R.string.dismiss
            positiveButtonColor = R.color.premium
            negativeButtonColor = R.color.grey_500
            gifImageResource = R.drawable.promo_anim
            cancelable = true
            actionListener = object : FancyDialog.FancyDialogAction {
                override fun onPositiveButtonClicked(context: Activity) = AppOperations.installPremium(context)
                override fun onNegativeButtonClicked(context: Activity) {}
            }
        }.build().show()
    }
}
