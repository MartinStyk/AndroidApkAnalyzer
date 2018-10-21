package sk.styk.martin.apkanalyzer.ui.activity.repackageddetection

import android.support.annotation.StringRes
import lecho.lib.hellocharts.model.ColumnChartData
import lecho.lib.hellocharts.model.PieChartData
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.model.server.RepackagedDetectionResult
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface RepackagedDetectionContract {
    interface View {

        fun showLoading(@StringRes status: Int = R.string.uploading_data)

        fun hideLoading()

        fun showAppOk(result: RepackagedDetectionResult)

        fun showAppNotOk(result: RepackagedDetectionResult)

        fun showAppNotDetected(result: RepackagedDetectionResult)

        fun showDetectionCharts(result: RepackagedDetectionResult, appSignaturePieChartData: PieChartData,
                                majoritySignaturePieChartData: PieChartData, signatureColumnChartData: ColumnChartData)

        fun showNoInternetConnection()

        fun showUploadNotAllowed()

        fun showDetectionError()

        fun showCheckLater()

        fun showServiceUnavailable()

        fun makeSnack(@StringRes stringId: Int)
    }

    interface Presenter : BasePresenter<View> {
        fun initialize(appDetailData: AppDetailData)

        fun onSignatureColumnTouch(columnIndex: Int)

        fun onThisAppSignatureRatioPieTouch(arcIndex: Int)

        fun onMajoritySignatureRatioPieTouch(arcIndex: Int)
    }
}