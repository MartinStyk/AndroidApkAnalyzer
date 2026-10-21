package sk.styk.martin.apkanalyzer.core.designsystem.tv.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.SurfaceDefaults
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import androidx.tv.material3.Surface as TvMaterialSurface

@Composable
fun ScreenSurface(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    TvMaterialSurface(
        modifier = modifier.fillMaxSize(),
        colors = SurfaceDefaults.colors(
            containerColor = AppTheme.colors.background,
            contentColor = AppTheme.colors.onBackground,
        ),
        content = content,
    )
}

@Preview(device = Devices.TV_1080p)
@Composable
private fun ScreenSurfacePreview() {
    ApkAnalyzerTvTheme {
        ScreenSurface {
            Text(text = "Installed apps", style = AppTheme.typography.headlineMedium, modifier = Modifier.padding(48.dp))
        }
    }
}
