package sk.styk.martin.apkanalyzer.feature.appdetail.tv.components

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
internal data class InfoSection(
    val title: String,
    val rows: List<InfoRow>,
    val icon: ImageVector? = null,
)

@Immutable
internal data class InfoRow(
    val key: String,
    val label: String,
    val value: String,
    val explanation: String?,
    val highlighted: Boolean = false,
    val icon: ImageVector? = null,
    val status: String? = null,
    val isCode: Boolean = false,
)
