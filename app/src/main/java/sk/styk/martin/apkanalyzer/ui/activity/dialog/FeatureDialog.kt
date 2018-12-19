package sk.styk.martin.apkanalyzer.ui.activity.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.shashank.sony.fancygifdialoglib.FancyGifDialog
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.ColorThemeHelper
import sk.styk.martin.apkanalyzer.util.DARK_THEME
import sk.styk.martin.apkanalyzer.util.LIGHT_THEME

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */
class FeatureDialog {

    companion object {
        const val fromVersion: Int = 38
    }

    @StringRes
    val title: Int = R.string.color_theme_promo_title
    @StringRes
    val message: Int = R.string.color_theme_promo_message
    @StringRes
    val negativeBtn: Int = R.string.color_theme_promo_stay_light
    @ColorRes
    val negativeBtnColor: Int = R.color.colorLightGrey
    @StringRes
    val positiveBtn: Int = R.string.color_theme_promo_switch_dark
    @ColorRes
    val positiveeBtnColor: Int = R.color.grey_500
    @DrawableRes
    val gifResource: Int = R.drawable.feature_anim

    val actions = object : FeatureDialogAction {
        override fun onPositiveButtonClicked(context: Activity) = ColorThemeHelper.setTheme(DARK_THEME, context)
        override fun onNegativeButtonClicked(context: Activity) = ColorThemeHelper.setTheme(LIGHT_THEME, context)
    }

    interface FeatureDialogController {
        fun onFeatureDialogShowRequested()
    }

    interface FeatureDialogAction {
        fun onPositiveButtonClicked(context: Activity)
        fun onNegativeButtonClicked(context: Activity)
    }

    @SuppressLint("ResourceType")
    fun showFeatureDialog(context: Activity) {

        FancyGifDialog.Builder(context)
                .setTitle(context.getString(title))
                .setMessage(context.getString(message))
                .setNegativeBtnText(context.getString(negativeBtn))
                .setPositiveBtnBackground(context.getString(R.color.grey_500))
                .setPositiveBtnText(context.getString(positiveBtn))
                .setGifResource(gifResource)
                .setNegativeBtnBackground(context.getString(R.color.grey_500))
                .isCancellable(true)
                .OnPositiveClicked {
                    actions.onPositiveButtonClicked(context)
                }
                .OnNegativeClicked {
                    actions.onNegativeButtonClicked(context)
                }
                .build();

    }


}