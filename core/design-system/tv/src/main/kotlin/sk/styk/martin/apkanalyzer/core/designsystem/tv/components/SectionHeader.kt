package sk.styk.martin.apkanalyzer.core.designsystem.tv.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme

@Composable
fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(text = text, style = AppTheme.typography.titleMedium, color = AppTheme.colors.primary, modifier = modifier)
}

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SectionHeaderPreview() {
    ApkAnalyzerTvTheme {
        SectionHeader(text = "Installed apps", modifier = Modifier.padding(48.dp))
    }
}
