package sk.styk.martin.apkanalyzer.ui.appdetail.recycler

interface ExpandableItemViewModel {
    val expanded: Boolean
    fun toggleExpanded(newlyExpanded: Boolean)
}
