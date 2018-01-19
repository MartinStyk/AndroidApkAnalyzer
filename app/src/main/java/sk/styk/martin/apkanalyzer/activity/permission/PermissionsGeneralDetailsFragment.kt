package sk.styk.martin.apkanalyzer.activity.permission


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_permission_detail_general.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.detail.PermissionData
import sk.styk.martin.apkanalyzer.util.PermissionLevelHelper

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class PermissionsGeneralDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_permission_detail_general, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val permissionData = arguments.getParcelable<PermissionData>(PermissionDetailPagerFragment.ARG_CHILD)
                ?: throw IllegalArgumentException("data null")

        permission_name.valueText = permissionData.name
        permission_group_name.valueText = permissionData.groupName
        permission_protection_level.valueText = PermissionLevelHelper.showLocalized(permissionData.protectionLevel)
        number_apps_granted.valueText = arguments.getInt(ARG_NUMBER_GRANTED_APPS).toString()
        number_apps_not_granted.valueText = arguments.getInt(ARG_NUMBER_NOT_GRANTED_APPS).toString()
    }

    companion object {
        const val ARG_NUMBER_GRANTED_APPS = "apps_granted_perm"
        const val ARG_NUMBER_NOT_GRANTED_APPS = "apps_not_granted_perm"
    }

}
