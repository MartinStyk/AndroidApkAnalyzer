package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.itemized

import android.support.annotation.LayoutRes
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailCertificateBinding
import sk.styk.martin.apkanalyzer.model.detail.CertificateData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.AppDetailPageContract

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */
class CertificateDetailFragment : AppDetailPageContract.View<CertificateData, FragmentAppDetailCertificateBinding>() {

    @LayoutRes
    override val layout: Int = R.layout.fragment_app_detail_certificate

    override fun showData(data: CertificateData) {
        binding.data = data
    }
}
