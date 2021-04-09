package sk.styk.martin.apkanalyzer.ui.premium

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentPremiumBinding
import sk.styk.martin.apkanalyzer.ui.about.AboutFragment

class PremiumFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentPremiumBinding>(inflater, R.layout.fragment_premium, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        FirebaseAnalytics.getInstance(requireContext())
                .setCurrentScreen(requireActivity(), AboutFragment::class.java.simpleName, AboutFragment::class.java.simpleName)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Hide action bar item for searching
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }
}
