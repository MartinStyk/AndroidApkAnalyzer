package sk.styk.martin.apkanalyzer.feature.apps.phone.components.appitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import sk.styk.martin.apkanalyzer.core.apps.sorting.AppSortType
import sk.styk.martin.apkanalyzer.core.common.model.AppReference
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.icons.ApkAnalyzerIcons
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.icons.AppIcon
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.phone.components.Icon
import sk.styk.martin.apkanalyzer.core.designsystem.phone.components.Text
import sk.styk.martin.apkanalyzer.core.designsystem.phone.lazylist.ListItemPosition
import sk.styk.martin.apkanalyzer.core.designsystem.phone.theme.Shapes
import sk.styk.martin.apkanalyzer.feature.apps.phone.R
import sk.styk.martin.apkanalyzer.feature.apps.phone.list.AppListItem
import java.text.DateFormat
import java.time.Instant
import java.util.Date

@Composable
internal fun AppListItemRow(
    app: AppListItem,
    onClick: () -> Unit,
    position: ListItemPosition,
    modifier: Modifier = Modifier,
    sortType: AppSortType = AppSortType.Name,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    val shape: Shape = listItemShape(position)

    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .background(AppTheme.colors.surface, shape)
            .padding(2.dp)
            .clip(Shapes.CardShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppIcon(source = AppReference.InstalledPackage(app.packageName))

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = app.applicationName,
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = app.packageName.value,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (sortType != AppSortType.Name) {
            Spacer(modifier = Modifier.width(8.dp))
            SortValueBadge(app = app, sortType = sortType)
        }

        if (trailingContent != null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailingContent()
        }
    }
}

@Composable
private fun SortValueBadge(
    app: AppListItem,
    sortType: AppSortType,
    modifier: Modifier = Modifier,
) {
    val (icon, value) = when (sortType) {
        AppSortType.ApkSize -> ApkAnalyzerIcons.FileUpload to app.apkSize.formatted()
        AppSortType.TotalSize -> ApkAnalyzerIcons.Storage to (app.totalSize?.formatted() ?: "—")
        AppSortType.InstallDate -> ApkAnalyzerIcons.Calendar to app.installTime.toShortDate(stringResource(R.string.preinstalled))
        AppSortType.TargetSdk -> ApkAnalyzerIcons.Android to app.targetSdk.toString()
        AppSortType.LastUpdated -> ApkAnalyzerIcons.Calendar to app.lastUpdateTime.toShortDate(stringResource(R.string.preinstalled))
        AppSortType.LastUsed -> ApkAnalyzerIcons.Calendar to (app.lastUsedTime?.toShortDate(stringResource(R.string.never)) ?: stringResource(R.string.never))
        AppSortType.Name -> return
    }

    Row(
        modifier = modifier
            .background(AppTheme.colors.secondaryContainer, RoundedCornerShape(8.dp))
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Icon(
            imageVector = icon,
            modifier = Modifier.size(14.dp),
            tint = AppTheme.colors.onSecondaryContainer,
        )
        Text(
            text = value,
            style = AppTheme.typography.labelSmall,
            color = AppTheme.colors.onSecondaryContainer,
        )
    }
}

internal fun listItemShape(position: ListItemPosition): Shape = when (position) {
    ListItemPosition.Single -> Shapes.CardShape
    ListItemPosition.First -> RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ListItemPosition.Middle -> RectangleShape
    ListItemPosition.Last -> RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
}

@Composable
private fun Instant.toShortDate(ifEmpty: String): String = if (this < Instant.ofEpochMilli(631152000000L)) {
    ifEmpty
} else {
    DateFormat.getDateInstance(DateFormat.SHORT, LocalLocale.current.platformLocale).format(Date(this.toEpochMilli()))
}
