package sk.styk.martin.apkanalyzer.ui.activity.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.shashank.sony.fancygifdialoglib.FancyGifDialog
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.file.AppOperations

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */
class PromoDialog {

    interface PromoDialogController {
        fun onPromoDialogShowRequested()
    }

    @SuppressLint("ResourceType")
    fun showPromoDialog(context: Activity) {

        FancyGifDialog.Builder(context)
                .setTitle(context.getString(R.string.upgrade_premium))
                .setMessage(context.getString(R.string.upgrade_premium_message))
                .setNegativeBtnText(context.getString(R.string.dismiss))
                .setPositiveBtnBackground(context.getString(R.color.premium))
                .setPositiveBtnText(context.getString(R.string.upgrade))
                .setGifResource(R.drawable.promo_anim)
                .setNegativeBtnBackground(context.getString(R.color.grey_500))
                .isCancellable(true)
                .OnPositiveClicked {
                    AppOperations.installPremium(context)
                }
                .OnNegativeClicked{}
                .build();

    }
}