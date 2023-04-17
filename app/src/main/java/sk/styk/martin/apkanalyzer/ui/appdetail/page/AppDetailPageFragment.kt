package sk.styk.martin.apkanalyzer.ui.appdetail.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import sk.styk.martin.apkanalyzer.BR
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragment.Companion.APP_DETAIL_REQUEST
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.util.components.toDialog
import sk.styk.martin.apkanalyzer.util.components.toSnackbar
import sk.styk.martin.apkanalyzer.util.provideViewModelOfParentFragment
import javax.inject.Inject

abstract class AppDetailPageFragment<VM : AppDetailPageFragmentViewModel, BINDING : ViewDataBinding> : Fragment() {

    @Inject
    lateinit var parentViewModelFactory: AppDetailFragmentViewModel.Factory

    protected lateinit var binding: BINDING

    protected lateinit var viewModel: VM

    abstract fun createFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): BINDING

    abstract fun createViewModel(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = createFragmentBinding(inflater, container)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setVariable(BR.viewModel, viewModel)
        with(viewModel) {
            openDescription.observe(viewLifecycleOwner) {
                it.toDialog().show(parentFragmentManager, "descrition_dialog")
            }
            showSnackbar.observe(viewLifecycleOwner) {
                it.toSnackbar(requireParentFragment().requireView()).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

    protected fun parentViewModel() = provideViewModelOfParentFragment {
        parentViewModelFactory.create(
            requireNotNull(requireArguments().getParcelable(APP_DETAIL_REQUEST)),
        )
    }
}
