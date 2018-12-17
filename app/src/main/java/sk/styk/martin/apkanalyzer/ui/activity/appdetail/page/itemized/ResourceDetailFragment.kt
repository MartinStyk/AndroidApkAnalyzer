package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.itemized

import android.support.annotation.LayoutRes
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailResourceBinding
import sk.styk.martin.apkanalyzer.model.detail.ResourceData

/**
 * @author Martin Styk
 * @version 03.07.2017.
 */
class ResourceDetailFragment : ItemizedAppDetailPageContract.View<ResourceData, FragmentAppDetailResourceBinding>() {

    override val itemizedDataType =  ItemizedDataType.RESOURCE_DATA

    @LayoutRes
    override val layout: Int = R.layout.fragment_app_detail_resource

    override fun showData(data: ResourceData) {
        binding.data = data
    }
}
