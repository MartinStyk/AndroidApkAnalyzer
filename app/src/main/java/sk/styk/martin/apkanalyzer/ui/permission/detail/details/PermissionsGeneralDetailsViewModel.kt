package sk.styk.martin.apkanalyzer.ui.permission.detail.details

import android.content.pm.PermissionInfo
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.android.material.snackbar.Snackbar
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.DialogComponent
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent

class PermissionsGeneralDetailsViewModel @AssistedInject constructor(
    @Assisted private val permissionDetailFragmentViewModel: PermissionDetailFragmentViewModel,
    val detailInfoAdapter: DetailInfoAdapter,
    val clipBoardManager: ClipBoardManager,
) : ViewModel(), DefaultLifecycleObserver {

    val openDescription = detailInfoAdapter.openDescription
        .map {
            DialogComponent(it.title, it.message, TextInfo.from(R.string.close))
        }

    val showSnackbar = detailInfoAdapter.copyToClipboard
        .map {
            clipBoardManager.copyToClipBoard(it.text, it.label)
            SnackBarComponent(TextInfo.from(R.string.copied_to_clipboard), Snackbar.LENGTH_SHORT)
        }

    init {
        val permissionData = permissionDetailFragmentViewModel.localPermissionData.permissionData

        detailInfoAdapter.info = listOfNotNull(
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.permissions_name),
                TextInfo.from(permissionData.name),
                TextInfo.from(R.string.permissions_name_description),
            ),
            permissionData.groupName?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.permissions_group),
                    TextInfo.from(it),
                    TextInfo.from(R.string.permissions_group_description),
                )
            },
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.permissions_protection),
                TextInfo.from(permissionLevelText(permissionData.protectionLevel)),
                TextInfo.from(R.string.permissions_protection_description),
            ),
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.permissions_granted_apps),
                TextInfo.from(permissionDetailFragmentViewModel.localPermissionData.grantedPackageNames.size.toString()),
                TextInfo.from(R.string.permissions_granted_apps_description),
            ),
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.permissions_not_granted_apps),
                TextInfo.from(permissionDetailFragmentViewModel.localPermissionData.notGrantedPackageNames.size.toString()),
                TextInfo.from(R.string.permissions_not_granted_apps_description),
            ),
        )
    }

    private fun permissionLevelText(level: Int) = when (level) {
        PermissionInfo.PROTECTION_NORMAL -> R.string.permissions_protection_normal
        PermissionInfo.PROTECTION_DANGEROUS -> R.string.permissions_protection_dangerous
        PermissionInfo.PROTECTION_SIGNATURE -> R.string.permissions_protection_signature
        else -> R.string.permissions_protection_normal
    }

    @AssistedFactory
    interface Factory {
        fun create(permissionDetailFragmentViewModel: PermissionDetailFragmentViewModel): PermissionsGeneralDetailsViewModel
    }
}
