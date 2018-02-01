package sk.styk.martin.apkanalyzer.ui.activity.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*
import sk.styk.martin.apkanalyzer.R

/**
 * @author Martin Styk
 */
class SettingsFragment : Fragment(), SettingsContract.View {

    private lateinit var presenter: SettingsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SettingsPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        setHasOptionsMenu(true)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.view = this
        presenter.initialize()
    }

    override fun setUpViews() {
        settings_upload_explanation.movementMethod = LinkMovementMethod.getInstance()
        allow_upload.setOnCheckedChangeListener({ _, isChecked -> presenter.uploadCheckBoxStateChange(isChecked) })
    }

    override fun uploadCheckBoxSet(isChecked: Boolean) {
        allow_upload.isChecked = isChecked
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Hide action bar item for searching
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }
}
