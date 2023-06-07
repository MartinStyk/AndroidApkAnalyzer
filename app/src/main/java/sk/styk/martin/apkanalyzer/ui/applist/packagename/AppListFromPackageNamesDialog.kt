package sk.styk.martin.apkanalyzer.ui.applist.packagename

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.DialogAppListBinding
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailActivity
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragment.Companion.APP_DETAIL_REQUEST
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailRequest
import sk.styk.martin.apkanalyzer.ui.applist.AppListAdapter
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppListFromPackageNamesDialog : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: AppListFromPackageNamesDialogViewModel.Factory

    private lateinit var viewModel: AppListFromPackageNamesDialogViewModel

    private lateinit var binding: DialogAppListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel {
            viewModelFactory.create(
                packageNames = requireNotNull(requireArguments().getStringArrayList(ARG_PACKAGE_NAMES)),
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.appClicked.observe(this, this@AppListFromPackageNamesDialog::startAppDetail)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAppListBinding.inflate(layoutInflater, null, false)

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(R.string.apps)
            .setNegativeButton(R.string.dismiss) { _, _ -> dismiss() }
            .create()
    }

    private fun startAppDetail(appListClickData: AppListAdapter.AppListClickData) {
        val intent = Intent(requireContext(), AppDetailActivity::class.java).apply {
            putExtra(APP_DETAIL_REQUEST, AppDetailRequest.InstalledPackage(appListClickData.lazyAppListData.packageName))
        }
        startActivity(intent)
    }

    companion object {
        private const val ARG_PACKAGE_NAMES = "arg_package_names"
        fun newInstance(packageNames: List<String>) = AppListFromPackageNamesDialog().apply {
            val list = if (packageNames is ArrayList<String>) packageNames else ArrayList<String>(packageNames)
            arguments = Bundle().apply {
                putStringArrayList(ARG_PACKAGE_NAMES, list)
            }
        }
    }
}
