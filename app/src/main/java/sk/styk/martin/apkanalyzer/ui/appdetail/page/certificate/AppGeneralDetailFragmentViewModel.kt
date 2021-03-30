package sk.styk.martin.apkanalyzer.ui.appdetail.page.certificate

import android.text.format.DateUtils
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.android.material.snackbar.Snackbar
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.util.Formatter
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.DialogComponent
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent

class AppCertificateDetailsFragmentViewModel @AssistedInject constructor(
        @Assisted private val appDetailFragmentViewModel: AppDetailFragmentViewModel,
        val detailInfoAdapter: DetailInfoAdapter,
        private val clipBoardManager: ClipBoardManager,
        private val formatter: Formatter,
) : ViewModel(), DefaultLifecycleObserver {

    val openDescription = detailInfoAdapter.openDescription
            .map {
                DialogComponent(it.name, it.description, TextInfo.from(R.string.close))
            }

    val showSnackbar = detailInfoAdapter.copyToClipboard
            .map {
                clipBoardManager.copyToClipBoard(it.value, it.name)
                SnackBarComponent(TextInfo.from(R.string.copied_to_clipboard), Snackbar.LENGTH_SHORT)
            }

    private val appDetailsObserver = Observer<AppDetailData> {
        val data = it.certificateData
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
                            TextInfo.from(data.issuerName),
                            TextInfo.from(R.string.issuer_name_description),
                    )
                },
                data.issuerOrganization?.let {
                    DetailInfoAdapter.DetailInfo(
                            TextInfo.from(R.string.issuer_organization),
                            TextInfo.from(data.issuerOrganization),
                            TextInfo.from(R.string.issuer_organization_description),
                    )
                },
                data.issuerCountry?.let {
                    DetailInfoAdapter.DetailInfo(
                            TextInfo.from(R.string.issuer_country),
                            TextInfo.from(data.issuerCountry),
                            TextInfo.from(R.string.issuer_country_description),
                    )
                },
                data.subjectName?.let {
                    DetailInfoAdapter.DetailInfo(
                            TextInfo.from(R.string.subject_name),
                            TextInfo.from(data.subjectName),
                            TextInfo.from(R.string.subject_name_description),
                    )
                },
                data.subjectOrganization?.let {
                    DetailInfoAdapter.DetailInfo(
                            TextInfo.from(R.string.subject_organization),
                            TextInfo.from(data.subjectOrganization),
                            TextInfo.from(R.string.subject_organization_description),
                    )
                },
                data.subjectCountry?.let {
                    DetailInfoAdapter.DetailInfo(
                            TextInfo.from(R.string.subject_country),
                            TextInfo.from(data.subjectCountry),
                            TextInfo.from(R.string.subject_country_description),
                    )
                },
        )
    }

    init {
        appDetailFragmentViewModel.appDetails.observeForever(appDetailsObserver)
    }

    override fun onCleared() {
        super.onCleared()
        appDetailFragmentViewModel.appDetails.removeObserver(appDetailsObserver)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppCertificateDetailsFragmentViewModel
    }
}