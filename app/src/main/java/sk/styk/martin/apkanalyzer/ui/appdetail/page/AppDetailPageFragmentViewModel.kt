package sk.styk.martin.apkanalyzer.ui.appdetail.page

import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.DialogComponent
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent

private const val LOADING_STATE = 0
private const val EMPTY_STATE = 1
private const val DATA_STATE = 2

abstract class AppDetailPageFragmentViewModel constructor(
        private val appDetailFragmentViewModel: AppDetailFragmentViewModel,
        val adapter: DetailInfoDescriptionAdapter<*>,
        private val clipBoardManager: ClipBoardManager,
) : ViewModel() {

    val openDescription = adapter.openDescription
            .map {
                DialogComponent(it.name, it.description, TextInfo.from(R.string.close))
            }

    val showSnackbar = adapter.copyToClipboard
            .map {
                clipBoardManager.copyToClipBoard(it.value, it.name)
                SnackBarComponent(TextInfo.from(R.string.copied_to_clipboard), Snackbar.LENGTH_SHORT)
            }

    private val viewStateLiveData = MutableLiveData(LOADING_STATE)
    val viewState: LiveData<Int> = viewStateLiveData

    private val appDetailsObserver = Observer<AppDetailData> {
        val hasData = onDataReceived(it)
        viewStateLiveData.value = if (hasData) DATA_STATE else EMPTY_STATE
    }

    init {
        appDetailFragmentViewModel.appDetails.observeForever(appDetailsObserver)
    }

    override fun onCleared() {
        super.onCleared()
        appDetailFragmentViewModel.appDetails.removeObserver(appDetailsObserver)
    }

    abstract fun onDataReceived(appDetailData: AppDetailData): Boolean

}