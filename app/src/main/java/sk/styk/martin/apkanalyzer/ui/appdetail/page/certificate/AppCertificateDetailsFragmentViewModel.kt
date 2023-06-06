package sk.styk.martin.apkanalyzer.ui.appdetail.page.certificate

import android.text.format.DateUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel
import sk.styk.martin.apkanalyzer.util.Formatter
import sk.styk.martin.apkanalyzer.util.TextInfo

class AppCertificateDetailsFragmentViewModel @AssistedInject constructor(
    @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
    val detailInfoAdapter: DetailInfoAdapter,
    clipBoardManager: ClipBoardManager,
    private val formatter: Formatter,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, detailInfoAdapter, clipBoardManager) {

    override fun onDataReceived(appDetailData: AppDetailData): Boolean {
        val data = appDetailData.certificateData
        detailInfoAdapter.info = listOfNotNull(
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.sign_algorithm),
                TextInfo.from(data.signAlgorithm),
                TextInfo.from(R.string.sign_algorithm_description),
            ),
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.start_date),
                TextInfo.from(formatter.formatDateTime(data.startDate.time, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME)),
                TextInfo.from(R.string.start_date_description),
            ),
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.end_date),
                TextInfo.from(formatter.formatDateTime(data.endDate.time, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME)),
                TextInfo.from(R.string.end_date_description),
            ),
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.cert_md5),
                TextInfo.from(data.certificateHash),
                TextInfo.from(R.string.cert_md5_description),
            ),
            data.issuerName?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.issuer_name),
                    TextInfo.from(it),
                    TextInfo.from(R.string.issuer_name_description),
                )
            },
            data.issuerOrganization?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.issuer_organization),
                    TextInfo.from(it),
                    TextInfo.from(R.string.issuer_organization_description),
                )
            },
            data.issuerCountry?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.issuer_country),
                    TextInfo.from(it),
                    TextInfo.from(R.string.issuer_country_description),
                )
            },
            data.subjectName?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.subject_name),
                    TextInfo.from(it),
                    TextInfo.from(R.string.subject_name_description),
                )
            },
            data.subjectOrganization?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.subject_organization),
                    TextInfo.from(it),
                    TextInfo.from(R.string.subject_organization_description),
                )
            },
            data.subjectCountry?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.subject_country),
                    TextInfo.from(it),
                    TextInfo.from(R.string.subject_country_description),
                )
            },
        )
        return true
    }

    @AssistedFactory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppCertificateDetailsFragmentViewModel
    }
}
