package sk.styk.martin.apkanalyzer.util

import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import sk.styk.martin.apkanalyzer.BuildConfig

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */
object AdUtils {

    const val isAdEnabled = BuildConfig.FLAVOR == AppFlavour.FREE

    private val defaultRequest: AdRequest = AdRequest.Builder()
            .addTestDevice("72FEA8FEF46331E756C654CF5C76557C")
            .addTestDevice("BF33B5A261F50A48670A239AF55828FF")
            .build()

    fun displayAd(adView: AdView, callback: AdLoadedListener? = null) {
        if (isAdEnabled) {
            adView.apply {
                loadAd(defaultRequest)
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        adView.visibility = View.VISIBLE
                        callback?.onAdLoaded()
                    }
                }
            }
        }
    }

    interface AdLoadedListener {
        fun onAdLoaded()
    }

}