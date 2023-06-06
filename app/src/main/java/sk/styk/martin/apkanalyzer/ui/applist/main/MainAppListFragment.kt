package sk.styk.martin.apkanalyzer.ui.applist.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.doOnPreDraw
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppSource
import sk.styk.martin.apkanalyzer.databinding.FragmentMainAppListBinding
import sk.styk.martin.apkanalyzer.manager.backpress.BackPressedListener
import sk.styk.martin.apkanalyzer.manager.backpress.BackPressedManager
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragment
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailRequest
import sk.styk.martin.apkanalyzer.ui.applist.BaseAppListFragment
import sk.styk.martin.apkanalyzer.util.FragmentTag
import sk.styk.martin.apkanalyzer.util.TAG_APP_ACTIONS
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent
import sk.styk.martin.apkanalyzer.util.components.toSnackbar
import sk.styk.martin.apkanalyzer.util.provideViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainAppListFragment : BaseAppListFragment<MainAppListViewModel>(), BackPressedListener {

    @Inject
    lateinit var backPressedManager: BackPressedManager

    private lateinit var binding: FragmentMainAppListBinding

    private lateinit var filePickerResultLuncher: ActivityResultLauncher<Intent>

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
        filePickerResultLuncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), viewModel.filePickerResult)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainAppListBinding.inflate(LayoutInflater.from(context), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding.recyclerViewAppList.doOnPreDraw { startPostponedEnterTransition() }

        backPressedManager.registerBackPressedListener(this)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        binding.viewModel = viewModel

        with(viewModel) {
            showSnack.observe(viewLifecycleOwner) { it.toSnackbar(binding.appListContainer).show() }
            openFilePicker.observe(viewLifecycleOwner) { startFilePicker() }
            openDetailFromFile.observe(viewLifecycleOwner) { startAppDetail(it) }
            indeterminateSnackbar.observe(viewLifecycleOwner) { handleIndefiniteSnackbar(it) }
            filteredSource.observe(viewLifecycleOwner) { handleFilteredSources(it) }
            setQueryText.observe(viewLifecycleOwner) { handleQueryFilter(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressedManager.unregisterBackPressedListener(this)
        viewLifecycleOwner.lifecycle.removeObserver(viewModel)
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

    private fun handleQueryFilter(filterQuery: String) {
        val searchView = binding.toolbar.menu.findItem(R.id.action_search)?.actionView as? SearchView
            ?: return
        searchView.setQuery(filterQuery, false)
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
            filePickerResultLuncher.launch(
                Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "application/vnd.android.package-archive"
                },
            )
        } catch (exception: ActivityNotFoundException) {
            Timber.tag(TAG_APP_ACTIONS).w(exception, "Can not open file picker")
            Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.activity_not_found_browsing, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun startAppDetail(uri: Uri) {
        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                AppDetailFragment.newInstance(AppDetailRequest.ExternalPackage(uri)),
                FragmentTag.AppDetailParent.tag,
            )
            .addToBackStack(FragmentTag.AppDetailParent.tag)
            .commit()
    }

    override fun onBackPressed(): Boolean {
        val searchView = binding.toolbar.menu.findItem(R.id.action_search)?.actionView as? SearchView
            ?: return false
        if (!searchView.isIconified) {
            searchView.onActionViewCollapsed()
            return true
        }
        return false
    }

    companion object {
        fun newInstance() = MainAppListFragment()
    }
}
