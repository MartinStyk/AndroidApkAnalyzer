package sk.styk.martin.apkanalyzer.feature.appdetail.tv.appcomponents

import sk.styk.martin.apkanalyzer.core.apps.components.ComponentKind

internal sealed interface TvComponentsAction {
    data object Retry : TvComponentsAction
    data class SelectScope(val scope: TvComponentScope) : TvComponentsAction
    data class LaunchComponent(val className: String, val kind: ComponentKind) : TvComponentsAction
}
