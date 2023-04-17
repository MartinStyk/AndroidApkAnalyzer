package sk.styk.martin.apkanalyzer.ui.permission.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionListBinding
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragment
import sk.styk.martin.apkanalyzer.util.FragmentTag

@AndroidEntryPoint
class PermissionListFragment : Fragment() {

    private lateinit var binding: FragmentPermissionListBinding

    private val viewModel: PermissionListFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPermissionListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding.recyclerViewPermissions.doOnPreDraw { startPostponedEnterTransition() }

        binding.viewModel = viewModel

        viewModel.openPermission.observe(viewLifecycleOwner, this::openPermissionDetail)
    }

    private fun openPermissionDetail(permissionClickData: PermissionListAdapter.PermissionClickData) {
        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
        parentFragmentManager.beginTransaction().apply {
            permissionClickData.view.get()?.let { addSharedElement(it, getString(R.string.transition_permission_detail)) }
        }.replace(
            R.id.container,
            PermissionDetailFragment.create(permissionClickData.localPermissionData),
            FragmentTag.PermissionDetail.tag,
        )
            .setReorderingAllowed(true)
            .addToBackStack(FragmentTag.PermissionDetail.tag)
            .commit()
    }
}
