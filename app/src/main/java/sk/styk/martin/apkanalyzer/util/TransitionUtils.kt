package sk.styk.martin.apkanalyzer.util

import android.content.Context
import android.graphics.Color
import com.google.android.material.transition.MaterialContainerTransform

fun Context.materialContainerTransform() = MaterialContainerTransform().apply {
    scrimColor = Color.TRANSPARENT
//    setAllContainerColors(ColorInfo.SURFACE.toColorInt(this@materialContainerTransform))
}
