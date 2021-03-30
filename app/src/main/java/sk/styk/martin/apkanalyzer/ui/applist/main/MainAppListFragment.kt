package sk.styk.martin.apkanalyzer.ui.applist.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentMainAppListBinding
import sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel.ViewModelFactory
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailActivity
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailRequest
import sk.styk.martin.apkanalyzer.ui.applist.BaseAppListFragment
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent
import sk.styk.martin.apkanalyzer.util.components.toSnackbar
import sk.styk.martin.apkanalyzer.util.file.ApkFilePicker
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

class MainAppListFragment : BaseAppListFragment<MainAppListViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentMainAppListBinding

    private lateinit var filePickerResultLuncher: ActivityResultLauncher<Intent>

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel(viewModelFactory)
        filePickerResultLuncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), viewModel.filePickerResult)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainAppListBinding.inflate(LayoutInflater.from(context), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        with(viewModel) {
            showSnack.observe(viewLifecycleOwner, { it.toSnackbar(binding.appListContainer).show() })
            openFilePicker.observe(viewLifecycleOwner, { startFilePicker() })
            openDetailFromFile.observe(viewLifecycleOwner, { startAppDetail(it) })
            indeterminateSnackbar.observe(viewLifecycleOwner, { handleIndefiniteSnackbar(it) })
            filteredSource.observe(viewLifecycleOwner, { handleFilteredSources(it) })
        }
    }

    private fun handleFilteredSources(filtered: AppSource?) {
        val itemId = when (filtered) {
            AppSource.GOOGLE_PLAY -> R.id.menu_show_google_play_apps
            AppSource.AMAZON_STORE -> R.id.menu_show_amazon_store_apps
            AppSource.SYSTEM_PREINSTALED -> R.id.menu_show_system_pre_installed_apps
            AppSource.UNKNOWN -> R.id.menu_show_unknown_source_apps
            else -> R.id.menu_show_all_apps
        }
        binding.toolbar.menu.findItem(itemId)?.isChecked = true
    }

    private fun handleIndefiniteSnackbar(component: SnackBarComponent?) {
        if (component != null && (snackbar == null || snackbar?.isShown == false)) {
            snackbar = component.toSnackbar(binding.appListContainer)
            snackbar?.show()
        } else if (component == null) {
            snackbar?.dismiss()
            snackbar = null
        }
    }

    private fun startFilePicker() {
        try {
            filePickerResultLuncher.launch(ApkFilePicker.filePickerIntent)
        } catch (exception: ActivityNotFoundException) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.activity_not_found_browsing, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun startAppDetail(uri: Uri) {
        val intent = Intent(requireContext(), AppDetailActivity::class.java).apply {
            putExtra(AppDetailActivity.APP_DETAIL_REQUEST, AppDetailRequest.ExternalPackage(uri))
        }
        startActivity(intent)
    }

    companion object {
        fun newInstance() = MainAppListFragment()
    }
}
