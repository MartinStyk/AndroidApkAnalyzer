package sk.styk.martin.apkanalyzer.util

import java.text.DecimalFormat

object BigDecimalFormatter {
    private var commonFormat: DecimalFormat? = null

    fun getCommonFormat(): DecimalFormat {
        if (commonFormat == null) {
            commonFormat = getFormat(2, 2)
        }
        return commonFormat!!
    }

    fun getFormat(minFractions: Int, maxFractions: Int): DecimalFormat {
        val customFormat = DecimalFormat()
        customFormat.maximumFractionDigits = maxFractions
        customFormat.minimumFractionDigits = minFractions

        return customFormat
    }
}
