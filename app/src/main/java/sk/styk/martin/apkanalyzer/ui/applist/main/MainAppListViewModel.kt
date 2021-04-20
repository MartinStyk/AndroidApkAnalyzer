package sk.styk.martin.apkanalyzer.ui.applist.main

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.appanalysis.InstalledAppsManager
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.NavigationDrawerModel
import sk.styk.martin.apkanalyzer.manager.permission.PermissionManager
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.model.list.AppListData
import sk.styk.martin.apkanalyzer.ui.applist.*
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent

class MainAppListViewModel @AssistedInject constructor(
        private val permissionManager: PermissionManager,
        private val installedAppsManager: InstalledAppsManager,
        private val navigationDrawerModel: NavigationDrawerModel,
        private val dispatcherProvider: DispatcherProvider,
        adapter: AppListAdapter
) : BaseAppListViewModel(adapter), DefaultLifecycleObserver, SearchView.OnQueryTextListener, SearchView.OnCloseListener, Toolbar.OnMenuItemClickListener {

    override var appListData = listOf<AppListData>()
        set(value) {
            field = value
            adapter.data = value
            viewStateLiveData.value = when {
                allApps.isEmpty() -> LOADING_STATE
                value.isEmpty() -> EMPTY_STATE
                else -> DATA_STATE
            }
        }

    private var allApps: List<AppListData> = emptyList()

    private val openFilePickerEvent = SingleLiveEvent<Unit>()
    val openFilePicker: LiveData<Unit> = openFilePickerEvent

    private val showSnackEvent = SingleLiveEvent<SnackBarComponent>()
    val showSnack: LiveData<SnackBarComponent> = showSnackEvent

    private val indefiniteSnackbarEvent = MutableLiveData<SnackBarComponent?>()
    val indeterminateSnackbar: LiveData<SnackBarComponent?> = indefiniteSnackbarEvent

    private var queryTextLiveData = MutableLiveData<String>()
    var queryText: LiveData<String> = queryTextLiveData
    private var queryTextInternal: String = ""

    private val filteredSourceLiveData = MutableLiveData<AppSource?>()
    val filteredSource: LiveData<AppSource?> = filteredSourceLiveData

    private val openDetailFromFileEvent = SingleLiveEvent<Uri>()
    val openDetailFromFile: LiveData<Uri> = openDetailFromFileEvent

    val filePickerResult = ActivityResultCallback<ActivityResult> {
        if (it?.resultCode == Activity.RESULT_OK) {
            it.data?.data?.let { openDetailFromFileEvent.value = it }
        }
    }

    init {
        viewModelScope.launch(dispatcherProvider.default()) {
            val installedApps = installedAppsManager.getAll()
            allApps = installedApps
            withContext(dispatcherProvider.main()) {
                appListData = allApps
            }
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        queryTextLiveData.value = queryTextInternal
    }

    fun onFilePickerClick() {
        if (permissionManager.hasPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            openFilePickerEvent.call()
        } else {
            permissionManager.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, object : PermissionManager.PermissionCallback {
                override fun onPermissionDenied(permission: String) {
                    showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.permission_not_granted), Snackbar.LENGTH_LONG)
                }

                override fun onPermissionGranted(permission: String) {
                    openFilePickerEvent.call()
                }
            })
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        queryTextInternal = newText ?: ""
        setDataFiltered()
        return true
    }

    override fun onQueryTextSubmit(p0: String?) = true

    override fun onClose(): Boolean {
        queryTextLiveData.value = ""
        return false
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_analyze_not_installed -> onFilePickerClick()
            R.id.menu_show_all_apps -> {
                item.isChecked = true
                filteredSourceLiveData.value = null
                setDataFiltered()
            }
            R.id.menu_show_google_play_apps -> {
                item.isChecked = true
                filteredSourceLiveData.value = AppSource.GOOGLE_PLAY
                setDataFiltered()
            }
            R.id.menu_show_amazon_store_apps -> {
                item.isChecked = true
                filteredSourceLiveData.value = AppSource.AMAZON_STORE
                setDataFiltered()
            }
            R.id.menu_show_system_pre_installed_apps -> {
                item.isChecked = true
                filteredSourceLiveData.value = AppSource.SYSTEM_PREINSTALED
                setDataFiltered()
            }
            R.id.menu_show_unknown_source_apps -> {
                item.isChecked = true
                filteredSourceLiveData.value = AppSource.UNKNOWN
                setDataFiltered()
            }
        }
        return true
    }

    fun onNavigationClick() = viewModelScope.launch {
        navigationDrawerModel.openDrawer()
    }

    private fun setDataFiltered() {
        val nameQuery = queryTextInternal
        val source = filteredSource.value
        val hasFilters = !nameQuery.isNullOrBlank() || source != null
        appListData = if (!hasFilters) {
            allApps
        } else allApps.filter { entry ->
            fun textSearch(name: String) = nameQuery.isNullOrBlank() ||
                    name.startsWith(nameQuery, ignoreCase = true) ||
                    name.split(" ".toRegex()).any { it.startsWith(nameQuery, ignoreCase = true) } ||
                    name.split(".".toRegex()).any { it.startsWith(nameQuery, ignoreCase = true) }

            (source == null || entry.source == source) && (textSearch(entry.applicationName) || textSearch(entry.packageName))
        }

        indefiniteSnackbarEvent.value = if (hasFilters) SnackBarComponent(
                message = TextInfo.from(R.string.app_filtering_active),
                duration = Snackbar.LENGTH_INDEFINITE,
                action = TextInfo.from(R.string.clear),
                callback = {
                    filteredSourceLiveData.value = null
                    queryTextLiveData.value = ""
                    appListData = allApps
                }
        ) else null
    }

    @AssistedFactory
    interface Factory {
        fun create(): MainAppListViewModel
    }

}