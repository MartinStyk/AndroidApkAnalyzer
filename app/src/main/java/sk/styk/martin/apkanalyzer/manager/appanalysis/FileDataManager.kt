package sk.styk.martin.apkanalyzer.manager.appanalysis

import android.content.pm.PackageInfo
import sk.styk.martin.apkanalyzer.model.detail.FileData
import sk.styk.martin.apkanalyzer.model.detail.FileEntry
import sk.styk.martin.apkanalyzer.util.TAG_APP_ANALYSIS
import timber.log.Timber
import java.io.IOException
import java.util.*
import java.util.jar.Attributes
import java.util.jar.JarFile
import java.util.jar.Manifest
import javax.inject.Inject
import kotlin.collections.Map.Entry

class FileDataManager @Inject constructor() {

    fun get(packageInfo: PackageInfo): FileData {

        val mf = openManifest(packageInfo) ?: return FileData()

        val drawables = ArrayList<FileEntry>()
        val layouts = ArrayList<FileEntry>()
        val menus = ArrayList<FileEntry>()
        val others = ArrayList<FileEntry>()

        var numberPngs = 0
        var numberXmls = 0
        val pngsSet = HashSet<String>()
        val xmlsSet = HashSet<String>()

        val map = mf.entries

        var dexHash: String? = null
        var arscHash: String? = null
        var manifestHash: String? = null

        for (entry in map.entries) {
            val fileName = entry.key
            val hash = extractHash(entry)
            val fileEntry = FileEntry(fileName, hash)

            // sort into categories according to location
            when {
                fileName.startsWith("res/drawable") -> drawables.add(fileEntry)
                fileName.startsWith("res/layout") -> layouts.add(fileEntry)
                fileName.startsWith("res/menu") -> menus.add(fileEntry)
                "classes.dex".equals(fileName) -> dexHash = hash
                "resources.arsc".equals(fileName) -> arscHash = hash
                "AndroidManifest.xml".equals(fileName) -> manifestHash = hash
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

        return FileData(
                dexHash = dexHash ?: "",
                arscHash = arscHash ?: "",
                manifestHash = manifestHash ?: "",
                drawableHashes = drawables,
                layoutHashes = layouts,
                menuHashes = menus,
                otherHashes = others,
                numberPngs = numberPngs,
                numberPngsWithDifferentName = pngsSet.size,
                numberXmls = numberXmls,
                numberXmlsWithDifferentName = xmlsSet.size
        )
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
            Timber.tag(TAG_APP_ANALYSIS).e(e, "Unable to find manifest for $packageInfo.")
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
