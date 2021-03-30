package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.service

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.android.material.snackbar.Snackbar
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.base.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.DialogComponent
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent

class AppServiceDetailFragmentViewModel @AssistedInject constructor(
        @Assisted private val appDetailFragmentViewModel: AppDetailFragmentViewModel,
        val adapter: AppServiceDetailListAdapter,
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

    private val appDetailsObserver = Observer<AppDetailData> {
        adapter.items = it.serviceData
    }

    init {
        appDetailFragmentViewModel.appDetails.observeForever(appDetailsObserver)
    }

    override fun onCleared() {
        super.onCleared()
        appDetailFragmentViewModel.appDetails.removeObserver(appDetailsObserver)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppServiceDetailFragmentViewModel
    }
}