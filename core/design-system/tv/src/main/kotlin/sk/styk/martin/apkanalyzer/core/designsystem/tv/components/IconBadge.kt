package sk.styk.martin.apkanalyzer.core.designsystem.tv.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.icons.ApkAnalyzerIcons
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme

@Composable
fun IconBadge(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colors.primary,
    size: Dp = 36.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(color = tint.copy(alpha = BACKGROUND_ALPHA), shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(imageVector = icon, tint = tint, modifier = Modifier.size(size * ICON_FRACTION))
    }
}

private const val BACKGROUND_ALPHA = 0.16f
private const val ICON_FRACTION = 0.55f

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun IconBadgePreview() {
    ApkAnalyzerTvTheme {
        Row(modifier = Modifier.padding(48.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconBadge(icon = ApkAnalyzerIcons.Permissions)
            IconBadge(icon = ApkAnalyzerIcons.Warning, tint = AppTheme.colors.warning)
            IconBadge(icon = ApkAnalyzerIcons.Fingerprint, size = 56.dp)
        }
    }
}
