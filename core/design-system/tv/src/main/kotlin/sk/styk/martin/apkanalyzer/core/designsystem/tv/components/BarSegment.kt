package sk.styk.martin.apkanalyzer.core.designsystem.tv.components

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class BarSegment(
    val value: Int,
    val color: Color,
    val label: String = "",
)
