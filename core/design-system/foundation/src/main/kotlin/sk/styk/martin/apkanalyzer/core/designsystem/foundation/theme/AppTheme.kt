package sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

@Immutable
data class ApkAnalyzerTypographyStyles(
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,
    val headlineLarge: TextStyle,
    val headlineMedium: TextStyle,
    val headlineSmall: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,
    val monospace: TextStyle,
)

val LocalApkAnalyzerColors = staticCompositionLocalOf<ApkAnalyzerColorPalette> { error(MISSING_THEME) }

val LocalApkAnalyzerTypography = staticCompositionLocalOf<ApkAnalyzerTypographyStyles> { error(MISSING_THEME) }

object AppTheme {
    val colors: ApkAnalyzerColorPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalApkAnalyzerColors.current

    val typography: ApkAnalyzerTypographyStyles
        @Composable
        @ReadOnlyComposable
        get() = LocalApkAnalyzerTypography.current
}

private const val MISSING_THEME = "AppTheme is read outside ApkAnalyzerTheme or ApkAnalyzerTvTheme"
