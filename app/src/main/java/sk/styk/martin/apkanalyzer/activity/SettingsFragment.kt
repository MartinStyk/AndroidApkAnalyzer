package sk.styk.martin.apkanalyzer.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.networking.ConnectivityHelper

/**
 * @author Martin Styk
 */
class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.fragment_settings, container, false)

        (rootView.findViewById<View>(R.id.settings_upload_explanation) as TextView).movementMethod = LinkMovementMethod.getInstance()

        val allowUpload = rootView.findViewById<CheckBox>(R.id.allow_upload)
        allowUpload.isChecked = ConnectivityHelper.isConnectionAllowedByUser(context)
        allowUpload.setOnCheckedChangeListener(
                object : CheckBox.OnCheckedChangeListener {
                    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
                        ConnectivityHelper.setConnectionAllowedByUser(context, b)
                    }
                }
        )

        setHasOptionsMenu(true)

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Hide action bar item for searching
        menu!!.clear()

        super.onCreateOptionsMenu(menu, inflater)
    }
}
