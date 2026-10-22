package sk.styk.martin.apkanalyzer.feature.appdetail.tv.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.Text
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme

@Composable
internal fun PaneField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    explanation: String? = null,
    valueColor: Color = AppTheme.colors.onSurface,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = label, style = AppTheme.typography.labelLarge, color = AppTheme.colors.onSurfaceVariant)
        Text(text = value, style = AppTheme.typography.titleMedium, color = valueColor)
        if (explanation != null) {
            Text(text = explanation, style = AppTheme.typography.bodyMedium, color = AppTheme.colors.onSurfaceVariant)
        }
    }
}

@Composable
internal fun PaneSection(
    title: String,
    modifier: Modifier = Modifier,
    explanation: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = title, style = AppTheme.typography.titleSmall, color = AppTheme.colors.primary)
        if (explanation != null) {
            Text(text = explanation, style = AppTheme.typography.bodyMedium, color = AppTheme.colors.onSurfaceVariant)
        }
        content()
    }
}

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF161618)
@Composable
private fun PaneFieldPreview() {
    ApkAnalyzerTvTheme {
        PaneSection(title = "Reachability", modifier = Modifier.padding(24.dp)) {
            PaneField(
                label = "Protection level",
                value = "Dangerous",
                explanation = "Gives access to private data or device features. You decide whether to allow it.",
            )
        }
    }
}
