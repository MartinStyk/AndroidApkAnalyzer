package sk.styk.martin.apkanalyzer.ui.activity.about

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.LinkMovementMethod
import android.view.*
import com.google.firebase.analytics.FirebaseAnalytics
import sk.styk.martin.apkanalyzer.databinding.FragmentAboutBinding

/**
 * @author Martin Styk
 */
class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentAboutBinding>(inflater, R.layout.fragment_about, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        about_app_github_link.movementMethod = LinkMovementMethod.getInstance()
        about_app_privacy_policy.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onResume() {
        super.onResume()
        FirebaseAnalytics.getInstance(requireContext())
                .setCurrentScreen(requireActivity(), AboutFragment::class.java.simpleName, AboutFragment::class.java.simpleName)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Hide action bar item for searching
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }
}
