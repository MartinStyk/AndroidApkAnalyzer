package sk.styk.martin.apkanalyzer.ui.activity.localstatistics

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_local_statistics.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentLocalStatisticsBinding
import sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel.ViewModelFactory
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ARROW_ANIMATION_DURATION
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_FLIPPED
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_STANDARD
import sk.styk.martin.apkanalyzer.ui.applist.packagename.AppListFromPackageNamesDialog
import sk.styk.martin.apkanalyzer.util.components.toDialog
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

class LocalStatisticsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentLocalStatisticsBinding

    private lateinit var viewModel: LocalStatisticsFragmentViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_local_statistics, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCharts()
        binding.viewModel = viewModel

        with(viewModel) {
            showDialog.observe(viewLifecycleOwner, { it.toDialog().show(parentFragmentManager, "description dialog") })
            showAppList.observe(viewLifecycleOwner, { AppListFromPackageNamesDialog.newInstance(it).show(parentFragmentManager, "AppListDialog") })
            analysisResultsExpanded.observe(viewLifecycleOwner, { animateArrowExpanded(binding.analysisResultsToggleArrow, it) })
            minSdkExpanded.observe(viewLifecycleOwner, { animateArrowExpanded(binding.minSdkToggleArrow, it) })
            targetSdkExpanded.observe(viewLifecycleOwner, { animateArrowExpanded(binding.targetSdkToggleArrow, it) })
            installLocationExpanded.observe(viewLifecycleOwner, { animateArrowExpanded(binding.installLocationToggleArrow, it) })
            signAlgorithmExpanded.observe(viewLifecycleOwner, { animateArrowExpanded(binding.signAlgorithmToggleArrow, it) })
            appSourceExpanded.observe(viewLifecycleOwner, { animateArrowExpanded(binding.appSourceToggleArrow, it) })
        }
    }

    private fun animateArrowExpanded(view: View, expanded: Boolean) {
        view.animate().apply {
            cancel()
            setDuration(ARROW_ANIMATION_DURATION).rotation(if (expanded) ROTATION_FLIPPED else ROTATION_STANDARD)
        }
    }

    private fun setupCharts() {
        listOf(chart_min_sdk, chart_target_sdk, chart_app_source, chart_sign_algorithm, chart_install_location).forEach {
            it.apply {
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
                        val position = chart_app_source.getPosition(e, YAxis.AxisDependency.RIGHT)
                        MPPointF.recycleInstance(position)
                    }

                    override fun onNothingSelected() {}
                })
            }
        }
    }

}
