package sk.styk.martin.apkanalyzer.core.designsystem.tv.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme

@Composable
fun CodeBox(
    text: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    maxLines: Int = Int.MAX_VALUE,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (label != null) {
            Text(text = label, style = AppTheme.typography.labelLarge, color = AppTheme.colors.onSurfaceVariant)
        }
        Text(
            text = text,
            style = AppTheme.typography.monospace,
            color = AppTheme.colors.onSurface,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppTheme.colors.surfaceVariant, shape = CodeShape)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        )
    }
}

private val CodeShape = RoundedCornerShape(10.dp)

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF161618)
@Composable
private fun CodeBoxPreview() {
    ApkAnalyzerTvTheme {
        CodeBox(
            label = "SHA-256",
            text = "3D:7A:12:23:01:9A:A3:9D:9E:A0:E3:43:6A:B7:C0:89:6B:FB:4F:B6:79:F4:DE:5F:E7:C2:3F:32:6C:8F:99:4A",
            modifier = Modifier
                .width(520.dp)
                .padding(24.dp),
        )
    }
}
