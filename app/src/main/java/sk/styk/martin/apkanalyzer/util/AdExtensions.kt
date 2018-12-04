package sk.styk.martin.apkanalyzer.util

import com.google.android.gms.ads.AdRequest

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */
fun AdRequest.Builder.buildDefault(): AdRequest = this
        .addTestDevice("72FEA8FEF46331E756C654CF5C76557C")
        .addTestDevice("BF33B5A261F50A48670A239AF55828FF")
        .build()