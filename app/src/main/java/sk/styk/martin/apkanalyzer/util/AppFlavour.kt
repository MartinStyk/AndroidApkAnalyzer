package sk.styk.martin.apkanalyzer.util

import sk.styk.martin.apkanalyzer.BuildConfig

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */
object AppFlavour {
    const val FREE = "free"
    const val PREMIUM = "premium"

    val isPremium = PREMIUM == BuildConfig.FLAVOR
}