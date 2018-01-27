package sk.styk.martin.apkanalyzer.business.analysis.logic

import sk.styk.martin.apkanalyzer.model.detail.FileData
import sk.styk.martin.apkanalyzer.model.detail.ResourceData
import java.util.*

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */

class ResourceService {

    fun get(fileData: FileData): ResourceData {

        var numJpg = 0
        var numGif = 0
        var numPng = 0
        var numXml = 0
        var numNinePatchPng = 0

        var unspecifiedDpi = 0
        var ldpi = 0
        var mdpi = 0
        var hdpi = 0
        var xhdpi = 0
        var xxhdpi = 0
        var xxxhdpi = 0
        var nodpi = 0
        var tvdpi = 0

        val differentDrawables = HashSet<String>()
        val differentLayouts = HashSet<String>()

        fileData.drawableHashes.forEach {
            val name = it.path
            // eliminate duplicities
            differentDrawables.add(it.fileName)

            when {
                name.endsWith(".jpg") -> numJpg++
                name.endsWith(".gif") -> numGif++
                name.endsWith(".9.png") -> numNinePatchPng++
                name.endsWith(".png") -> numPng++
                name.endsWith(".xml") -> numXml++
            }

            when {
                name.contains("ldpi") -> ldpi++
                name.contains("mdpi") -> mdpi++
                name.contains("xxxhdpi") -> xxxhdpi++
                name.contains("xxhdpi") -> xxhdpi++
                name.contains("xhdpi") -> xhdpi++
                name.contains("hdpi") -> hdpi++
                name.contains("nodpi") -> nodpi++
                name.contains("tvdpi") -> tvdpi++
                else -> unspecifiedDpi++
            }
        }

        // just adding it to set to eliminate duplicities
        fileData.layoutHashes.forEach { differentLayouts.add(it.fileName) }

        return ResourceData(
                pngDrawables = numPng,
                ninePatchDrawables = numNinePatchPng,
                jpgDrawables = numJpg,
                gifDrawables = numGif,
                xmlDrawables = numXml,
                drawables = fileData.drawableHashes.size,
                differentDrawables = differentDrawables.size,
                ldpiDrawables = ldpi,
                mdpiDrawables = mdpi,
                hdpiDrawables = hdpi,
                xhdpiDrawables = xhdpi,
                xxhdpiDrawables = xxhdpi,
                xxxhdpiDrawables = xxxhdpi,
                nodpiDrawables = nodpi,
                tvdpiDrawables = tvdpi,
                unspecifiedDpiDrawables = unspecifiedDpi,
                layouts = fileData.layoutHashes.size,
                differentLayouts = differentLayouts.size)
    }
}
