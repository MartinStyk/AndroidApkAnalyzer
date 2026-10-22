package sk.styk.martin.apkanalyzer.core.designsystem.tv.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme

@Composable
fun SegmentedBar(segments: List<BarSegment>, modifier: Modifier = Modifier) {
    val visible = segments.filter { it.value > 0 }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(BarHeight)
            .clip(BarShape)
            .background(AppTheme.colors.surfaceVariant),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        visible.forEach { segment ->
            Box(
                modifier = Modifier
                    .weight(segment.value.toFloat())
                    .fillMaxHeight()
                    .background(segment.color),
            )
        }
    }
}

@Composable
fun SegmentedBarLegend(segments: List<BarSegment>, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        segments.filter { it.value > 0 }.forEach { segment ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(color = segment.color, shape = CircleShape),
                )
                Text(text = segment.label, style = AppTheme.typography.labelLarge, color = AppTheme.colors.onSurfaceVariant, maxLines = 1)
            }
        }
    }
}

private val BarHeight = 8.dp
private val BarShape = RoundedCornerShape(4.dp)

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF161618)
@Composable
private fun SegmentedBarPreview() {
    ApkAnalyzerTvTheme {
        SegmentedBar(
            segments = listOf(
                BarSegment(8, AppTheme.colors.warning),
                BarSegment(120, AppTheme.colors.primary),
                BarSegment(200, AppTheme.colors.onSurfaceVariant),
            ),
            modifier = Modifier
                .width(360.dp)
                .padding(24.dp),
        )
    }
}
