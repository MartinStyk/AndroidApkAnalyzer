package sk.styk.martin.apkanalyzer.core.designsystem.phone.modifier

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.phone.theme.Shapes as AppShape

@Composable
fun Modifier.card(shape: Shape = AppShape.CardShape): Modifier = this
    .clip(shape)
    .background(AppTheme.colors.surface, shape)
