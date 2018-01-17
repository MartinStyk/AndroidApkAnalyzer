package sk.styk.martin.apkanalyzer.model.statistics;

import android.util.Log;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Math Statistics collected about attributes of Apk file
 * <p>
 * @author Martin Styk
 * @version 28.07.2017.
 */
public class MathStatisticsBuilder {

    private float[] data;

    public MathStatisticsBuilder(float[] data) {
        this.data = data;
    }

    public MathStatistics build() {

        if (data == null || data.length == 0) {
            throw new IllegalArgumentException();
        }

        Arrays.sort(data);

        BigDecimal arithmeticMean = new BigDecimal(getMean());
        BigDecimal median = new BigDecimal(getMedian());
        BigDecimal min = new BigDecimal(data[data.length - 1]);
        BigDecimal max = new BigDecimal(data[0]);
        BigDecimal variance = new BigDecimal(getVariance(arithmeticMean.floatValue()));
        BigDecimal deviation = new BigDecimal(Math.sqrt(variance.floatValue()));

        return new MathStatistics(
                arithmeticMean,
                median,
                min,
                max,
                variance,
                deviation
        );
    }

    private double getMean() {
        double sum = 0.0;
        for (double a : data)
            sum += a;
        return sum / data.length;
    }

    private double getVariance(float mean) {
        double temp = 0;
        for (double a : data)
            temp += (a - mean) * (a - mean);
        return temp / (data.length - 1);
    }


    private float getMedian() {
        if (data.length % 2 == 0) {
            return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0f;
        }
        return data[data.length / 2];
    }

}
