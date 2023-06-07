package sk.styk.martin.apkanalyzer.ui.appdetail.page.definedpermission

import android.content.pm.PackageManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.core.appanalysis.AppPermissionManager
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.usedpermission.AppPermissionListAdapter
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.DialogComponent
import sk.styk.martin.apkanalyzer.core.common.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent

class AppDefinedPermissionFragmentViewModel @AssistedInject constructor(
    @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
    val permissionAdapter: AppPermissionListAdapter,
    clipBoardManager: ClipBoardManager,
    private val packageManager: PackageManager,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, permissionAdapter, clipBoardManager) {

    private val showDialogEvent = SingleLiveEvent<DialogComponent>()
    val showDialog = SingleLiveEvent<DialogComponent>()

    override fun onDataReceived(appDetailData: AppDetailData): Boolean {
        permissionAdapter.items = appDetailData.permissionData.definesPermissions.map {
            AppPermissionListAdapter.DecomposedPermissionData(it.name, it.simpleName)
        }
        return permissionAdapter.items.isNotEmpty()
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        permissionAdapter.showPermissionDetail.observe(owner) { it ->
            val description = try {
                packageManager.getPermissionInfo(it.completeName, PackageManager.GET_META_DATA).loadDescription(packageManager)
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }?.takeIf { it.isNotBlank() }
                ?.let { TextInfo.from(it) }
                ?: TextInfo.from(R.string.NA)

            showDialogEvent.value = DialogComponent(
                title = TextInfo.from(it.simpleName),
                message = description,
                negativeButtonText = TextInfo.from(R.string.dismiss),
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppDefinedPermissionFragmentViewModel
    }
}
