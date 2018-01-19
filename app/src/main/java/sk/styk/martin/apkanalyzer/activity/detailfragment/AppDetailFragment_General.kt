package sk.styk.martin.apkanalyzer.activity.detailfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateUtils
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.model.detail.GeneralData
import sk.styk.martin.apkanalyzer.util.InstallLocationHelper
import sk.styk.martin.apkanalyzer.view.DetailItemView

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */

class AppDetailFragment_General : Fragment() {

    private var data: GeneralData? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_app_detail_general, container, false)

        data = arguments.getParcelable(AppDetailFragment.ARG_CHILD)

        (rootView.findViewById<View>(R.id.item_application_name) as DetailItemView).valueText = data!!.applicationName
        (rootView.findViewById<View>(R.id.item_package_name) as DetailItemView).valueText = data!!.packageName
        (rootView.findViewById<View>(R.id.item_process_name) as DetailItemView).valueText = data!!.processName
        (rootView.findViewById<View>(R.id.item_version_name) as DetailItemView).valueText = data!!.versionName
        (rootView.findViewById<View>(R.id.item_version_code) as DetailItemView).valueText = data!!.versionCode.toString()
        (rootView.findViewById<View>(R.id.item_system_application) as DetailItemView).valueText = if (data!!.isSystemApp) getString(R.string.yes) else getString(R.string.no)
        (rootView.findViewById<View>(R.id.item_uid) as DetailItemView).valueText = data!!.uid.toString()
        (rootView.findViewById<View>(R.id.item_application_description) as DetailItemView).valueText = data!!.description
        (rootView.findViewById<View>(R.id.item_application_app_source) as DetailItemView).valueText = if (data!!.source == null) null else data!!.source.toString()
        (rootView.findViewById<View>(R.id.item_target_sdk) as DetailItemView).valueText = data!!.targetSdkVersion.toString()
        (rootView.findViewById<View>(R.id.item_target_android_version) as DetailItemView).valueText = data!!.targetSdkLabel
        (rootView.findViewById<View>(R.id.item_min_sdk) as DetailItemView).valueText = data!!.minSdkVersion.toString()
        (rootView.findViewById<View>(R.id.item_min_android_version) as DetailItemView).valueText = data!!.minSdkLabel
        (rootView.findViewById<View>(R.id.item_apk_directory) as DetailItemView).valueText = data!!.apkDirectory
        (rootView.findViewById<View>(R.id.item_data_directory) as DetailItemView).valueText = data!!.dataDirectory
        (rootView.findViewById<View>(R.id.item_install_location) as DetailItemView).valueText = InstallLocationHelper.showLocalizedLocation(data!!.installLocation, context)
        (rootView.findViewById<View>(R.id.item_apk_size) as DetailItemView).valueText = Formatter.formatShortFileSize(activity, data!!.apkSize)

        val installTime = DateUtils.formatDateTime(activity, data!!.firstInstallTime, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME)
        (rootView.findViewById<View>(R.id.item_first_install_time) as DetailItemView).valueText = installTime

        val updateTime = DateUtils.formatDateTime(activity, data!!.lastUpdateTime, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME)
        (rootView.findViewById<View>(R.id.item_last_update_time) as DetailItemView).valueText = updateTime

        return rootView
    }
}
