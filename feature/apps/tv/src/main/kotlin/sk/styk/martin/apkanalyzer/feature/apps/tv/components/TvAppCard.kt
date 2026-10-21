package sk.styk.martin.apkanalyzer.feature.apps.tv.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.styk.martin.apkanalyzer.core.common.model.AppReference
import sk.styk.martin.apkanalyzer.core.common.model.PackageName
import sk.styk.martin.apkanalyzer.core.common.model.megabytes
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.icons.AppIcon
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.WideCard
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import java.time.Instant

internal val TvAppCardWidth = 280.dp

@Composable
internal fun TvAppCard(
    app: TvAppItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    WideCard(
        onClick = onClick,
        image = {
            AppIcon(
                source = AppReference.InstalledPackage(app.packageName),
                size = 48.dp,
                modifier = Modifier.padding(end = 16.dp),
            )
        },
        title = app.applicationName,
        subtitle = app.packageName.value,
        description = description,
        modifier = modifier.fillMaxWidth(),
    )
}

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TvAppCardPreview() {
    ApkAnalyzerTvTheme {
        TvAppCard(
            app = TvAppItem(
                packageName = PackageName("com.google.android.youtube.tv"),
                applicationName = "YouTube",
                targetSdk = 34,
                apkSize = 142.megabytes,
                totalSize = 380.megabytes,
                installTime = Instant.parse("2025-03-02T10:00:00Z"),
                lastUpdateTime = Instant.parse("2026-09-14T10:00:00Z"),
                lastUsedTime = null,
            ),
            onClick = {},
            description = "142 MB",
            modifier = Modifier.width(TvAppCardWidth),
        )
    }
}
