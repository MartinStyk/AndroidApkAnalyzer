package sk.styk.martin.apkanalyzer.ui.activity.repackageddetection

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.Loader
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.business.upload.task.RepackagedDetectionLoader
import sk.styk.martin.apkanalyzer.model.server.RepackagedDetectionResult
import sk.styk.martin.apkanalyzer.model.server.RepackagedDetectionStatus
import java.math.BigDecimal
import java.util.*


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
                    RepackagedDetectionStatus.NOK -> view.showAppNotOk(result.result, generateAppSignatureChart(result.result), generateMajoritySignatureChart(result.result))
                    RepackagedDetectionStatus.OK -> view.showAppOk(result.result, generateAppSignatureChart(result.result), generateMajoritySignatureChart(result.result))
                    RepackagedDetectionStatus.INSUFFICIENT_DATA -> view.showAppNotDetected(result.result, generateAppSignatureChart(result.result), generateMajoritySignatureChart(result.result))
                }
            }
            is RepackagedDetectionLoader.LoaderResult.NotConnectedToInternet -> view.showNoInternetConnection()
            is RepackagedDetectionLoader.LoaderResult.UserNotAllowedUpload -> view.showUploadNotAllowed()
            is RepackagedDetectionLoader.LoaderResult.ServiceNotAvailable -> view.showServiceUnavailable()
            is RepackagedDetectionLoader.LoaderResult.CommunicationError -> view.showDetectionError()
        }
    }

    override fun onLoaderReset(loader: Loader<RepackagedDetectionLoader.LoaderResult>?) {}

    private fun generateAppSignatureChart(result: RepackagedDetectionResult): PieChartData {

//        val percentageSameSignature = result.percentageSameSignature
        val percentageSameSignature = BigDecimal.TEN

        val values = ArrayList<SliceValue>(2)

        val sliceInteresting = SliceValue(0f, ContextCompat.getColor(context, R.color.accent))
        sliceInteresting.setLabel(context.getString(R.string.repackaged_result_detection_app_signature_same_dev,percentageSameSignature.toString ()))
        sliceInteresting.setTarget(percentageSameSignature.toFloat())

        val sliceRest = SliceValue(100f, ContextCompat.getColor(context, R.color.primary_light))
        sliceRest.setTarget(100 - percentageSameSignature.toFloat())
        sliceRest.setLabel(context.getString(R.string.repackaged_result_detection_app_signature_other_devs, BigDecimal(100).minus(percentageSameSignature).toString()))

        values.add(sliceInteresting)
        values.add(sliceRest)

        val data = PieChartData(values)
        data.setHasCenterCircle(true)
        data.setHasLabels(true)
        data.setCenterCircleScale(0.5f)
        return data
    }

    private fun generateMajoritySignatureChart(result: RepackagedDetectionResult): PieChartData {
        val percentageMajoritySignature = BigDecimal.valueOf(25)

        val values = ArrayList<SliceValue>(2)

        val sliceInteresting = SliceValue(0f, ContextCompat.getColor(context, R.color.accent))
        sliceInteresting.setLabel(context.getString(R.string.repackaged_result_detection_majority_signature_most_common_devs, percentageMajoritySignature.toString ()))
        sliceInteresting.setTarget(percentageMajoritySignature.toFloat())

        val sliceRest = SliceValue(100f, ContextCompat.getColor(context, R.color.primary_light))
        sliceRest.setTarget(100 - percentageMajoritySignature.toFloat())
        sliceRest.setLabel(context.getString(R.string.repackaged_result_detection_majority_signature_other_devs, BigDecimal(100).minus(percentageMajoritySignature).toString()))

        values.add(sliceInteresting)
        values.add(sliceRest)

        val data = PieChartData(values)
        data.setHasCenterCircle(true)
        data.setHasLabels(true)
        data.setCenterCircleScale(0.5f)
        return data
    }
}