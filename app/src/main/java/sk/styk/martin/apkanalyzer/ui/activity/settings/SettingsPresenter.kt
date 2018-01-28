package sk.styk.martin.apkanalyzer.ui.activity.repackageddetection

import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.util.networking.ConnectivityHelper


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class SettingsPresenter : SettingsContract.Presenter {

    lateinit var view: SettingsContract.View

    override fun initialize() {
        view.setUpViews()
        view.uploadCheckBoxSet(ConnectivityHelper.isConnectionAllowedByUser(context))
    }

    override fun uploadCheckBoxStateChange(isChecked: Boolean) {
        ConnectivityHelper.setConnectionAllowedByUser(context, isChecked)
    }
}