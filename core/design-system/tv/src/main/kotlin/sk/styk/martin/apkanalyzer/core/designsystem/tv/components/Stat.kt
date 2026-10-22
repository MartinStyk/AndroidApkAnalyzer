package sk.styk.martin.apkanalyzer.core.designsystem.tv.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme

@Composable
fun Stat(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    valueColor: Color = AppTheme.colors.onSurface,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(text = value, style = AppTheme.typography.headlineSmall, color = valueColor, maxLines = 1)
        Text(
            text = label,
            style = AppTheme.typography.labelLarge,
            color = AppTheme.colors.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF161618)
@Composable
private fun StatPreview() {
    ApkAnalyzerTvTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.padding(24.dp)) {
            Stat(value = "244", label = "Activities")
            Stat(value = "29/29", label = "Sensitive granted", valueColor = AppTheme.colors.warning)
        }
    }
}
