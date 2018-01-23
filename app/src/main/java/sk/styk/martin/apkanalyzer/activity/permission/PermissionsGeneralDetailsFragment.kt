package sk.styk.martin.apkanalyzer.activity.permission


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionDetailGeneralBinding

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
        val binding: FragmentPermissionDetailGeneralBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_permission_detail_general, container, false)

        binding.data = arguments.getParcelable(PermissionDetailPagerFragment.ARG_CHILD) ?: throw IllegalArgumentException("data null")
        binding.granted = arguments.getInt(ARG_NUMBER_GRANTED_APPS)
        binding.notGranted = arguments.getInt(ARG_NUMBER_NOT_GRANTED_APPS)

        return binding.root
    }

    companion object {
        const val ARG_NUMBER_GRANTED_APPS = "apps_granted_perm"
        const val ARG_NUMBER_NOT_GRANTED_APPS = "apps_not_granted_perm"
    }

}
