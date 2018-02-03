package sk.styk.martin.apkanalyzer.ui.activity.repackageddetection

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.business.upload.task.RepackagedDetectionLoader
import sk.styk.martin.apkanalyzer.model.server.RepackagedDetectionStatus


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class RepackagedDetectionPresenter(
        private val loader: ApkAnalyzerAbstractAsyncLoader<RepackagedDetectionLoader.LoaderResult>,
        private val loaderManager: LoaderManager
) : RepackagedDetectionContract.Presenter,
        LoaderManager.LoaderCallbacks<RepackagedDetectionLoader.LoaderResult> {

    override lateinit var view: RepackagedDetectionContract.View
    /**
     * Initializes the presenter by showing/hiding proper views and starting data loading.
     */
    override fun initialize() {
        view.showLoading()
        startLoadingData()
    }

    // Data loading part
    private fun startLoadingData() {
        loaderManager.initLoader(RepackagedDetectionLoader.ID, Bundle(), this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<RepackagedDetectionLoader.LoaderResult> {
        return loader
    }

    override fun onLoadFinished(loader: Loader<RepackagedDetectionLoader.LoaderResult>?, result: RepackagedDetectionLoader.LoaderResult?) {
        view.hideLoading()
        when (result) {
            is RepackagedDetectionLoader.LoaderResult.Success -> {
                when (result.result.status) {
                    RepackagedDetectionStatus.NOK -> view.showAppNotOk(result.result)
                    RepackagedDetectionStatus.OK -> view.showAppOk(result.result)
                    RepackagedDetectionStatus.INSUFFICIENT_DATA -> view.showAppNotDetected(result.result)
                }
            }
            is RepackagedDetectionLoader.LoaderResult.NotConnectedToInternet -> view.showNoInternetConnection()
            is RepackagedDetectionLoader.LoaderResult.UserNotAllowedUpload -> view.showUploadNotAllowed()
            is RepackagedDetectionLoader.LoaderResult.ServiceNotAvailable -> view.showServiceUnavailable()
            is RepackagedDetectionLoader.LoaderResult.CommunicationError -> view.showDetectionError()
        }
    }

    override fun onLoaderReset(loader: Loader<RepackagedDetectionLoader.LoaderResult>?) {}
}