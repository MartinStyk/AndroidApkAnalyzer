package sk.styk.martin.apkanalyzer.feature.apps.impl.components.quickfilter

import sk.styk.martin.apkanalyzer.core.apps.filtering.ActivityQuickFilter
import sk.styk.martin.apkanalyzer.core.apps.filtering.QuickFilter
import sk.styk.martin.apkanalyzer.core.apps.filtering.SourceQuickFilter
import sk.styk.martin.apkanalyzer.feature.apps.impl.components.AppDataPermission

sealed interface QuickFilterRowAction {
    data class QuickFilterToggle(val filter: QuickFilter) : QuickFilterRowAction
    data class SourceQuickFilterToggled(val filter: SourceQuickFilter) : QuickFilterRowAction
    data class ActivityQuickFilterSelected(val filter: ActivityQuickFilter?) : QuickFilterRowAction
    data object FilterClick : QuickFilterRowAction
    data object DismissPermissionRationale : QuickFilterRowAction
    data class OpenPermissionSettings(val permission: AppDataPermission) : QuickFilterRowAction
}
