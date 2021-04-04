package sk.styk.martin.apkanalyzer.ui.appdetail

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
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
import dagger.android.support.AndroidSupportInjection
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailBinding
import sk.styk.martin.apkanalyzer.manager.backpress.BackPressedListener
import sk.styk.martin.apkanalyzer.manager.backpress.BackPressedManager
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest.ManifestActivity
import sk.styk.martin.apkanalyzer.util.components.toSnackbar
import sk.styk.martin.apkanalyzer.util.file.AppOperations
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

class AppDetailFragment : Fragment(), BackPressedListener {

    @Inject
    lateinit var viewModelFactory: AppDetailFragmentViewModel.Factory

    @Inject
    lateinit var backPressedManager: BackPressedManager

    private lateinit var binding: FragmentAppDetailBinding

    private lateinit var viewModel: AppDetailFragmentViewModel

    private lateinit var installPermissionResultLauncher: ActivityResultLauncher<Intent>

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel {
            viewModelFactory.create(requireNotNull(requireArguments().getParcelable(AppDetailActivity.APP_DETAIL_REQUEST)))
        }
        lifecycle.addObserver(viewModel)
        installPermissionResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), viewModel.installPermissionResult)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAppDetailBinding.inflate(inflater, container, false)
        binding.pager.adapter = AppDetailPagerAdapter(
                requireArguments(),
                requireContext().applicationContext,
                childFragmentManager)
        binding.tabs.setupWithViewPager(binding.pager)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPressedManager.registerBackPressedListener(this)
        binding.viewModel = viewModel

        with(viewModel) {
            showSnack.observe(viewLifecycleOwner, { it.toSnackbar(binding.root).show() })
            close.observe(viewLifecycleOwner, { requireActivity().onBackPressed() })
            openSystemInfo.observe(viewLifecycleOwner, { AppOperations.openAppSystemPage(requireContext(), it) })
            openGooglePlay.observe(viewLifecycleOwner, { AppOperations.openGooglePlay(requireContext(), it) })
            installApp.observe(viewLifecycleOwner, { AppOperations.installPackage(requireContext(), it) })
            showManifest.observe(viewLifecycleOwner, { startActivity(ManifestActivity.createIntent(requireContext(), it)) })
            openImage.observe(viewLifecycleOwner, { openImage(it) })
            openSettingsInstallPermission.observe(viewLifecycleOwner, { startInstallSettings()})
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

    private fun openImage(path: String) {
        try {
            startActivity( Intent().apply {
                action = Intent.ACTION_VIEW
                setDataAndType(Uri.parse(path), "image/png")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (e: ActivityNotFoundException) {
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
            Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.activity_not_found_browsing, Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {
        val TAG = AppDetailFragment::class.java.simpleName
    }

    override fun onBackPressed(): Boolean {
        return if (binding.btnActions.isSpeedDialMenuOpen) {
            binding.btnActions.closeSpeedDialMenu()
            true
        } else {
            false
        }
    }
}

