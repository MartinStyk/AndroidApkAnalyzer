package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.details


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionDetailGeneralBinding
import sk.styk.martin.apkanalyzer.model.detail.PermissionData

class PermissionsGeneralDetailsFragment : Fragment(), PermissionsGeneralDetailsContract.View {

    private lateinit var presenter: PermissionsGeneralDetailsContract.Presenter
    private lateinit var binding: FragmentPermissionDetailGeneralBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter = PermissionsGeneralDetailsPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_permission_detail_general, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.view = this
        presenter.initialize(arguments!!)
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
