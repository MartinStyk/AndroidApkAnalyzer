package sk.styk.martin.apkanalyzer.ui.statistics

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.core.appanalysis.MAX_SDK_VERSION
import sk.styk.martin.apkanalyzer.core.common.resources.ResourcesManager
import sk.styk.martin.apkanalyzer.core.uilibrary.ColorInfo
import sk.styk.martin.apkanalyzer.core.appstatistics.LocalApplicationStatisticManager
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.NavigationDrawerModel
import sk.styk.martin.apkanalyzer.core.appstatistics.model.StatisticsData
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.DialogComponent
import sk.styk.martin.apkanalyzer.core.common.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject
import kotlin.math.roundToInt

private const val LOADING_STATE = 0
private const val DATA_STATE = 1

typealias PackageName = String

@HiltViewModel
class StatisticsFragmentViewModel @Inject constructor(
    private val navigationDrawerModel: NavigationDrawerModel,
    private val localApplicationStatisticManager: LocalApplicationStatisticManager,
    private val resourcesManager: ResourcesManager,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel(), Toolbar.OnMenuItemClickListener {

    private val viewStateLiveData = MutableLiveData(LOADING_STATE)
    val viewState: LiveData<Int> = viewStateLiveData.distinctUntilChanged()

    private val loadingProgressLiveData = MutableLiveData<Int>()
    val loadingProgress: LiveData<Int> = loadingProgressLiveData

    private val loadingProgressMaxLiveData = MutableLiveData<Int>()
    val loadingProgressMax: LiveData<Int> = loadingProgressMaxLiveData

    private val statisticDataLiveData = MutableLiveData<StatisticsDataWithCharts>()
    val statisticData: LiveData<StatisticsDataWithCharts> = statisticDataLiveData

    private val showDialogEvent = SingleLiveEvent<DialogComponent>()
    val showDialog: LiveData<DialogComponent> = showDialogEvent

    private val showAppListEvent = SingleLiveEvent<List<PackageName>>()
    val showAppList: LiveData<List<PackageName>> = showAppListEvent

    private val analysisResultsExpandedLiveData = MutableLiveData(true)
    val analysisResultsExpanded: LiveData<Boolean> = analysisResultsExpandedLiveData

    private val minSdkExpandedLiveData = MutableLiveData(false)
    val minSdkExpanded: LiveData<Boolean> = minSdkExpandedLiveData

    private val targetSdkExpandedLiveData = MutableLiveData(false)
    val targetSdkExpanded: LiveData<Boolean> = targetSdkExpandedLiveData

    private val installLocationExpandedLiveData = MutableLiveData(false)
    val installLocationExpanded: LiveData<Boolean> = installLocationExpandedLiveData

    private val signAlgorithmExpandedLiveData = MutableLiveData(false)
    val signAlgorithmExpanded: LiveData<Boolean> = signAlgorithmExpandedLiveData

    private val appSourceExpandedLiveData = MutableLiveData(false)
    val appSourceExpanded: LiveData<Boolean> = appSourceExpandedLiveData

    private val appSizeExpandedLiveData = MutableLiveData(false)
    val appSizeExpanded: LiveData<Boolean> = appSizeExpandedLiveData

    private val activitiesExpandedLiveData = MutableLiveData(false)
    val activitiesExpanded: LiveData<Boolean> = activitiesExpandedLiveData

    private val servicesExpandedLiveData = MutableLiveData(false)
    val servicesExpanded: LiveData<Boolean> = servicesExpandedLiveData

    private val providersExpandedLiveData = MutableLiveData(false)
    val providersExpanded: LiveData<Boolean> = providersExpandedLiveData

    private val receiversExpandedLiveData = MutableLiveData(false)
    val receiversExpanded: LiveData<Boolean> = receiversExpandedLiveData

    private val usedPermissionsExpandedLiveData = MutableLiveData(false)
    val usedPermissionsExpanded: LiveData<Boolean> = usedPermissionsExpandedLiveData

    private val definedPermissionsExpandedLiveData = MutableLiveData(false)
    val definedPermissionsExpanded: LiveData<Boolean> = definedPermissionsExpandedLiveData

    init {
        viewModelScope.launch {
            localApplicationStatisticManager.loadStatisticsData()
                .flowOn(dispatcherProvider.default())
                .collect {
                    when (it) {
                        is LocalApplicationStatisticManager.StatisticsLoadingStatus.Loading -> {
                            loadingProgressLiveData.value = it.currentProgress
                            loadingProgressMaxLiveData.value = it.totalProgress
                            viewStateLiveData.value = LOADING_STATE
                        }
                        is LocalApplicationStatisticManager.StatisticsLoadingStatus.Data -> {
                            val data = withContext(dispatcherProvider.default()) {
                                StatisticsDataWithCharts(
                                    statisticsData = it.data,
                                    minSdkChartData = getBarSdkData(it.data.minSdk),
                                    targetSdkChartData = getBarSdkData(it.data.targetSdk),
                                    installLocationChartData = getBarData(it.data.installLocation),
                                    appSourceChartData = getBarData(it.data.appSource),
                                    signAlgorithChartData = getBarData(it.data.signAlgorithm),
                                )
                            }
                            statisticDataLiveData.value = data
                            viewStateLiveData.value = DATA_STATE
                        }
                    }
                }
        }
    }

    fun onNavigationClick() = viewModelScope.launch {
        navigationDrawerModel.openDrawer()
    }

    fun showDetail(title: String, message: String) {
        showDialogEvent.value = DialogComponent(TextInfo.from(title), TextInfo.from(message), TextInfo.from(R.string.close))
    }

    fun onChartMarkerClick(chartEntry: Entry) {
        val packageNames = chartEntry.data as? List<PackageName> ?: return
        showAppListEvent.value = packageNames
    }

    fun toggleAnalysisResultExpanded() {
        analysisResultsExpandedLiveData.value = analysisResultsExpandedLiveData.value?.not()
    }

    fun toggleMinSdkExpanded() {
        minSdkExpandedLiveData.value = minSdkExpandedLiveData.value?.not()
    }

    fun toggleTargetSdkExpanded() {
        targetSdkExpandedLiveData.value = targetSdkExpandedLiveData.value?.not()
    }

    fun toggleInstallLocationExpanded() {
        installLocationExpandedLiveData.value = installLocationExpandedLiveData.value?.not()
    }

    fun toggleSignAlgorithmExpanded() {
        signAlgorithmExpandedLiveData.value = signAlgorithmExpandedLiveData.value?.not()
    }

    fun toggleAppSourceExpanded() {
        appSourceExpandedLiveData.value = appSourceExpandedLiveData.value?.not()
    }

    fun toggleAppSizeExpanded() {
        appSizeExpandedLiveData.value = appSizeExpandedLiveData.value?.not()
    }

    fun toggleActivitiesExpanded() {
        activitiesExpandedLiveData.value = activitiesExpandedLiveData.value?.not()
    }

    fun toggleServicesExpanded() {
        servicesExpandedLiveData.value = servicesExpandedLiveData.value?.not()
    }

    fun toggleReceiversExpanded() {
        receiversExpandedLiveData.value = receiversExpandedLiveData.value?.not()
    }

    fun toggleProvidersExpanded() {
        providersExpandedLiveData.value = providersExpandedLiveData.value?.not()
    }

    fun toggleUsedPermissionsExpanded() {
        usedPermissionsExpandedLiveData.value = usedPermissionsExpandedLiveData.value?.not()
    }

    fun toggleDefinedPermissionsExpanded() {
        definedPermissionsExpandedLiveData.value = definedPermissionsExpandedLiveData.value?.not()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_expand_all -> {
                setAllExpanded(true)
                true
            }
            R.id.action_collapse_all -> {
                setAllExpanded(false)
                true
            }
            else -> false
        }
    }

    private fun setAllExpanded(isExpanded: Boolean) {
        analysisResultsExpandedLiveData.value = isExpanded
        minSdkExpandedLiveData.value = isExpanded
        targetSdkExpandedLiveData.value = isExpanded
        installLocationExpandedLiveData.value = isExpanded
        signAlgorithmExpandedLiveData.value = isExpanded
        appSourceExpandedLiveData.value = isExpanded
        appSizeExpandedLiveData.value = isExpanded
        activitiesExpandedLiveData.value = isExpanded
        servicesExpandedLiveData.value = isExpanded
        providersExpandedLiveData.value = isExpanded
        receiversExpandedLiveData.value = isExpanded
        usedPermissionsExpandedLiveData.value = isExpanded
        definedPermissionsExpandedLiveData.value = isExpanded
    }

    private fun getBarSdkData(
        map: Map<Int, List<String>>,
        columnColor: ColorInfo = ColorInfo.fromColor(R.color.graph_bar),
        selectedColumnColor: ColorInfo = ColorInfo.fromColor(R.color.secondary),
    ): BarDataHolder {
        val values = ArrayList<BarEntry>(map.size)
        val axisValues = ArrayList<String>(map.size)
        var index = 0f
        for (sdk in 1..MAX_SDK_VERSION) {
            if (map[sdk] == null) {
                continue
            }

            val applicationCount = map[sdk]?.size ?: 0

            values.add(BarEntry(index++, applicationCount.toFloat(), map[sdk]))
            axisValues.add(sdk.toString())
        }

        return BarDataHolder(
            BarData(
                listOf<IBarDataSet>(
                    BarDataSet(values, "mLabel")
                        .apply {
                            color = resourcesManager.getColor(columnColor)
                            highLightColor = resourcesManager.getColor(selectedColumnColor)
                            highLightAlpha = 255
                            valueTextColor = color
                            isHighlightEnabled = true
                        },
                ),
            ),
        ) { i, _ -> axisValues[i.roundToInt()] }
    }

    private fun getBarData(
        map: Map<*, List<String>>,
        columnColor: ColorInfo = ColorInfo.fromColor(R.color.graph_bar),
        selectedColumnColor: ColorInfo = ColorInfo.fromColor(R.color.secondary),
    ): BarDataHolder {
        val values = mutableListOf<BarEntry>()
        val axisValues = mutableListOf<String>()
        var index = 1f
        for ((key, value) in map) {
            values.add(BarEntry(index++, value.size.toFloat(), value))
            axisValues.add(key.toString())
        }

        return BarDataHolder(
            BarData(
                listOf<IBarDataSet>(
                    BarDataSet(values, "mLabel")
                        .apply {
                            color = resourcesManager.getColor(columnColor)
                            highLightColor = resourcesManager.getColor(selectedColumnColor)
                            highLightAlpha = 255
                            valueTextColor = color
                            isHighlightEnabled = true
                        },
                ),
            ),
        ) { i, _ -> axisValues[i.roundToInt() - 1] }
    }

    data class BarDataHolder(
        val data: BarData,
        val valueFormatter: IAxisValueFormatter,
    )

    data class StatisticsDataWithCharts(
        val statisticsData: StatisticsData,
        val minSdkChartData: BarDataHolder,
        val targetSdkChartData: BarDataHolder,
        val installLocationChartData: BarDataHolder,
        val appSourceChartData: BarDataHolder,
        val signAlgorithChartData: BarDataHolder,
    )
}
