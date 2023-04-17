package sk.styk.martin.apkanalyzer.util

import sk.styk.martin.apkanalyzer.BuildConfig

object AppFlavour {
    const val FREE = "free"
    const val PREMIUM = "premium"

    val isPremium = PREMIUM == BuildConfig.FLAVOR
}
