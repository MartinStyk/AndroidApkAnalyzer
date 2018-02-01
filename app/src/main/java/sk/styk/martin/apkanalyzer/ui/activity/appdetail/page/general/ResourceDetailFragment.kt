package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.general

import android.support.annotation.LayoutRes
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailResourceBinding
import sk.styk.martin.apkanalyzer.model.detail.ResourceData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.AppDetailPageContract

/**
 * @author Martin Styk
 * @version 03.07.2017.
 */
class ResourceDetailFragment : AppDetailPageContract.View<ResourceData, FragmentAppDetailResourceBinding>() {

    @LayoutRes
    override val layout: Int = R.layout.fragment_app_detail_resource

    override fun showData(data: ResourceData) {
        binding.data = data
    }
}
