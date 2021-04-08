package sk.styk.martin.apkanalyzer.manager.resources

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.palette.graphics.Palette
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ForApplication
import sk.styk.martin.apkanalyzer.util.ColorInfo
import sk.styk.martin.apkanalyzer.util.file.toBitmap
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ResourcesManager @Inject constructor(
        @ForApplication val context: Context,
        @ForApplication val resources: Resources,
) {

    fun getString(@StringRes stringRes: Int, vararg args: Any): CharSequence = context.getString(stringRes, *args)

    @ColorInt
    fun getColor(colorInfo: ColorInfo): Int = colorInfo.toColorInt(context)

    @ColorInt
    fun getColor(@ColorRes colorRes: Int): Int = ResourcesCompat.getColor(resources, colorRes, context.theme)

    @Dimension(unit = Dimension.DP)
    fun getDisplayHeight(): Float = resources.displayMetrics.run { heightPixels / density }

    suspend fun generatePalette(drawable: Drawable): Palette = suspendCoroutine {
        Palette.from(drawable.toBitmap()).generate { palette ->
            if (palette != null) it.resume(palette) else it.resumeWithException(IllegalStateException())
        }
    }
}