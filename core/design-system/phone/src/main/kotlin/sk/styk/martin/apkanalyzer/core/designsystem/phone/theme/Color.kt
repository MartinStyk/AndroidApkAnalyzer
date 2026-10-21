package sk.styk.martin.apkanalyzer.core.designsystem.phone.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.ApkAnalyzerColorPalette

internal fun lightColorScheme(colors: ApkAnalyzerColorPalette) = lightColorScheme(
    primary = colors.primary,
    onPrimary = colors.onPrimary,
    primaryContainer = colors.primaryContainer,
    onPrimaryContainer = colors.primary,
    secondary = colors.secondary,
    onSecondary = colors.onSecondary,
    secondaryContainer = colors.secondaryContainer,
    onSecondaryContainer = colors.onSecondaryContainer,
    tertiary = colors.tertiary,
    onTertiary = colors.onTertiary,
    tertiaryContainer = colors.tertiaryContainer,
    onTertiaryContainer = colors.onTertiaryContainer,
    background = colors.background,
    onBackground = colors.onBackground,
    surface = colors.surface,
    onSurface = colors.onSurface,
    surfaceVariant = colors.surfaceVariant,
    onSurfaceVariant = colors.onSurfaceVariant,
    error = colors.negative,
    onError = colors.surface,
    errorContainer = colors.negativeContainer,
    onErrorContainer = colors.negative,
    outline = colors.onSurfaceVariant,
)

internal fun darkColorScheme(colors: ApkAnalyzerColorPalette) = darkColorScheme(
    primary = colors.primary,
    onPrimary = colors.onPrimary,
    primaryContainer = colors.primaryContainer,
    onPrimaryContainer = colors.primary,
    secondary = colors.secondary,
    onSecondary = colors.onSecondary,
    secondaryContainer = colors.secondaryContainer,
    onSecondaryContainer = colors.onSecondaryContainer,
    tertiary = colors.tertiary,
    onTertiary = colors.onTertiary,
    tertiaryContainer = colors.tertiaryContainer,
    onTertiaryContainer = colors.onTertiaryContainer,
    background = colors.background,
    onBackground = colors.onBackground,
    surface = colors.surface,
    onSurface = colors.onSurface,
    surfaceVariant = colors.surfaceVariant,
    onSurfaceVariant = colors.onSurfaceVariant,
    error = colors.negative,
    onError = colors.surface,
    errorContainer = colors.negativeContainer,
    onErrorContainer = colors.negative,
    outline = colors.onSurfaceVariant,
)
