package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.string

import sk.styk.martin.apkanalyzer.util.AppDetailDataExchange

class StringListDetailPagePresenter(private val dataType: StringDataType) : StringListDetailPageContract.Presenter {

    override lateinit var view: StringListDetailPageContract.View
    protected lateinit var values: List<String>

    override fun initialize(packageName: String) {
        val data = AppDetailDataExchange.get(packageName)
        values = when (dataType) {
            StringDataType.USED_PERMISSIONS -> data?.permissionData?.usesPermissionsNames ?: emptyList()
            StringDataType.DEFINED_PERMISSIONS -> data?.permissionData?.definesPermissionsNames ?: emptyList()
        }
    }

    override fun getData() {
        view.showData()
    }

    override fun itemCount(): Int = values.size

    override fun onBindViewOnPosition(position: Int, holder: StringListDetailPageContract.ItemView) {
        holder.bind(values[position])
    }

    enum class StringDataType {
        USED_PERMISSIONS,
        DEFINED_PERMISSIONS,
    }

}