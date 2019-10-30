package sk.styk.martin.apkanalyzer.model.statistics

import java.math.BigDecimal
import java.util.*

class MathStatisticsBuilder(private val data: FloatArray) {

    fun build(): MathStatistics {

        if (data.isEmpty()) {
            throw IllegalArgumentException()
        }

        Arrays.sort(data)

        val arithmeticMean = BigDecimal(computeMean())
        val variance = BigDecimal(computeVariance(arithmeticMean.toFloat()))
        val deviation = BigDecimal(Math.sqrt(variance.toFloat().toDouble()))

        return MathStatistics(
                arithmeticMean = arithmeticMean,
                median = BigDecimal(computeMedian().toDouble()),
                max = BigDecimal(data[data.size - 1].toDouble()),
                min = BigDecimal(data[0].toDouble()),
                variance = variance,
                deviation = deviation
        )
    }


    private fun computeMean(): Double = data.sum() / data.size.toDouble()

    private fun computeMedian(): Float {
        return if (data.size % 2 == 0)
            (data[data.size / 2 - 1] + data[data.size / 2]) / 2.0f
        else
            data[data.size / 2]
    }


    private fun computeVariance(mean: Float): Double {
        var temp = 0.0
        for (a in data)
            temp += (a - mean) * (a - mean)
        return temp / (data.size - 1)
    }

}
