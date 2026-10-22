package sk.styk.martin.apkanalyzer.core.designsystem.tv.components

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import kotlin.random.Random

@Composable
fun AccentBackdrop(
    accent: Color?,
    modifier: Modifier = Modifier,
    strength: Float = DEFAULT_STRENGTH,
    content: @Composable BoxScope.() -> Unit,
) {
    val glow by animateColorAsState(
        targetValue = accent?.copy(alpha = strength) ?: Color.Transparent,
        animationSpec = tween(durationMillis = FADE_DURATION),
        label = "accentGlow",
    )
    val grain = remember { ShaderBrush(ImageShader(grainTexture(), TileMode.Repeated, TileMode.Repeated)) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .drawBehind {
                if (glow.alpha == 0f) return@drawBehind
                drawRect(
                    Brush.radialGradient(
                        colors = List(GLOW_STOPS) { glow.copy(alpha = glow.alpha * falloff(it / (GLOW_STOPS - 1f))) },
                        center = Offset(size.width * CENTER_X, size.height * CENTER_Y),
                        radius = size.maxDimension * RADIUS,
                    ),
                )
                drawRect(grain)
            },
        content = content,
    )
}

private fun falloff(distance: Float): Float {
    val remaining = 1f - distance
    return remaining * remaining * (SMOOTHSTEP_A - SMOOTHSTEP_B * remaining)
}

private fun grainTexture(): ImageBitmap {
    val random = Random(GRAIN_SEED)
    val pixels = IntArray(GRAIN_SIZE * GRAIN_SIZE) {
        val shade = if (random.nextBoolean()) 0xFFFFFF else 0x000000
        (random.nextInt(GRAIN_MAX_ALPHA + 1) shl ALPHA_SHIFT) or shade
    }
    return Bitmap.createBitmap(pixels, GRAIN_SIZE, GRAIN_SIZE, Bitmap.Config.ARGB_8888).asImageBitmap()
}

private const val DEFAULT_STRENGTH = 0.32f
private const val GLOW_STOPS = 12
private const val CENTER_X = 0.12f
private const val CENTER_Y = 0.1f
private const val RADIUS = 0.85f
private const val SMOOTHSTEP_A = 3f
private const val SMOOTHSTEP_B = 2f
private const val GRAIN_SIZE = 128
private const val GRAIN_MAX_ALPHA = 6
private const val GRAIN_SEED = 7
private const val ALPHA_SHIFT = 24
private const val FADE_DURATION = 600

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun AccentBackdropPreview() {
    ApkAnalyzerTvTheme {
        AccentBackdrop(accent = Color(0xFFE53935)) {
            Text(text = "YouTube", style = AppTheme.typography.displaySmall)
        }
    }
}
