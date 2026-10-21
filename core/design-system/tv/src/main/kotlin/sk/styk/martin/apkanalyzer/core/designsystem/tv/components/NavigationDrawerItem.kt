package sk.styk.martin.apkanalyzer.core.designsystem.tv.components

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey

@Immutable
data class NavigationDrawerItem(
    val navKey: NavKey,
    val icon: ImageVector,
    val title: String,
)
