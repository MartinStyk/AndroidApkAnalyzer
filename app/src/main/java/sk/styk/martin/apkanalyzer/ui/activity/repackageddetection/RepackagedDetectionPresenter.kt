package sk.styk.martin.apkanalyzer.ui.activity.repackageddetection

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.Loader
import lecho.lib.hellocharts.model.*
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.business.upload.task.RepackagedDetectionLoader
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
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
        LoaderManager.LoaderCallbacks<RepackagedDetectionLoader.LoaderResult>,
        RepackagedDetectionLoader.ProgressCallback {

    override lateinit var view: RepackagedDetectionContract.View
    lateinit var appDetailData: AppDetailData
    private var repackagedDetectionResult: RepackagedDetectionResult? = null
    private val PIE_CHART_CURRENT_APP_SLICE = 0

    /**
     * Initializes the presenter by showing/hiding proper views and starting data loading.
     */
    override fun initialize(appDetailData: AppDetailData) {
        this.appDetailData = appDetailData
        view.showLoading()
        startLoadingData()
    }

    // Data loading part
    private fun startLoadingData() {
        val loader = loaderManager.initLoader(RepackagedDetectionLoader.ID, Bundle(), this)
        (loader as? RepackagedDetectionLoader)?.setCallback(this)
    }

    override fun onProgressChanged(status: RepackagedDetectionLoader.LoaderStatus) {
        when (status) {
            RepackagedDetectionLoader.LoaderStatus.UPLOADING -> view.showLoading(R.string.uploading_data)
            RepackagedDetectionLoader.LoaderStatus.DETECTING -> view.showLoading(R.string.running_detection)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<RepackagedDetectionLoader.LoaderResult> {
        return loader
    }

    override fun onLoadFinished(loader: Loader<RepackagedDetectionLoader.LoaderResult>, result: RepackagedDetectionLoader.LoaderResult?) {
        view.hideLoading()
        when (result) {
            is RepackagedDetectionLoader.LoaderResult.Success -> {
                repackagedDetectionResult = result.result
                when (result.result.status) {
                    RepackagedDetectionStatus.INSUFFICIENT_DATA -> {
                        view.showAppNotDetected(result.result)
                        view.showDetectionCharts(result.result, generateAppSignatureChart(result.result),
                                generateMajoritySignatureChart(result.result),
                                generateSignaturesChart(result.result))
                    }
                    RepackagedDetectionStatus.NOK -> {
                        view.showAppNotOk(result.result)
                        view.showDetectionCharts(result.result, generateAppSignatureChart(result.result),
                                generateMajoritySignatureChart(result.result),
                                generateSignaturesChart(result.result))
                    }
                    RepackagedDetectionStatus.OK -> {
                        view.showAppOk(result.result)
                        view.showDetectionCharts(result.result, generateAppSignatureChart(result.result),
                                generateMajoritySignatureChart(result.result),
                                generateSignaturesChart(result.result))
                    }
                }

            }
            is RepackagedDetectionLoader.LoaderResult.NotConnectedToInternet -> view.showNoInternetConnection()
            is RepackagedDetectionLoader.LoaderResult.UserNotAllowedUpload -> view.showUploadNotAllowed()
            is RepackagedDetectionLoader.LoaderResult.ServiceNotAvailable -> view.showServiceUnavailable()
            is RepackagedDetectionLoader.LoaderResult.CommunicationError -> view.showDetectionError()
            is RepackagedDetectionLoader.LoaderResult.LongOperationError -> view.showCheckLater()

        }
    }

    override fun onLoaderReset(loader: Loader<RepackagedDetectionLoader.LoaderResult>) {}

    override fun onSignatureColumnTouch(columnIndex: Int) {
        val touchedEntry = repackagedDetectionResult?.signaturesNumberOfApps?.toList()?.sortedBy { (_, value) -> -value }?.get(columnIndex)
        touchedEntry?.let {
            if (touchedEntry.first == appDetailData.certificateData.certificateHash) {
                view.makeSnack(R.string.repackaged_global_signature_current_app_touch)
            } else {
                view.makeSnack(R.string.repackaged_global_signature_other_app_touch)
            }
        }
    }

    override fun onThisAppSignatureRatioPieTouch(arcIndex: Int) {
        if (arcIndex == PIE_CHART_CURRENT_APP_SLICE) {
            view.makeSnack(R.string.repackaged_result_detection_app_signature_same_dev_touch)
        } else {
            view.makeSnack(R.string.repackaged_result_detection_app_signature_other_devs_touch)
        }
    }

    override fun onMajoritySignatureRatioPieTouch(arcIndex: Int) {
        if (arcIndex == PIE_CHART_CURRENT_APP_SLICE) {
            view.makeSnack(R.string.repackaged_result_detection_majority_signature_most_common_devs_touch)
        } else {
            view.makeSnack(R.string.repackaged_result_detection_majority_signature_other_devs_touch)
        }
    }

    private fun generateAppSignatureChart(result: RepackagedDetectionResult): PieChartData {
        val percentageSameSignature = result.percentageSameSignature

        val values = ArrayList<SliceValue>(2)

        val sliceInteresting = SliceValue(0f, ContextCompat.getColor(context, R.color.accent))
        sliceInteresting.setLabel(context.getString(R.string.repackaged_result_detection_app_signature_same_dev, percentageSameSignature.toString()))
        sliceInteresting.setTarget(percentageSameSignature.toFloat())

        val sliceRest = SliceValue(100f, ContextCompat.getColor(context, R.color.primary))
        sliceRest.setTarget(100 - percentageSameSignature.toFloat())
        sliceRest.setLabel(context.getString(R.string.repackaged_result_detection_app_signature_other_devs, BigDecimal(100).minus(percentageSameSignature).toString()))

        values.add(sliceInteresting)
        values.add(sliceRest)

        val data = PieChartData(values)
        data.setHasCenterCircle(true)
        data.setHasLabels(true)
        data.centerCircleScale = 0.5f
        return data
    }

    private fun generateMajoritySignatureChart(result: RepackagedDetectionResult): PieChartData {
        val percentageMajoritySignature = result.percentageMajoritySignature

        val values = ArrayList<SliceValue>(2)

        val sliceInteresting = SliceValue(0f, ContextCompat.getColor(context, R.color.accent))
        sliceInteresting.setLabel(context.getString(R.string.repackaged_result_detection_majority_signature_most_common_devs, percentageMajoritySignature.toString()))
        sliceInteresting.setTarget(percentageMajoritySignature.toFloat())

        val sliceRest = SliceValue(100f, ContextCompat.getColor(context, R.color.primary))
        sliceRest.setTarget(100 - percentageMajoritySignature.toFloat())
        sliceRest.setLabel(context.getString(R.string.repackaged_result_detection_majority_signature_other_devs, BigDecimal(100).minus(percentageMajoritySignature).toString()))

        values.add(sliceInteresting)
        values.add(sliceRest)

        val data = PieChartData(values)
        data.setHasCenterCircle(true)
        data.setHasLabels(true)
        data.centerCircleScale = 0.5f
        return data
    }

    private fun generateSignaturesChart(result: RepackagedDetectionResult): ColumnChartData {
        val columns = ArrayList<Column>(result.signaturesNumberOfApps.size)

        val sorted = result.signaturesNumberOfApps.toList().sortedBy { (_, value) -> -value }.toMap()

        sorted.forEach {
            val color = if (it.key == appDetailData.certificateData.certificateHash)
                R.color.accent
            else
                R.color.primary

            val column = Column(listOf(SubcolumnValue(0f, ContextCompat.getColor(context, color)).setTarget(it.value.toFloat())))
            column.setHasLabels(true)
            columns.add(column)
        }

        return ColumnChartData(columns)
    }
}
