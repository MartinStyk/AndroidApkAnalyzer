package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.itemized

import androidx.annotation.LayoutRes
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailCertificateBinding
import sk.styk.martin.apkanalyzer.model.detail.CertificateData

class CertificateDetailFragment : ItemizedAppDetailPageContract.View<CertificateData, FragmentAppDetailCertificateBinding>() {

    override val itemizedDataType = ItemizedDataType.CERTIFICATE_DATA

    @LayoutRes
    override val layout: Int = R.layout.fragment_app_detail_certificate

    override fun showData(data: CertificateData) {
        binding.data = data
    }

    override fun showNoData() {
        binding.data = null
    }
}
