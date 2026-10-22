package sk.styk.martin.apkanalyzer.core.designsystem.tv.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sk.styk.martin.apkanalyzer.core.common.model.AppReference
import sk.styk.martin.apkanalyzer.core.common.model.PackageName
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.icons.AppIcon
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme

val AppCardWidth: Dp = 280.dp

@Composable
fun AppCard(
    packageName: PackageName,
    applicationName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    WideCard(
        onClick = onClick,
        image = {
            AppIcon(
                source = AppReference.InstalledPackage(packageName),
                size = 48.dp,
                modifier = Modifier.padding(end = 16.dp),
            )
        },
        title = applicationName,
        subtitle = packageName.value,
        description = description,
        modifier = modifier.fillMaxWidth(),
    )
}

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun AppCardPreview() {
    ApkAnalyzerTvTheme {
        AppCard(
            packageName = PackageName("com.google.android.youtube.tv"),
            applicationName = "YouTube",
            onClick = {},
            description = "142 MB",
            modifier = Modifier.width(AppCardWidth),
        )
    }
}
