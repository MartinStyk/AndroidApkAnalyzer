package sk.styk.martin.apkanalyzer.util

import android.content.Context

fun Int.DP(context: Context): Float = (this * context.resources.displayMetrics.density)

fun Float.DP(context: Context): Float = (this * context.resources.displayMetrics.density)

object DisplayHelper {

    fun displayHeightDp(context: Context) = context.resources.displayMetrics.heightPixels / context.resources.displayMetrics.density

}