package sk.styk.martin.apkanalyzer.util

import android.os.Parcel
import android.os.Parcelable

import java.math.BigDecimal

/**
 * Pair of number and its percentage part of total count
 * Used in statistics task
 *
 * @author Martin Styk
 * @version 21.01.2016.
 */
class PercentagePair : Parcelable {
    var count: Number
    var percentage: BigDecimal

    constructor(count: Number, total: Int) {
        this.count = count
        this.percentage = PercentagePair.Companion.getPercentage(count.toDouble(), total.toDouble())
    }

    override fun toString(): String {
        return count.toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(percentage) + "%)"
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeSerializable(this.count)
        dest.writeSerializable(this.percentage)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PercentagePair

        if (count != other.count) return false
        if (percentage != other.percentage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = count.hashCode()
        result = 31 * result + percentage.hashCode()
        return result
    }

    protected constructor(`in`: Parcel) {
        this.count = `in`.readSerializable() as Number
        this.percentage = `in`.readSerializable() as BigDecimal
    }

    companion object {

        val CREATOR: Parcelable.Creator<PercentagePair> = object : Parcelable.Creator<PercentagePair> {
            override fun createFromParcel(source: Parcel): PercentagePair {
                return PercentagePair(source)
            }

            override fun newArray(size: Int): Array<PercentagePair?> {
                return arrayOfNulls(size)
            }
        }

        fun getPercentage(part: Double, whole: Double): BigDecimal {
            return BigDecimal(part * 100 / whole)
        }
    }

}