package sk.styk.martin.apkanalyzer.util

/**
 * @author Martin Styk
 * @version 13.11.2017.
 */
object HashCodeHelper {

    /**
     * Returns hashcode for list, which doesn't take order into account
     * Object.hashCode() must be stable, e.g. always return same value
     * -  always override hashcode in objects passed to this method
     * -  we can rely on String.hashCode() to be consistent
     */
    fun hashList(vararg lists: List<*>): Int {
        var hash = 31
        for (list in lists)
            for (o in list) {
                if (o != null)
                    hash += 29 * o.hashCode()
            }
        return hash
    }
}
