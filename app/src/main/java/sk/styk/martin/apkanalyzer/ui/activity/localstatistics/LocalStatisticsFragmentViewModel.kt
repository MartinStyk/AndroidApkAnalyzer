package sk.styk.martin.apkanalyzer.ui.activity.localstatistics

import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.appanalysis.LocalApplicationStatisticManager
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.NavigationDrawerModel
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsDataWithCharts
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.DialogComponent
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

private const val LOADING_STATE = 0
private const val DATA_STATE = 1

class LocalStatisticsFragmentViewModel @Inject constructor(
        private val navigationDrawerModel: NavigationDrawerModel,
        private val localApplicationStatisticManager: LocalApplicationStatisticManager,
        private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val viewStateLiveData = MutableLiveData(LOADING_STATE)
    val viewState: LiveData<Int> = viewStateLiveData.distinctUntilChanged()

    private val loadingProgressLiveData = MutableLiveData<Int>()
    val loadingProgress: LiveData<Int> = loadingProgressLiveData

    private val loadingProgressMaxLiveData = MutableLiveData<Int>()
    val loadingProgressMax: LiveData<Int> = loadingProgressMaxLiveData

    private val statisticDataLiveData = MutableLiveData<LocalStatisticsDataWithCharts>()
    val statisticData: LiveData<LocalStatisticsDataWithCharts> = statisticDataLiveData

    private val showDialogEvent = SingleLiveEvent<DialogComponent>()
    val showDialog: LiveData<DialogComponent> = showDialogEvent

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
                                statisticDataLiveData.value = it.data
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

}