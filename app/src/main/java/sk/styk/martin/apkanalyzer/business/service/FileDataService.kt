package sk.styk.martin.apkanalyzer.business.service

import android.content.pm.PackageInfo
import android.util.Log
import sk.styk.martin.apkanalyzer.model.detail.FileData
import sk.styk.martin.apkanalyzer.model.detail.FileEntry
import java.io.IOException
import java.util.*
import java.util.jar.Attributes
import java.util.jar.JarFile
import java.util.jar.Manifest
import kotlin.collections.Map.Entry

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
class FileDataService {

    fun get(packageInfo: PackageInfo): FileData {

        val fileData = FileData()

        val mf = openManifest(packageInfo) ?: return fileData

        val drawables = ArrayList<FileEntry>()
        val layouts = ArrayList<FileEntry>()
        val menus = ArrayList<FileEntry>()
        val others = ArrayList<FileEntry>()

        var numberPngs = 0
        var numberXmls = 0
        val pngsSet = HashSet<String>()
        val xmlsSet = HashSet<String>()

        val map = mf.entries

        for (entry in map.entries) {
            val fileName = entry.key
            val hash = extractHash(entry)
            val fileEntry = FileEntry(fileName, hash)

            // sort into categories according to location
            when {
                fileName.startsWith("res/drawable") -> drawables.add(fileEntry)
                fileName.startsWith("res/layout") -> layouts.add(fileEntry)
                fileName.startsWith("res/menu") -> menus.add(fileEntry)
                "classes.dex".equals(fileName) -> fileData.dexHash = hash
                "resources.arsc".equals(fileName) -> fileData.arscHash = hash
                "AndroidManifest.xml".equals(fileName) -> fileData.manifestHash = hash
                else -> others.add(fileEntry)
            }

            // sort into categories according to file type
            when {
                fileName.endsWith(".png") -> {
                    numberPngs++
                    pngsSet.add(fileName)
                }
                fileName.endsWith(".xml") -> {
                    numberXmls++
                    xmlsSet.add(fileEntry.fileName)
                }
            }
        }

        fileData.drawableHashes = drawables
        fileData.layoutHashes = layouts
        fileData.menuHashes = menus
        fileData.otherHashes = others

        fileData.numberPngs = numberPngs
        fileData.numberPngsWithDifferentName = pngsSet.size
        fileData.numberXmls = numberXmls
        fileData.numberXmlsWithDifferentName = xmlsSet.size

        return fileData
    }

    /**
     * Gets all entries from application manifest file
     *
     * @param packageInfo APK packageInfo
     * @return all entries on manifest file or null, manifest is not found
     */
    private fun openManifest(packageInfo: PackageInfo): Manifest? {
        if (packageInfo.applicationInfo == null || packageInfo.applicationInfo.sourceDir == null)
            return null

        var jar: JarFile? = null
        try {
            jar = JarFile(packageInfo.applicationInfo.sourceDir)
            return jar.manifest
        } catch (e: IOException) {
            Log.e(FileDataService::class.java.simpleName, "Unable to find manifest", e)
        } finally {
            try {
                jar?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun extractHash(entry: Entry<String, Attributes>): String {
        return entry.value.getValue("SHA1-Digest") ?: entry.value.getValue("SHA-256-Digest")
    }
}
