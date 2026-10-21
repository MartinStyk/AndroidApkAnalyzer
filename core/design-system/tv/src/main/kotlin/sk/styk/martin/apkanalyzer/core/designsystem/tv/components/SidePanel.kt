package sk.styk.martin.apkanalyzer.core.designsystem.tv.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.tv.material3.SurfaceDefaults
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import androidx.tv.material3.Surface as TvMaterialSurface

@Composable
fun SidePanel(
    title: String,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
            SidePanelContent(title = title, content = content)
        }
    }
}

@Composable
private fun SidePanelContent(title: String, content: @Composable ColumnScope.() -> Unit) {
    TvMaterialSurface(
        modifier = Modifier
            .width(PanelWidth)
            .fillMaxHeight(),
        colors = SurfaceDefaults.colors(
            containerColor = AppTheme.colors.surface,
            contentColor = AppTheme.colors.onSurface,
        ),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 27.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = title, style = AppTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 8.dp))
            content()
        }
    }
}

private val PanelWidth = 400.dp

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SidePanelContentPreview() {
    ApkAnalyzerTvTheme {
        SidePanelContent(title = "Sort by") {
            SelectableListItem(selected = true, onClick = {}, label = "Name")
            SelectableListItem(selected = false, onClick = {}, label = "Install date")
        }
    }
}
