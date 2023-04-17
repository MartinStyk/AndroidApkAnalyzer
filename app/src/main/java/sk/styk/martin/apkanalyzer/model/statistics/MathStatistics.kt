package sk.styk.martin.apkanalyzer.model.statistics

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@SuppressLint("ParcelCreator")
@Parcelize
data class MathStatistics(
    val arithmeticMean: BigDecimal,
    var median: BigDecimal,
    var max: BigDecimal,
    var min: BigDecimal,
    var variance: BigDecimal,
    var deviation: BigDecimal,
) : Parcelable
