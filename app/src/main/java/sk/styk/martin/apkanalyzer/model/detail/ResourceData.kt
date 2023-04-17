package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ResourceData(
    // types of drawables
    var pngDrawables: Int = 0,
    var ninePatchDrawables: Int = 0,
    var jpgDrawables: Int = 0,
    var gifDrawables: Int = 0,
    var xmlDrawables: Int = 0,
    var drawables: Int = 0,
    var differentDrawables: Int = 0, // number of drawables with different name (i.e. /res/hdpi/a.png == /res/xhdpi/a.png)
    // drawables according to dimensions
    var ldpiDrawables: Int = 0,
    var mdpiDrawables: Int = 0,
    var hdpiDrawables: Int = 0,
    var xhdpiDrawables: Int = 0,
    var xxhdpiDrawables: Int = 0,
    var xxxhdpiDrawables: Int = 0,
    var nodpiDrawables: Int = 0,
    var tvdpiDrawables: Int = 0,
    var unspecifiedDpiDrawables: Int = 0,
    // layouts
    var layouts: Int = 0,
    var differentLayouts: Int = 0, // number of layouts with different name (i.e. /res/layout/a.xml == /res/layout-land/a.xml))
) : Parcelable
