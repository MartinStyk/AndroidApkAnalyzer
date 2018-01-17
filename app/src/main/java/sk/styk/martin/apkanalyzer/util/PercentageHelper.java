package sk.styk.martin.apkanalyzer.util;

import java.math.BigDecimal;

/**
 * @author Martin Styk
 * @version 28.07.2017.
 */
public class PercentageHelper {
    public static BigDecimal getPercentage(double part, double whole) {
        return new BigDecimal(part * 100 / whole);
    }
}
