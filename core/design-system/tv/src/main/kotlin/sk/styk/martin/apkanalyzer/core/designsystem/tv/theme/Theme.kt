package sk.styk.martin.apkanalyzer.core.designsystem.tv.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.tv.material3.ColorScheme
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.ApkAnalyzerColorPalette
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.DarkApkAnalyzerColors
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.LocalApkAnalyzerColors
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.LocalApkAnalyzerTypography

@Composable
fun ApkAnalyzerTvTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalApkAnalyzerColors provides DarkApkAnalyzerColors,
        LocalApkAnalyzerTypography provides ApkAnalyzerTvTypography.toApkAnalyzerTypographyStyles(),
    ) {
        MaterialTheme(
            colorScheme = DarkApkAnalyzerColors.toTvColorScheme(),
            typography = ApkAnalyzerTvTypography,
        ) {
            CompositionLocalProvider(LocalContentColor provides DarkApkAnalyzerColors.onBackground, content = content)
        }
    }
}

private fun ApkAnalyzerColorPalette.toTvColorScheme(): ColorScheme = darkColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = primary,
    secondary = secondary,
    onSecondary = onSecondary,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    tertiary = tertiary,
    onTertiary = onTertiary,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = onSurfaceVariant,
    error = negative,
    onError = surface,
    errorContainer = negativeContainer,
    onErrorContainer = negative,
    border = onSurfaceVariant,
)
