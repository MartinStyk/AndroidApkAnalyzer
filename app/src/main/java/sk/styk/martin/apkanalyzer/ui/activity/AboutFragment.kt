package sk.styk.martin.apkanalyzer.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_about.*
import sk.styk.martin.apkanalyzer.BuildConfig
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.file.AppOperations

/**
 * @author Martin Styk
 */
class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        about_app_version.text = BuildConfig.VERSION_NAME
        about_app_github_link.movementMethod = LinkMovementMethod.getInstance()
        about_app_privacy_policy.movementMethod = LinkMovementMethod.getInstance()

        about_app_rate.setOnClickListener { AppOperations.openGooglePlay(context, context.packageName) }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Hide action bar item for searching
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }
}
