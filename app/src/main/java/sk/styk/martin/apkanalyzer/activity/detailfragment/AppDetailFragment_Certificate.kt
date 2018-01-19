package sk.styk.martin.apkanalyzer.activity.detailfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_app_detail_certificate.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.model.detail.CertificateData

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */
class AppDetailFragment_Certificate : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_detail_certificate, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments.getParcelable<CertificateData>(AppDetailFragment.ARG_CHILD)
                ?: throw IllegalArgumentException("data is null")

        item_sign_algorithm.valueText = data.signAlgorithm
        item_start_date.valueText = DateUtils.formatDateTime(context, data.startDate.time, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME)
        item_end_date.valueText = DateUtils.formatDateTime(activity, data.endDate.time, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME)
        item_public_key_md5.valueText = data.publicKeyMd5
        item_cert_md5.valueText = data.certificateHash
        item_serial_number.valueText = data.serialNumber.toString()
        item_issuer_name.valueText = data.issuerName
        item_issuer_organization.valueText = data.issuerOrganization
        item_issuer_country.valueText = data.issuerCountry
        item_subject_name.valueText = data.subjectName
        item_subject_organization.valueText = data.subjectOrganization
        item_subject_country.valueText = data.subjectCountry
    }
}
