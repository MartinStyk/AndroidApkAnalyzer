package sk.styk.martin.apkanalyzer.core.uilibrary

import android.content.Context
import android.os.Parcelable
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.getColorOrThrow

sealed class ColorInfo : Parcelable {

    companion object {

        @JvmField
        val TRANSPARENT = fromColor(android.R.color.transparent)

        @JvmField
        val PRIMARY = fromAttr(R.attr.colorPrimary)

        @JvmField
        val SECONDARY = fromAttr(R.attr.colorSecondary)

        @JvmField
        val SECONDARY_VARIANT = fromAttr(R.attr.colorSecondaryVariant)

        @JvmField
        val ON_SECONDARY = fromAttr(R.attr.colorOnSecondary)

        @JvmField
        val BACKGROUND = fromAttr(android.R.attr.colorBackground)

        @JvmField
        val TEXT_PRIMARY = fromAttr(android.R.attr.textColorPrimary)

        @JvmField
        val TEXT_SECONDARY = fromAttr(android.R.attr.textColorSecondary)

        @JvmField
        val TEXT_HINT = fromAttr(android.R.attr.textColorHint)

        @JvmField
        val SURFACE = fromAttr(R.attr.colorSurface)

        @JvmStatic
        fun fromColor(@ColorRes colorRes: Int): ColorInfo = ColorInfoRes(colorRes)

        @JvmStatic
        fun fromAttr(@AttrRes colorAttr: Int): ColorInfo = ColorInfoAttrRes(colorAttr)

        @JvmStatic
        fun fromColorInt(@ColorInt colorInt: Int): ColorInfo = ColorInfoInt(colorInt)
    }

    @ColorInt
    abstract fun toColorInt(context: Context): Int

    @kotlinx.parcelize.Parcelize
    private data class ColorInfoRes(@ColorRes private val colorRes: Int) : ColorInfo() {

        override fun toColorInt(context: Context): Int = ContextCompat.getColor(context, colorRes)
    }

    @kotlinx.parcelize.Parcelize
    private data class ColorInfoAttrRes(@AttrRes private val attrRes: Int) : ColorInfo() {

        override fun toColorInt(context: Context): Int {
            val attrs = intArrayOf(attrRes)
            val typedArray = context.obtainStyledAttributes(attrs)

            @ColorInt
            val color = typedArray.getColorOrThrow(0)
            typedArray.recycle()

            return color
        }
    }

    @kotlinx.parcelize.Parcelize
    private data class ColorInfoInt(@ColorInt private val colorInt: Int) : ColorInfo() {

        override fun toColorInt(context: Context): Int = colorInt
    }
}