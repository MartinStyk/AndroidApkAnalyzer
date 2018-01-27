package sk.styk.martin.apkanalyzer.presenter

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import sk.styk.martin.apkanalyzer.activity.RepackagedDetectionView
import sk.styk.martin.apkanalyzer.business.upload.task.RepackagedDetectionLoader
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.model.server.RepackagedDetectionStatus


/**
 * Created by Martin Styk on 27.01.2018.
 */
class RepackagedDetectionPresenter : LoaderManager.LoaderCallbacks<RepackagedDetectionLoader.LoaderResult>, Presenter {

    private lateinit var data: AppDetailData

    lateinit var view: RepackagedDetectionView


    /**
     * Initializes the presenter by showing/hiding proper views and starting data loading.
     */
    fun initialize(data: AppDetailData) {
        this.data = data
        showViewLoading()
        startLoadingData()
    }

    private fun startLoadingData() {
        view.getLoaderManager().initLoader(RepackagedDetectionLoader.ID, Bundle(), this)
    }


    private fun showViewLoading() {
        view.showLoading()
    }

    private fun hideViewLoading() {
        view.hideLoading()
    }


    // Data loading part

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<RepackagedDetectionLoader.LoaderResult> {
        return RepackagedDetectionLoader(data, view.getContext())
    }

    override fun onLoadFinished(loader: Loader<RepackagedDetectionLoader.LoaderResult>?, result: RepackagedDetectionLoader.LoaderResult?) {
        view.hideLoading()
        when (result) {
            is RepackagedDetectionLoader.LoaderResult.Success -> {
                when (result.result.status) {
                    RepackagedDetectionStatus.NOK -> view.showAppNotOk()
                    RepackagedDetectionStatus.OK -> view.showAppOk()
                    RepackagedDetectionStatus.INSUFFICIENT_DATA -> view.showAppNotDetected()
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