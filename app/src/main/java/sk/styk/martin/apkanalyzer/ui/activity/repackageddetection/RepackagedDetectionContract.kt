package sk.styk.martin.apkanalyzer.ui.activity.repackageddetection

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

        fun showLoading()

        fun hideLoading()

        fun showAppOk(result: RepackagedDetectionResult, appSignaturePieChartData: PieChartData, majoritySignaturePieChartData: PieChartData,
                      signatureColumnChartData: ColumnChartData)

        fun showAppNotOk(result: RepackagedDetectionResult, appSignaturePieChartData: PieChartData, majoritySignaturePieChartData: PieChartData,
                         signatureColumnChartData: ColumnChartData)

        fun showAppNotDetected(result: RepackagedDetectionResult, appSignaturePieChartData: PieChartData, majoritySignaturePieChartData: PieChartData,
                               signatureColumnChartData: ColumnChartData)

        fun showNoInternetConnection()

        fun showUploadNotAllowed()

        fun showDetectionError()

        fun showServiceUnavailable()
    }

    interface Presenter : BasePresenter<View> {
        fun initialize(appDetailData: AppDetailData)
    }
}