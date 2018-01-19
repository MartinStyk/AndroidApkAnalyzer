package sk.styk.martin.apkanalyzer.activity.permission


import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionDetailGeneralBinding
import sk.styk.martin.apkanalyzer.model.detail.PermissionData

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class PermissionsGeneralDetailsFragment : Fragment() {

    private var binding: FragmentPermissionDetailGeneralBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPermissionDetailGeneralBinding.inflate(layoutInflater)

        val permissionData = arguments.getParcelable<PermissionData>(PermissionDetailPagerFragment.ARG_CHILD)
        binding!!.permission = permissionData
        binding!!.context = context
        binding!!.grantedApps = arguments.getInt(ARG_NUMBER_GRANTED_APPS)
        binding!!.notGrantedApps = arguments.getInt(ARG_NUMBER_NOT_GRANTED_APPS)

        return binding!!.root
    }

    companion object {

        private val TAG = PermissionsGeneralDetailsFragment::class.java.simpleName

        val ARG_NUMBER_GRANTED_APPS = "apps_granted_perm"
        val ARG_NUMBER_NOT_GRANTED_APPS = "apps_not_granted_perm"
    }

}
