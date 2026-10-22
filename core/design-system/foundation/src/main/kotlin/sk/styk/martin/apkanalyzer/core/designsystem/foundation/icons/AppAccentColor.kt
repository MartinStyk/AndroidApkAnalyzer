package sk.styk.martin.apkanalyzer.core.designsystem.foundation.icons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import sk.styk.martin.apkanalyzer.core.common.model.AppReference

@Composable
fun rememberAppAccentColor(source: AppReference): Color? {
    val context = LocalContext.current
    val accent by produceState<Color?>(initialValue = null, source) {
        val request = ImageRequest.Builder(context)
            .data(source)
            .size(PALETTE_SAMPLE_SIZE)
            .allowHardware(false)
            .build()
        val result = SingletonImageLoader.get(context).execute(request) as? SuccessResult ?: return@produceState
        val palette = Palette.from(result.image.toBitmap()).generate()
        val swatch = palette.vibrantSwatch ?: palette.lightVibrantSwatch ?: palette.darkVibrantSwatch ?: palette.dominantSwatch
        value = swatch?.hsl?.let { (hue, saturation, lightness) ->
            Color.hsl(
                hue = hue,
                saturation = saturation.coerceAtMost(MAX_SATURATION),
                lightness = lightness.coerceIn(MIN_LIGHTNESS, MAX_LIGHTNESS),
            )
        }
    }
    return accent
}

private const val PALETTE_SAMPLE_SIZE = 96
private const val MAX_SATURATION = 0.75f
private const val MIN_LIGHTNESS = 0.45f
private const val MAX_LIGHTNESS = 0.6f
