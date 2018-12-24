package sk.styk.martin.apkanalyzer.ui.activity.localstatistics

import android.databinding.DataBindingUtil
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_local_statistics.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.task.LocalStatisticsLoader
import sk.styk.martin.apkanalyzer.databinding.FragmentLocalStatisticsBinding
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsDataWithCharts
import sk.styk.martin.apkanalyzer.ui.activity.applist.AppListDialog
import sk.styk.martin.apkanalyzer.ui.customview.ClickableMarkerView
import java.util.*

/**
 * @author Martin Styk
 */
class LocalStatisticsFragment : Fragment(), LocalStatisticsContract.View, ClickableMarkerView.OnMarkerClickListener {

    private lateinit var binding: FragmentLocalStatisticsBinding
    private lateinit var presenter: LocalStatisticsContract.Presenter
    private val onValueSelectedRectF = RectF()

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
        loading_bar?.setProgress(currentProgress, maxProgress)
    }

    override fun showStatistics(data: LocalStatisticsDataWithCharts) {
        binding.data = data
    }

    override fun setupCharts() {
        listOf(chart_min_sdk, chart_target_sdk, chart_app_source, chart_sign_algorithm, chart_install_location).forEach {
            val mv = ClickableMarkerView(requireContext(), this).apply { chartView = it }

            it.apply {
                marker = mv
                isDragEnabled = true
                isScaleXEnabled = true
                isScaleYEnabled = false
                setDrawValueAboveBar(true)
                setDrawGridBackground(false)
                setPinchZoom(false)
                description.apply {
                    isEnabled = false
                }
                xAxis.apply {
                    isGranularityEnabled = true
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = ContextCompat.getColor(context, R.color.graph_bar)
                }

                axisLeft.apply {
                    setDrawZeroLine(false)
                    setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                    setDrawLimitLinesBehindData(true)
                    textColor = ContextCompat.getColor(context, R.color.graph_bar)
                }

                axisRight.apply {
                    isEnabled = false
                }
                legend.apply {
                    isEnabled = false
                }
                setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        e ?: return

                        val bounds = onValueSelectedRectF
                        chart_app_source.getBarBounds(e as BarEntry, bounds)
                        val position = chart_app_source.getPosition(e, YAxis.AxisDependency.RIGHT)

                        MPPointF.recycleInstance(position)
                    }

                    override fun onNothingSelected() {}
                })
            }
        }

    }

    override fun showAppLists(packages: List<String>) {
        AppListDialog.newInstance(packages as ArrayList<String>)
                .show((context as AppCompatActivity).supportFragmentManager, AppListDialog::class.java.simpleName)
    }

    override fun onMarkerClick(apps: List<String>) {
        showAppLists(apps)
    }
}
