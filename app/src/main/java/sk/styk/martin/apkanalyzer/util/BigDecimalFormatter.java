package sk.styk.martin.apkanalyzer.util;

import java.text.DecimalFormat;

/**
 * Created by Martin Styk on 31.07.2017.
 */
public class BigDecimalFormatter {
    private static DecimalFormat commonFormat;

    public static DecimalFormat getCommonFormat() {
        if (commonFormat == null) {
            commonFormat = new DecimalFormat();
            commonFormat.setMaximumFractionDigits(2);
            commonFormat.setMinimumFractionDigits(2);
        }
        return commonFormat;
    }

    public static DecimalFormat getFormat(int minFractions, int maxFractions) {
        DecimalFormat customFormat = new DecimalFormat();
        customFormat.setMaximumFractionDigits(maxFractions);
        customFormat.setMinimumFractionDigits(minFractions);

        return customFormat;
    }
}