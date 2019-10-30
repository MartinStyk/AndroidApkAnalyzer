package sk.styk.martin.apkanalyzer.util

import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import sk.styk.martin.apkanalyzer.BuildConfig
import sk.styk.martin.apkanalyzer.R

object AdUtils {

    const val isAdEnabled = BuildConfig.SHOW_ADS

    private val defaultRequest: AdRequest = AdRequest.Builder()
            .addTestDevice("72FEA8FEF46331E756C654CF5C76557C")
            .addTestDevice("BF33B5A261F50A48670A239AF55828FF")
            .build()

    fun displayAd(adView: AdView, container: View? = null, callback: AdLoadedListener? = null) {
        if (isAdEnabled) {
            adView.apply {
                loadAd(defaultRequest)
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        adView.visibility = View.VISIBLE
                        container?.visibility = View.VISIBLE
                        container?.setPadding(0, adView.resources.getDimensionPixelOffset(R.dimen.padding_ad), 0, 0)
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