package sk.styk.martin.apkanalyzer.ui.activity.repackageddetection

import sk.styk.martin.apkanalyzer.ui.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface RepackagedDetectionContract {
    interface View {

        fun showLoading()

        fun hideLoading()

        fun showAppOk()

        fun showAppNotOk()

        fun showAppNotDetected()

        fun showNoInternetConnection()

        fun showUploadNotAllowed()

        fun showDetectionError()

        fun showServiceUnavailable()
    }

    interface Presenter : BasePresenter
}