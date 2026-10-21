package sk.styk.martin.apkanalyzer.feature.apps.phone.components.quickfilter

sealed interface QuickFilterRowEvent {
    data object NavigateToFilter : QuickFilterRowEvent
    data object OpenUsageAccessSettings : QuickFilterRowEvent
}
