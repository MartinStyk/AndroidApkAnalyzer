package sk.styk.martin.apkanalyzer.ui.manifest

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialSharedAxis
import com.pddstudio.highlightjs.HighlightJsView
import com.pddstudio.highlightjs.models.Language
import com.pddstudio.highlightjs.models.Theme
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentManifestBinding
import sk.styk.martin.apkanalyzer.util.OutputFilePickerRequest
import sk.styk.martin.apkanalyzer.util.TAG_APP_ACTIONS
import sk.styk.martin.apkanalyzer.util.TAG_APP_ANALYSIS
import sk.styk.martin.apkanalyzer.util.components.toSnackbar
import sk.styk.martin.apkanalyzer.util.isNightMode
import sk.styk.martin.apkanalyzer.util.provideViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AndroidManifestFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: AndroidManifestFragmentViewModel.Factory

    private lateinit var binding: FragmentManifestBinding

    private lateinit var viewModel: AndroidManifestFragmentViewModel

    private lateinit var exportPathPickerResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        viewModel = provideViewModel {
            viewModelFactory.create(requireNotNull(requireArguments().getParcelable(MANIFEST_REQUEST)))
        }
        exportPathPickerResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), viewModel.exportFilePickerResult)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentManifestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        with(viewModel) {
            close.observe(viewLifecycleOwner) { requireActivity().onBackPressed() }
            showSnack.observe(viewLifecycleOwner) { it.toSnackbar(binding.root).show() }
            showManifestFile.observe(viewLifecycleOwner, this@AndroidManifestFragment::showManifestFile)
            openExportFilePicker.observe(viewLifecycleOwner) { openExportFilePicker(it) }
            manifest.observe(viewLifecycleOwner) { showManifest(it) }
        }
    }

    private fun showManifestFile(fileUri: Uri) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(fileUri, "text/xml")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Timber.tag(TAG_APP_ACTIONS).w(e, "Can not show manifest with intent $intent")
            Toast.makeText(requireContext(), R.string.activity_not_found_doc, Toast.LENGTH_LONG).show()
        }
    }

    private fun openExportFilePicker(outputFilePickerRequest: OutputFilePickerRequest) {
        try {
            exportPathPickerResultLauncher.launch(
                Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = outputFilePickerRequest.fileType
                    putExtra(Intent.EXTRA_TITLE, outputFilePickerRequest.fileName)
                },
            )
        } catch (e: ActivityNotFoundException) {
            Timber.tag(TAG_APP_ACTIONS).w(e, "Can not open file picker")
            Toast.makeText(requireContext(), R.string.activity_not_found_browsing, Toast.LENGTH_LONG).show()
        }
    }

    private fun showManifest(manifest: String) {
        try {
            val codeView = HighlightJsView(requireContext()).apply {
                highlightLanguage = Language.XML
                theme = if (binding.root.isNightMode()) Theme.ATOM_ONE_DARK else Theme.ATOM_ONE_LIGHT
                setSource(manifest)
            }
            with(binding.codeContainer) {
                removeAllViews()
                addView(codeView)
            }
        } catch (e: Exception) {
            Timber.tag(TAG_APP_ANALYSIS).e(e, "Cannot create code view")
            Toast.makeText(requireContext(), R.string.no_data, Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        }
    }

    companion object {
        private const val MANIFEST_REQUEST = "manifest_request"

        fun create(manifestRequest: ManifestRequest) = AndroidManifestFragment().apply {
            arguments = Bundle().apply {
                putParcelable(MANIFEST_REQUEST, manifestRequest)
            }
        }
    }
}
