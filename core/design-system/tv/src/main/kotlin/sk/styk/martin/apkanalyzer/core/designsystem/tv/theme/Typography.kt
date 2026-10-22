package sk.styk.martin.apkanalyzer.core.designsystem.tv.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Typography
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.ApkAnalyzerTypographyStyles
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.Text

internal val ApkAnalyzerTvTypography =
    Typography(
        displayLarge = tvStyle(FontWeight.Normal, size = 57f, lineHeight = 64f, letterSpacing = -0.25f),
        displayMedium = tvStyle(FontWeight.Normal, size = 45f, lineHeight = 52f, letterSpacing = 0f),
        displaySmall = tvStyle(FontWeight.Normal, size = 36f, lineHeight = 44f, letterSpacing = 0f),
        headlineLarge = tvStyle(FontWeight.Normal, size = 32f, lineHeight = 40f, letterSpacing = 0f),
        headlineMedium = tvStyle(FontWeight.Normal, size = 28f, lineHeight = 36f, letterSpacing = 0f),
        headlineSmall = tvStyle(FontWeight.Normal, size = 24f, lineHeight = 32f, letterSpacing = 0f),
        titleLarge = tvStyle(FontWeight.Medium, size = 22f, lineHeight = 28f, letterSpacing = 0f),
        titleMedium = tvStyle(FontWeight.Medium, size = 16f, lineHeight = 24f, letterSpacing = 0.15f),
        titleSmall = tvStyle(FontWeight.Medium, size = 14f, lineHeight = 20f, letterSpacing = 0.1f),
        bodyLarge = tvStyle(FontWeight.Normal, size = 16f, lineHeight = 24f, letterSpacing = 0.5f),
        bodyMedium = tvStyle(FontWeight.Normal, size = 14f, lineHeight = 20f, letterSpacing = 0.25f),
        bodySmall = tvStyle(FontWeight.Normal, size = 12f, lineHeight = 16f, letterSpacing = 0.4f),
        labelLarge = tvStyle(FontWeight.Medium, size = 14f, lineHeight = 20f, letterSpacing = 0.1f),
        labelMedium = tvStyle(FontWeight.Medium, size = 12f, lineHeight = 16f, letterSpacing = 0.5f),
        labelSmall = tvStyle(FontWeight.Medium, size = 11f, lineHeight = 16f, letterSpacing = 0f),
    )

private fun tvStyle(
    weight: FontWeight,
    size: Float,
    lineHeight: Float,
    letterSpacing: Float,
) = TextStyle(
    fontWeight = weight,
    fontSize = (size * TV_TYPE_SCALE).sp,
    lineHeight = (lineHeight * TV_TYPE_SCALE).sp,
    letterSpacing = letterSpacing.sp,
)

private const val TV_TYPE_SCALE = 0.875f

internal fun Typography.toApkAnalyzerTypographyStyles() = ApkAnalyzerTypographyStyles(
    displayLarge = displayLarge,
    displayMedium = displayMedium,
    displaySmall = displaySmall,
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall,
    monospace = bodyMedium.copy(fontFamily = FontFamily.Monospace, letterSpacing = 0.sp),
)

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TvTypographyPreview() {
    ApkAnalyzerTvTheme {
        Column(modifier = Modifier.padding(48.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "Installed apps", style = AppTheme.typography.displayMedium)
            Text(text = "Permissions", style = AppTheme.typography.headlineMedium)
            Text(text = "Camera", style = AppTheme.typography.titleLarge)
            Text(text = "Allowed while the app is in use", style = AppTheme.typography.titleMedium)
            Text(text = "This app can take pictures and record video.", style = AppTheme.typography.bodyLarge)
            Text(text = "Target SDK 35 · Android 15", style = AppTheme.typography.bodyMedium)
            Text(text = "Open app info", style = AppTheme.typography.labelLarge)
            Text(text = "3A:F1:9C:42:07:BE:5D:10", style = AppTheme.typography.monospace)
        }
    }
}
