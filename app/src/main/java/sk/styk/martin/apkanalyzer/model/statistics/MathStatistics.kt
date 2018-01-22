package sk.styk.martin.apkanalyzer.model.statistics

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

/**
 * Math Statistics collected about attributes of Apk file
 *
 * @author Martin Styk
 * @version 28.07.2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class MathStatistics(val arithmeticMean: BigDecimal,
                          var median: BigDecimal,
                          var max: BigDecimal,
                          var min: BigDecimal,
                          var variance: BigDecimal,
                          var deviation: BigDecimal) : Parcelable
