package sk.styk.martin.apkanalyzer.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import sk.styk.martin.apkanalyzer.BuildConfig
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.file.AppOperations

/**
 * @author Martin Styk
 */
class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.fragment_about, container, false)

        (rootView.findViewById<View>(R.id.about_app_version) as TextView).text = BuildConfig.VERSION_NAME
        (rootView.findViewById<View>(R.id.about_app_github_link) as TextView).movementMethod = LinkMovementMethod.getInstance()
        (rootView.findViewById<View>(R.id.about_app_privacy_policy) as TextView).movementMethod = LinkMovementMethod.getInstance()

        rootView.findViewById<View>(R.id.about_app_rate).setOnClickListener { AppOperations().openGooglePlay(context, context.packageName) }

        setHasOptionsMenu(true)

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Hide action bar item for searching
        menu!!.clear()

        super.onCreateOptionsMenu(menu, inflater)
    }
}
