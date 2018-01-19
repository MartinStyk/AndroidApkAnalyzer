package sk.styk.martin.apkanalyzer.activity.detailfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.model.detail.CertificateData
import sk.styk.martin.apkanalyzer.view.DetailItemView

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */
class AppDetailFragment_Certificate : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_app_detail_certificate, container, false)

        val data = arguments.getParcelable<CertificateData>(AppDetailFragment.ARG_CHILD)

        (rootView.findViewById<View>(R.id.item_sign_algorithm) as DetailItemView).valueText = data!!.signAlgorithm

        val startDate = DateUtils.formatDateTime(activity, data.startDate.time, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME)
        (rootView.findViewById<View>(R.id.item_start_date) as DetailItemView).valueText = startDate

        val endDate = DateUtils.formatDateTime(activity, data.endDate.time, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME)
        (rootView.findViewById<View>(R.id.item_end_date) as DetailItemView).valueText = endDate

        (rootView.findViewById<View>(R.id.item_public_key_md5) as DetailItemView).valueText = data.publicKeyMd5
        (rootView.findViewById<View>(R.id.item_cert_md5) as DetailItemView).valueText = data.certificateHash
        (rootView.findViewById<View>(R.id.item_serial_number) as DetailItemView).valueText = data.serialNumber.toString()
        (rootView.findViewById<View>(R.id.item_issuer_name) as DetailItemView).valueText = data.issuerName
        (rootView.findViewById<View>(R.id.item_issuer_organization) as DetailItemView).valueText = data.issuerOrganization.toString()
        (rootView.findViewById<View>(R.id.item_issuer_country) as DetailItemView).valueText = data.issuerCountry
        (rootView.findViewById<View>(R.id.item_subject_name) as DetailItemView).valueText = data.subjectName.toString()
        (rootView.findViewById<View>(R.id.item_subject_organization) as DetailItemView).valueText = data.subjectOrganization
        (rootView.findViewById<View>(R.id.item_subject_country) as DetailItemView).valueText = data.subjectCountry

        return rootView
    }
}
