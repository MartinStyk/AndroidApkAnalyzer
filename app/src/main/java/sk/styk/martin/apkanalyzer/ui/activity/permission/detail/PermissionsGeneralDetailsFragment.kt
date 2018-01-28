package sk.styk.martin.apkanalyzer.ui.activity.permission.detail


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionDetailGeneralBinding
import sk.styk.martin.apkanalyzer.model.detail.PermissionData

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class PermissionsGeneralDetailsFragment : Fragment(), PermissionsGeneralDetailsContract.View {

    private lateinit var presenter: PermissionsGeneralDetailsPresenter
    private lateinit var binding: FragmentPermissionDetailGeneralBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter = PermissionsGeneralDetailsPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_permission_detail_general, container, false)

        presenter.view = this
        presenter.initialize(arguments)

        return binding.root
    }

    override fun showPermissionDetails(permissionData: PermissionData, grantedApps: Int, notGrantedApss: Int) {
        binding.data = permissionData
        binding.granted = grantedApps
        binding.notGranted = notGrantedApss
    }

    companion object {
        const val ARG_NUMBER_GRANTED_APPS = "apps_granted_perm"
        const val ARG_NUMBER_NOT_GRANTED_APPS = "apps_not_granted_perm"
    }

}
