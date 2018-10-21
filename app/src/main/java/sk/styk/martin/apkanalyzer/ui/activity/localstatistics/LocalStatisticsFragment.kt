package sk.styk.martin.apkanalyzer.ui.activity.localstatistics

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.google.firebase.analytics.FirebaseAnalytics
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener
import lecho.lib.hellocharts.model.SubcolumnValue
import lecho.lib.hellocharts.view.ColumnChartView
import sk.styk.martin.apkanalyzer.business.analysis.task.LocalStatisticsLoader
import sk.styk.martin.apkanalyzer.databinding.FragmentLocalStatisticsBinding
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsDataWithCharts
import sk.styk.martin.apkanalyzer.ui.activity.applist.AppListDialog
import java.util.*

/**
 * @author Martin Styk
 */
class LocalStatisticsFragment : Fragment(), LocalStatisticsContract.View {

    private lateinit var binding: FragmentLocalStatisticsBinding
    private lateinit var presenter: LocalStatisticsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter = LocalStatisticsPresenter(LocalStatisticsLoader(requireContext()), loaderManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_local_statistics, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.view = this
        presenter.initialize()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Hide action bar item for searching
        menu?.clear()

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        FirebaseAnalytics.getInstance(requireContext()).setCurrentScreen(requireActivity(), LocalStatisticsFragment::class.java.simpleName, LocalStatisticsFragment::class.java.simpleName)
    }

    override fun changeProgress(currentProgress: Int, maxProgress: Int) {
        loading_bar.setProgress(currentProgress, maxProgress)
    }

    override fun showStatistics(data: LocalStatisticsDataWithCharts) {
        binding.data = data
        chart_min_sdk.columnChartData = data.minSdkChartData
        chart_target_sdk.columnChartData = data.targetSdkChartData
        chart_install_location.columnChartData = data.installLocationChartData
        chart_sign_algorithm.columnChartData = data.signAlgorithmChartData
        chart_app_source.columnChartData = data.appSourceChartData
    }

    override fun setupCharts() {
        listOf(chart_min_sdk, chart_target_sdk, chart_sign_algorithm, chart_app_source, chart_install_location).forEach {
            it.zoomType = ZoomType.HORIZONTAL
            it.isScrollEnabled = true
            it.setContainerScrollEnabled(true, ContainerScrollType.VERTICAL)
        }
        chart_min_sdk.onValueTouchListener = MinSdkValueTouchListener()
        chart_target_sdk.onValueTouchListener = TargetSdkValueTouchListener()
        chart_sign_algorithm.onValueTouchListener = SignAlgorithmValueTouchListener()
        chart_install_location.onValueTouchListener = InstallLocationValueTouchListener()
        chart_app_source.onValueTouchListener = AppSourceValueTouchListener()
    }

    override fun showAppLists(packages: List<String>) {
        AppListDialog.newInstance(packages as ArrayList<String>)
                .show((context as AppCompatActivity).supportFragmentManager, AppListDialog::class.java.simpleName)
    }


    private inner class MinSdkValueTouchListener : ColumnChartOnValueSelectListener {
        override fun onValueSelected(columnIndex: Int, subcolumnIndex: Int, value: SubcolumnValue) =
                presenter.onMinSdkValueSelected(String((chart_min_sdk as ColumnChartView).columnChartData.axisXBottom.values[columnIndex].labelAsChars))

        override fun onValueDeselected() {}
    }

    private inner class TargetSdkValueTouchListener : ColumnChartOnValueSelectListener {
        override fun onValueSelected(columnIndex: Int, subcolumnIndex: Int, value: SubcolumnValue) =
                presenter.onTargetSdkValueSelected(String((chart_target_sdk as ColumnChartView).columnChartData.axisXBottom.values[columnIndex].labelAsChars))

        override fun onValueDeselected() {}
    }

    private inner class InstallLocationValueTouchListener : ColumnChartOnValueSelectListener {
        override fun onValueSelected(columnIndex: Int, subcolumnIndex: Int, value: SubcolumnValue) =
                presenter.onInstallLocationValueSelected(String((chart_install_location as ColumnChartView).columnChartData.axisXBottom.values[columnIndex].labelAsChars))

        override fun onValueDeselected() {}
    }

    private inner class SignAlgorithmValueTouchListener : ColumnChartOnValueSelectListener {
        override fun onValueSelected(columnIndex: Int, subcolumnIndex: Int, value: SubcolumnValue) =
                presenter.onSignAlgorithmValueSelected(String((chart_sign_algorithm as ColumnChartView).columnChartData.axisXBottom.values[columnIndex].labelAsChars))

        override fun onValueDeselected() {}
    }

    private inner class AppSourceValueTouchListener : ColumnChartOnValueSelectListener {
        override fun onValueSelected(columnIndex: Int, subcolumnIndex: Int, value: SubcolumnValue) =
                presenter.onAppSourceValueSelected(String((chart_app_source as ColumnChartView).columnChartData.axisXBottom.values[columnIndex].labelAsChars))

        override fun onValueDeselected() {}
    }
}
