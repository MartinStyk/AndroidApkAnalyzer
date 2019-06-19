package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.itemized

import androidx.annotation.LayoutRes
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailGeneralBinding
import sk.styk.martin.apkanalyzer.model.detail.GeneralData

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */
class GeneralDetailFragment : ItemizedAppDetailPageContract.View<GeneralData, FragmentAppDetailGeneralBinding>() {

    override val itemizedDataType =  ItemizedDataType.GENERAL_DATA

    @LayoutRes
    override val layout: Int = R.layout.fragment_app_detail_general

    override fun showData(data: GeneralData) {
        binding.data = data
    }

    override fun showNoData() {
        binding.data = null
    }
}

