package sk.styk.martin.apkanalyzer.feature.settings.phone

import sk.styk.martin.apkanalyzer.core.common.settings.ColorAppScheme

sealed interface SettingsAction {
    data class ColorSchemeSelected(val colorScheme: ColorAppScheme) : SettingsAction
    data class RecentlyViewedAppsToggled(val enabled: Boolean) : SettingsAction
    data object OpenLanguageSettings : SettingsAction
    data object NavigateBack : SettingsAction
}
