package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.general

import android.support.annotation.LayoutRes
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailGeneralBinding
import sk.styk.martin.apkanalyzer.model.detail.GeneralData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.AppDetailPageContract

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */
class GeneralDetailFragment : AppDetailPageContract.View<GeneralData, FragmentAppDetailGeneralBinding>() {

    @LayoutRes
    override val layout: Int = R.layout.fragment_app_detail_general

    override fun showData(data: GeneralData) {
        binding.data = data
    }
}

