package sk.styk.martin.apkanalyzer.ui.activity.applist.searchable

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_app_list.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentAppListBinding
import sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel.ViewModelFactory
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.base.AppDetailActivity
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent
import sk.styk.martin.apkanalyzer.util.components.toSnackbar
import sk.styk.martin.apkanalyzer.util.file.ApkFilePicker
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

class AppListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentAppListBinding

    private lateinit var viewModel: AppListViewModel

    private lateinit var filePickerResultLuncher: ActivityResultLauncher<Intent>

    private var snackbar: Snackbar? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel(viewModelFactory)
        filePickerResultLuncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), viewModel.filePickerResult)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAppListBinding.inflate(LayoutInflater.from(context), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        with(viewModel) {
            showSnack.observe(viewLifecycleOwner, { it.toSnackbar(app_list_container).show() })
            openFilePicker.observe(viewLifecycleOwner, { startFilePicker() })
            appClicked.observe(viewLifecycleOwner, { startActivity(AppDetailActivity.createIntent(packageName = it.packageName, context = requireContext())) })
            openDetailFromFile.observe(viewLifecycleOwner, { startActivity(AppDetailActivity.createIntent(packageUri = it, context = requireContext())) })
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
            snackbar = component.toSnackbar(app_list_container)
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

    companion object {
        fun newInstance() = AppListFragment()
    }
}
