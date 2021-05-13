package sk.styk.martin.apkanalyzer.ui.appdetail

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailBinding
import sk.styk.martin.apkanalyzer.manager.backpress.BackPressedListener
import sk.styk.martin.apkanalyzer.manager.backpress.BackPressedManager
import sk.styk.martin.apkanalyzer.ui.manifest.AndroidManifestFragment
import sk.styk.martin.apkanalyzer.ui.manifest.ManifestRequest
import sk.styk.martin.apkanalyzer.util.*
import sk.styk.martin.apkanalyzer.util.components.toSnackbar
import sk.styk.martin.apkanalyzer.util.file.AppOperations
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AppDetailFragment : Fragment(), BackPressedListener {

    @Inject
    lateinit var viewModelFactory: AppDetailFragmentViewModel.Factory

    @Inject
    lateinit var backPressedManager: BackPressedManager

    private lateinit var binding: FragmentAppDetailBinding

    private lateinit var viewModel: AppDetailFragmentViewModel

    private lateinit var installPermissionResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var exportPathPickerResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = requireContext().materialContainerTransform()

        viewModel = provideViewModel {
            viewModelFactory.create(requireNotNull(requireArguments().getParcelable(APP_DETAIL_REQUEST)))
        }
        lifecycle.addObserver(viewModel)
        installPermissionResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), viewModel.installPermissionResult)
        exportPathPickerResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), viewModel.exportFilePickerResult)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAppDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPressedManager.registerBackPressedListener(this)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.pager.adapter = AppDetailPagerAdapter(
                requireArguments(),
                requireContext().applicationContext,
                childFragmentManager)
        binding.tab.setupWithViewPager(binding.pager)

        with(viewModel) {
            showSnack.observe(viewLifecycleOwner, { it.toSnackbar(binding.root).show() })
            close.observe(viewLifecycleOwner, { requireActivity().onBackPressed() })
            openSystemInfo.observe(viewLifecycleOwner, { AppOperations.openAppSystemPage(requireContext(), it) })
            openGooglePlay.observe(viewLifecycleOwner, { AppOperations.openGooglePlay(requireContext(), it) })
            installApp.observe(viewLifecycleOwner, { AppOperations.installPackage(requireContext(), it) })
            showManifest.observe(viewLifecycleOwner, this@AppDetailFragment::openManifestFragment)
            openImage.observe(viewLifecycleOwner, { openImage(it) })
            openSettingsInstallPermission.observe(viewLifecycleOwner, { startInstallSettings() })
            openExportFilePicker.observe(viewLifecycleOwner, { openDirectoryPicker(it) })
        }
    }

    override fun onDestroyView() {
        backPressedManager.unregisterBackPressedListener(this)
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

    private fun openImage(path: Uri) {
        try {
            startActivity(Intent().apply {
                action = Intent.ACTION_VIEW
                setDataAndType(path, "image/png")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (e: ActivityNotFoundException) {
            Timber.tag(TAG_APP_ACTIONS).w(e, "Can not open image in external app")
            Toast.makeText(requireContext(), R.string.activity_not_found_image, Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("InlinedApi")
    private fun startInstallSettings() {
        try {
            installPermissionResultLauncher.launch(
                    Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                        data = Uri.parse(String.format("package:%s", requireContext().packageName))
                    }
            )
        } catch (exception: ActivityNotFoundException) {
            Timber.tag(TAG_APP_ACTIONS).w(exception, "Can not open install settings")
            Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.activity_not_found_browsing, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun openManifestFragment(manifestRequest: ManifestRequest) {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        parentFragmentManager.beginTransaction()
                .replace(R.id.container, AndroidManifestFragment.create(manifestRequest), FragmentTag.Manifest.tag)
                .addToBackStack(FragmentTag.Manifest.tag)
                .setReorderingAllowed(true)
                .commit()
    }

    private fun openDirectoryPicker(outputFilePickerRequest: OutputFilePickerRequest) {
        try {
            exportPathPickerResultLauncher.launch(
                    Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = outputFilePickerRequest.fileType
                        putExtra(Intent.EXTRA_TITLE, outputFilePickerRequest.fileName)
                    }
            )
        } catch (e: ActivityNotFoundException) {
            Timber.tag(TAG_APP_ACTIONS).w(e, "Can not open file picker")
            Toast.makeText(requireContext(), R.string.activity_not_found_browsing, Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed(): Boolean {
        return if (binding.btnActions.isSpeedDialMenuOpen) {
            binding.btnActions.closeSpeedDialMenu()
            true
        } else {
            false
        }
    }

    companion object {
        const val APP_DETAIL_REQUEST = "appDetailRequest"
        fun newInstance(appDetailRequest: AppDetailRequest) = AppDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(APP_DETAIL_REQUEST, appDetailRequest)
            }
        }
    }
}

