package sk.styk.martin.apkanalyzer.ui.activity.repackageddetection

import sk.styk.martin.apkanalyzer.model.server.RepackagedDetectionResult
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface RepackagedDetectionContract {
    interface View {

        fun showLoading()

        fun hideLoading()

        fun showAppOk(result: RepackagedDetectionResult)

        fun showAppNotOk(result: RepackagedDetectionResult)

        fun showAppNotDetected(result: RepackagedDetectionResult)

        fun showNoInternetConnection()

        fun showUploadNotAllowed()

        fun showDetectionError()

        fun showServiceUnavailable()
    }

    interface Presenter : BasePresenter<View>
}