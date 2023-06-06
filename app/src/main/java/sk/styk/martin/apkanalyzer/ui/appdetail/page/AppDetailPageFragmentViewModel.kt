package sk.styk.martin.apkanalyzer.ui.appdetail.page

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.android.material.snackbar.Snackbar
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppDetailData
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
) : ViewModel(), DefaultLifecycleObserver {

    val openDescription = adapter.openDescription
        .map {
            DialogComponent(it.title, it.message, TextInfo.from(R.string.close))
        }

    val showSnackbar = adapter.copyToClipboard
        .map {
            clipBoardManager.copyToClipBoard(it.text, it.label)
            SnackBarComponent(TextInfo.from(R.string.copied_to_clipboard), Snackbar.LENGTH_SHORT)
        }

    private val viewStateLiveData = MutableLiveData(LOADING_STATE)
    val viewState: LiveData<Int> = viewStateLiveData

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        appDetailFragmentViewModel.appDetails.observe(owner) {
            val hasData = onDataReceived(it)
            viewStateLiveData.value = if (hasData) DATA_STATE else EMPTY_STATE
        }
    }

    abstract fun onDataReceived(appDetailData: AppDetailData): Boolean
}
