package sk.styk.martin.apkanalyzer.ui.activity.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.databinding.FragmentSettingsBinding
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.NavigationDrawerModel
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var navigationDrawerModel: NavigationDrawerModel

    private lateinit var binding: FragmentSettingsBinding

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                navigationDrawerModel.openDrawer()
            }
        }
    }

}