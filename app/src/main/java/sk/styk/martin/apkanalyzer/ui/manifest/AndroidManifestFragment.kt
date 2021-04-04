package sk.styk.martin.apkanalyzer.ui.manifest

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.pddstudio.highlightjs.models.Language
import com.pddstudio.highlightjs.models.Theme
import dagger.android.support.AndroidSupportInjection
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentManifestBinding
import sk.styk.martin.apkanalyzer.util.components.toSnackbar
import sk.styk.martin.apkanalyzer.util.file.GenericFileProvider
import sk.styk.martin.apkanalyzer.util.provideViewModel
import java.io.File
import javax.inject.Inject

class AndroidManifestFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: AndroidManifestFragmentViewModel.Factory

    private lateinit var binding: FragmentManifestBinding

    private lateinit var viewModel: AndroidManifestFragmentViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel {
            viewModelFactory.create(requireNotNull(requireArguments().getParcelable(MANIFEST_REQUEST)))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentManifestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.codeView.highlightLanguage = Language.XML
        binding.codeView.theme = Theme.ATOM_ONE_LIGHT
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        with(viewModel) {
            close.observe(viewLifecycleOwner, { requireActivity().onBackPressed() })
            showSnack.observe(viewLifecycleOwner, { it.toSnackbar(binding.root).show() })
            showManifestFile.observe(viewLifecycleOwner, this@AndroidManifestFragment::showManifestFile)
        }
    }

    private fun showManifestFile(file: File) {
        val apkUri = FileProvider.getUriForFile(requireContext(), GenericFileProvider.AUTHORITY, file)

        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(apkUri, "text/xml")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), R.string.activity_not_found_doc, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val MANIFEST_REQUEST = "manifest_request"
        const val TAG = "AndroidManifestFragment"

        fun create(manifestRequest: ManifestRequest) = AndroidManifestFragment().apply {
            arguments = Bundle().apply {
                putParcelable(MANIFEST_REQUEST, manifestRequest)
            }
        }
    }
}
