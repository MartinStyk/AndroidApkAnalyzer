package sk.styk.martin.apkanalyzer.ui.activity.settings

import sk.styk.martin.apkanalyzer.ui.base.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface SettingsContract {
    interface View {
        fun setUpViews()
        fun uploadCheckBoxSet(isChecked: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun uploadCheckBoxStateChange(isChecked: Boolean)
    }
}