package sk.styk.martin.apkanalyzer.util;

import java.util.List;

/**
 * Created by Martin Styk on 13.11.2017.
 */

public class HashCodeHelper {

    /**
     * Returns hashcode for list, which doesn't take order into account
     * Object.hashCode() must be stable, e.g. always return same value
     *  -  always override hashcode in objects passed to this method
     *  -  we can rely on String.hashCode() to be consistent
     */
    public static int hashList(List<?> list) {
        int hash = 31;
        for (Object o : list) {
            hash += 29 * o.hashCode();
        }
        return hash;
    }
}
