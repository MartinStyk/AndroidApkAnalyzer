package sk.styk.martin.apkanalyzer.core.designsystem.tv.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import sk.styk.martin.apkanalyzer.core.common.model.AppReference
import sk.styk.martin.apkanalyzer.core.common.model.PackageName
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.icons.AppIcon
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import androidx.tv.material3.WideClassicCard as TvMaterialWideClassicCard

@Composable
fun WideCard(
    onClick: () -> Unit,
    image: @Composable BoxScope.() -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    description: String? = null,
) {
    TvMaterialWideClassicCard(
        onClick = onClick,
        image = image,
        title = {
            Text(
                text = title,
                style = AppTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        subtitle = {
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        description = {
            if (description != null) {
                Text(
                    text = description,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        modifier = modifier,
        shape = CardDefaults.shape(shape = CardShape),
        colors = CardDefaults.colors(
            containerColor = AppTheme.colors.surface,
            contentColor = AppTheme.colors.onSurface,
            focusedContainerColor = AppTheme.colors.surfaceVariant,
            focusedContentColor = AppTheme.colors.onSurface,
        ),
        border = CardDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(width = 2.dp, color = AppTheme.colors.primary),
                shape = CardShape,
            ),
        ),
        scale = CardDefaults.scale(focusedScale = 1.05f),
        contentPadding = PaddingValues(16.dp),
    )
}

private val CardShape = RoundedCornerShape(12.dp)

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun WideCardPreview() {
    ApkAnalyzerTvTheme {
        WideCard(
            onClick = {},
            image = { AppIcon(source = AppReference.InstalledPackage(PackageName("com.netflix.ninja")), modifier = Modifier.size(48.dp)) },
            title = "Netflix",
            subtitle = "com.netflix.ninja",
            modifier = Modifier.width(320.dp),
        )
    }
}
