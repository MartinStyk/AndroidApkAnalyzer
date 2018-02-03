package sk.styk.martin.apkanalyzer.ui.activity.appdetail.base

import android.net.Uri
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface AppDetailActivityContract {
    interface View {
        fun setupViews()
    }

    interface Presenter : BasePresenter<View> {
    }

    companion object {

    }
}