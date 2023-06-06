package sk.styk.martin.apkanalyzer.ui.permission.detail.pager

import android.content.pm.PackageManager
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.core.apppermissions.model.LocalPermissionData
import sk.styk.martin.apkanalyzer.util.TAG_APP_ANALYSIS
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.DialogComponent
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import timber.log.Timber

class PermissionDetailFragmentViewModel @AssistedInject constructor(
    @Assisted val localPermissionData: LocalPermissionData,
    private val packageManager: PackageManager,
) : ViewModel(), Toolbar.OnMenuItemClickListener {

    val title = localPermissionData.permissionData.simpleName

    private val showDialogEvent = SingleLiveEvent<DialogComponent>()
    val showDialog: LiveData<DialogComponent> = showDialogEvent

    private val closeEvent = SingleLiveEvent<Unit>()
    val close: LiveData<Unit> = closeEvent

    fun onNavigationClick() = closeEvent.call()

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.description -> showDescriptionDialog()
        }
        return true
    }

    private fun showDescriptionDialog() {
        val description = try {
            packageManager.getPermissionInfo(localPermissionData.permissionData.name, PackageManager.GET_META_DATA).loadDescription(packageManager)
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.tag(TAG_APP_ANALYSIS).i("No description for permission ${localPermissionData.permissionData.name}")
            null
        }?.takeIf { it.isNotBlank() }
            ?.let { TextInfo.from(it) }
            ?: TextInfo.from(R.string.NA)

        showDialogEvent.value = DialogComponent(
            title = TextInfo.from(localPermissionData.permissionData.simpleName),
            message = description,
            negativeButtonText = TextInfo.from(R.string.dismiss),
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(localPermissionData: LocalPermissionData): PermissionDetailFragmentViewModel
    }
}
