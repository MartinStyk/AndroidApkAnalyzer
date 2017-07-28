package sk.styk.martin.apkanalyzer.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin Styk on 28.07.2017.
 */

public class ConversionHelper {

    public static double[] toDoubleArray(List<Double> numbers) {
        double[] target = new double[numbers.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = numbers.get(i).doubleValue();
        }
        return target;
    }

    public static double[] toDoubleArray(float[] numbers) {
        double[] target = new double[numbers.length];
        for (int i = 0; i < target.length; i++) {
            target[i] = (double) numbers[i];
        }
        return target;
    }

    public static List<Integer> toIntegerList(double[] arr) {
        List<Integer> toReturn = new ArrayList<Integer>();
        for (int i = 0; i < arr.length; i++) {
            Double d = arr[i];
            toReturn.add(d.intValue());
        }
        return toReturn;
    }

    public static List<Long> toLongList(double[] arr) {
        if (arr == null)
            return null;

        List<Long> toReturn = new ArrayList<Long>();
        for (int i = 0; i < arr.length; i++) {
            Double d = arr[i];
            toReturn.add(d.longValue());
        }
        return toReturn;
    }

    public static List<Long> toLongList(List<Number> arr) {
        if (arr == null)
            return null;

        List<Long> toReturn = new ArrayList<Long>();
        for (int i = 0; i < arr.size(); i++) {
            Long d = arr.get(i).longValue();
            toReturn.add(d);
        }
        return toReturn;
    }

}

