package sk.styk.martin.apkanalyzer.ui.base

/**
 * Interface representing a ListPresenter in a model view pagerPresenter (MVP) pattern.
 *
 * Offers method for use in RecyclerView adapter
 */
interface ListPresenter<in ITEM> {

    /**
     * Number of items in collection
     */
    fun itemCount(): Int

    /**
     * Binds item to view holder
     */
    fun onBindViewOnPosition(position: Int, holder: ITEM)
}