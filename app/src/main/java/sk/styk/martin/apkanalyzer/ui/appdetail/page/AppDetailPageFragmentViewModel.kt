package sk.styk.martin.apkanalyzer.ui.appdetail.page

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.android.material.snackbar.Snackbar
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.DialogComponent
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent

abstract class AppDetailPageFragmentViewModel constructor(
        private val appDetailFragmentViewModel: AppDetailFragmentViewModel,
        val adapter: DetailInfoDescriptionAdapter<*>,
        private val clipBoardManager: ClipBoardManager,
) : ViewModel(), DefaultLifecycleObserver {

    val openDescription = adapter.openDescription
            .map {
                DialogComponent(it.name, it.description, TextInfo.from(R.string.close))
            }

    val showSnackbar = adapter.copyToClipboard
            .map {
                clipBoardManager.copyToClipBoard(it.value, it.name)
                SnackBarComponent(TextInfo.from(R.string.copied_to_clipboard), Snackbar.LENGTH_SHORT)
            }


    private val appDetailsObserver = Observer<AppDetailData> { onDataReceived(it) }

    init {
        appDetailFragmentViewModel.appDetails.observeForever(appDetailsObserver)
    }

    override fun onCleared() {
        super.onCleared()
        appDetailFragmentViewModel.appDetails.removeObserver(appDetailsObserver)
    }

    abstract fun onDataReceived(appDetailData: AppDetailData)

}