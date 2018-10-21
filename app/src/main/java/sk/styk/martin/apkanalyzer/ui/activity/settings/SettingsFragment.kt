package sk.styk.martin.apkanalyzer.ui.activity.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.LinkMovementMethod
import android.view.*
import com.google.firebase.analytics.FirebaseAnalytics

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.view = this
        presenter.initialize()
    }

    override fun onResume() {
        super.onResume()
        FirebaseAnalytics.getInstance(requireContext()).setCurrentScreen(requireActivity(), SettingsFragment::class.java.simpleName, SettingsFragment::class.java.simpleName)
    }

    override fun setUpViews() {
        settings_upload_explanation.movementMethod = LinkMovementMethod.getInstance()
        // TODO: fix "Cannot infer a type for this parameter. Please specify it explicitly."
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
