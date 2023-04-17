package sk.styk.martin.apkanalyzer.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentStatisticsBinding
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ARROW_ANIMATION_DURATION
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_FLIPPED
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_STANDARD
import sk.styk.martin.apkanalyzer.ui.applist.packagename.AppListFromPackageNamesDialog
import sk.styk.martin.apkanalyzer.util.components.toDialog

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding

    private val viewModel: StatisticsFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCharts()
        binding.viewModel = viewModel

        with(viewModel) {
            showDialog.observe(viewLifecycleOwner) {
                it.toDialog().show(parentFragmentManager, "description dialog")
            }
            showAppList.observe(viewLifecycleOwner) {
                AppListFromPackageNamesDialog.newInstance(it)
                    .show(parentFragmentManager, "AppListDialog")
            }
            analysisResultsExpanded.observe(viewLifecycleOwner) {
                animateArrowExpanded(
                    binding.analysisResultsToggleArrow,
                    it,
                )
            }
            minSdkExpanded.observe(viewLifecycleOwner) {
                animateArrowExpanded(
                    binding.minSdkToggleArrow,
                    it,
                )
            }
            targetSdkExpanded.observe(viewLifecycleOwner) {
                animateArrowExpanded(
                    binding.targetSdkToggleArrow,
                    it,
                )
            }
            installLocationExpanded.observe(viewLifecycleOwner) {
                animateArrowExpanded(
                    binding.installLocationToggleArrow,
                    it,
                )
            }
            signAlgorithmExpanded.observe(viewLifecycleOwner) {
                animateArrowExpanded(
                    binding.signAlgorithmToggleArrow,
                    it,
                )
            }
            appSourceExpanded.observe(viewLifecycleOwner) {
                animateArrowExpanded(
                    binding.appSourceToggleArrow,
                    it,
                )
            }
        }
    }

    private fun animateArrowExpanded(view: View, expanded: Boolean) {
        view.animate().apply {
            cancel()
            setDuration(ARROW_ANIMATION_DURATION).rotation(if (expanded) ROTATION_FLIPPED else ROTATION_STANDARD)
        }
    }

    private fun setupCharts() {
        listOf(
            binding.chartMinSdk,
            binding.chartTargetSdk,
            binding.chartAppSource,
            binding.chartSignAlgorithm,
            binding.chartInstallLocation,
        ).forEach {
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
                        val position = binding.chartAppSource.getPosition(e, YAxis.AxisDependency.RIGHT)
                        MPPointF.recycleInstance(position)
                    }

                    override fun onNothingSelected() {}
                })
            }
        }
    }
}
