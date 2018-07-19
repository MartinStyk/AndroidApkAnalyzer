package sk.styk.martin.apkanalyzer.ui.activity.repackageddetection

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repackaged_detection.*
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener
import lecho.lib.hellocharts.model.ColumnChartData
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.model.SubcolumnValue
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.upload.task.RepackagedDetectionLoader
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.model.server.RepackagedDetectionResult


/**
 * @author Martin Styk
 * @version 05.01.2018.
 */
class RepackagedDetectionFragment : Fragment(), RepackagedDetectionContract.View {

    private lateinit var presenter: RepackagedDetectionContract.Presenter

    init {
        retainInstance = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = RepackagedDetectionPresenter(RepackagedDetectionLoader(currentData(), requireContext()), loaderManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repackaged_detection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.view = this
        presenter.initialize(currentData())
    }

    override fun showLoading(@StringRes status: Int) {
        activity?.runOnUiThread {
            repackaged_loading.visibility = View.VISIBLE
            repackaged_loading_status.setText(status)
            repackaged_content.visibility = View.GONE
        }
    }

    override fun hideLoading() {
        repackaged_loading.visibility = View.GONE
        repackaged_content.visibility = View.VISIBLE

    }

    override fun showAppOk(result: RepackagedDetectionResult) {
        repackaged_image.setImageResource(R.drawable.ic_ok)
        repackaged_header.text = getString(R.string.repackaged_result_ok)
        repackaged_description.text = resources.getQuantityString(R.plurals.repackaged_result_detection_description_general,
                result.totalSimilarApps,
                result.totalSimilarApps)
        repackaged_description_detail.text = getString(R.string.repackaged_result_ok_description)
    }

    override fun showAppNotOk(result: RepackagedDetectionResult) {
        repackaged_image.setImageResource(R.drawable.ic_warning)
        repackaged_header.text = getString(R.string.repackaged_result_nok)
        repackaged_description.text = resources.getQuantityString(R.plurals.repackaged_result_detection_description_general,
                result.totalSimilarApps,
                result.totalSimilarApps)
        repackaged_description_detail.text = getString(R.string.repackaged_result_nok_description)
    }

    override fun showAppNotDetected(result: RepackagedDetectionResult) {
        repackaged_image.setImageResource(R.drawable.ic_android)
        repackaged_header.text = getString(R.string.repackaged_result_insufficient)
        repackaged_description.text = resources.getQuantityString(R.plurals.repackaged_result_detection_description_general,
                result.totalSimilarApps,
                result.totalSimilarApps)
        repackaged_description_detail.text = getString(R.string.repackaged_result_insufficient_description)
    }

    override fun showNoInternetConnection() {
        repackaged_image.setImageResource(R.drawable.ic_cloud_upload)
        repackaged_header.text = getString(R.string.no_internet_connection)
        repackaged_description.text = getString(R.string.no_internet_connection_description)
    }

    override fun showUploadNotAllowed() {
        repackaged_image.setImageResource(R.drawable.ic_allow_upload)
        repackaged_header.text = getString(R.string.metadata_upload_not_allowed)
        repackaged_description.text = getString(R.string.metadata_upload_not_allowed_description)
    }

    override fun showDetectionError() {
        repackaged_image.setImageResource(R.drawable.ic_not_available)
        repackaged_header.text = getString(R.string.repackaged_error)
        repackaged_description.text = getString(R.string.repackaged_error_description)
    }

    override fun showCheckLater() {
        repackaged_image.setImageResource(R.drawable.ic_return_detection)
        repackaged_header.text = getString(R.string.long_detection)
        repackaged_description.text = getString(R.string.long_detection_description)
    }

    override fun showServiceUnavailable() {
        repackaged_image.setImageResource(R.drawable.ic_not_available)
        repackaged_header.text = getString(R.string.service_not_available)
        repackaged_description.text = getString(R.string.service_not_available_description)
    }

    override fun makeSnack(stringId: Int) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), stringId, Snackbar.LENGTH_LONG).show()
    }

    override fun showDetectionCharts(result: RepackagedDetectionResult, appSignaturePieChartData: PieChartData,
                                     majoritySignaturePieChartData: PieChartData, signatureColumnChartData: ColumnChartData) {
        repackaged_card_signatures_chart.visibility = View.VISIBLE
        repackaged_global_signature_chart.isZoomEnabled = false
        repackaged_global_signature_chart.columnChartData = signatureColumnChartData
        repackaged_global_signature_chart.startDataAnimation()
        repackaged_global_signature_chart.onValueTouchListener = object : ColumnChartOnValueSelectListener {
            override fun onValueSelected(columnIndex: Int, subcolumnIndex: Int, value: SubcolumnValue) =
                    presenter.onSignatureColumnTouch(columnIndex)

            override fun onValueDeselected() {}
        }

        repackaged_card_app_signature.visibility = View.VISIBLE
        repackaged_app_signature_chart.pieChartData = appSignaturePieChartData
        repackaged_app_signature_chart.startDataAnimation()
        repackaged_app_signature_chart.onValueTouchListener = object : PieChartOnValueSelectListener {
            override fun onValueSelected(arcIndex: Int, value: SliceValue) =
                    presenter.onThisAppSignatureRatioPieTouch(arcIndex)

            override fun onValueDeselected() {}
        }
        repackaged_app_signature_description.text = getString(R.string.repackaged_result_detection_app_signature, result.percentageSameSignature.toString())

        repackaged_card_majority_signature.visibility = View.VISIBLE
        repackaged_majority_signature_chart.pieChartData = majoritySignaturePieChartData
        repackaged_majority_signature_chart.startDataAnimation()
        repackaged_majority_signature_chart.onValueTouchListener = object : PieChartOnValueSelectListener {
            override fun onValueSelected(arcIndex: Int, value: SliceValue) =
                    presenter.onMajoritySignatureRatioPieTouch(arcIndex)

            override fun onValueDeselected() {}
        }
        repackaged_majority_signature_description.text = getString(R.string.repackaged_result_detection_majority_signature, result.percentageMajoritySignature.toString())
    }

    private fun currentData(): AppDetailData {
        return arguments?.getParcelable(DATA) ?: throw IllegalArgumentException("no app detail data")
    }

    companion object {
        const val TAG = "RepackagedDetectionFragment"

        private const val DATA = "data"

        fun newInstance(data: AppDetailData): RepackagedDetectionFragment {
            val frag = RepackagedDetectionFragment()
            val args = Bundle()
            args.putParcelable(DATA, data)
            frag.arguments = args
            return frag
        }
    }
}