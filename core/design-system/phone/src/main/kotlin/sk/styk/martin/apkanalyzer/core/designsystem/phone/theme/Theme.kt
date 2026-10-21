package sk.styk.martin.apkanalyzer.core.designsystem.phone.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.DarkApkAnalyzerColors
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.LightApkAnalyzerColors
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.LocalApkAnalyzerColors
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.LocalApkAnalyzerTypography

@Composable
fun ApkAnalyzerTheme(isDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val apkAnalyzerColors = if (isDarkTheme) DarkApkAnalyzerColors else LightApkAnalyzerColors
    val materialColorScheme =
        remember(isDarkTheme) {
            if (isDarkTheme) darkColorScheme(apkAnalyzerColors) else lightColorScheme(apkAnalyzerColors)
        }

    CompositionLocalProvider(
        LocalApkAnalyzerColors provides apkAnalyzerColors,
        LocalApkAnalyzerTypography provides ApkAnalyzerTypography.toApkAnalyzerTypographyStyles(),
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = ApkAnalyzerTypography,
            content = content,
        )
    }
}
