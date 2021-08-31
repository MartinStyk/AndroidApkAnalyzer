package sk.styk.martin.apkanalyzer.manager.appanalysis

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.text.TextUtils
import sk.styk.martin.apkanalyzer.util.TAG_APP_ANALYSIS
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.StringWriter
import javax.inject.Inject
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

class AndroidManifestManager @Inject constructor(private val packageManager: PackageManager) {

    fun loadAndroidManifest(packageName: String, packagePath: String): String {
        val manifest = readManifest(packageManager, packageName, packagePath)
        return formatManifest(manifest)
    }

    private fun readManifest(packageManager: PackageManager, packageName: String, packagePath: String): String {
        val stringBuilder = StringBuilder()
        try {
            val apkResources = try {
                packageManager.getResourcesForApplication(packageName)
            } catch (exception: PackageManager.NameNotFoundException) {
                packageManager.getPackageArchiveInfo(packagePath, 0)?.let {
                    packageManager.getResourcesForApplication(it.applicationInfo)
                }
            }

            if (apkResources == null) {
                Timber.tag(TAG_APP_ANALYSIS).w("Resources for package $packageName not found")
                return ""
            }

            val parser = apkResources.assets.openXmlResourceParser("AndroidManifest.xml")

            var eventType: Int = parser.next()

            while (eventType != XmlResourceParser.END_DOCUMENT) {
                // start tag found
                if (eventType == XmlResourceParser.START_TAG) {
                    //start with opening element and writing its name
                    stringBuilder.append("<").append(parser.name)

                    //for each attribute in given element append attrName="attrValue"
                    for (attribute in 0 until parser.attributeCount) {
                        val attributeName = parser.getAttributeName(attribute)
                        val attributeValue = getAttributeValue(attributeName, parser.getAttributeValue(attribute), apkResources)

                        stringBuilder.append(" ").append(attributeName).append("=\"").append(attributeValue).append("\"")
                    }

                    stringBuilder.append(">")

                    if (parser.text != null) {
                        // if there is  body of xml element, add it there
                        stringBuilder.append(parser.text)
                    }
                } else if (eventType == XmlResourceParser.END_TAG) {
                    stringBuilder.append("</").append(parser.name).append(">")

                }

                eventType = parser.next()
            }
        } catch (e: Exception) {
            Timber.tag(TAG_APP_ANALYSIS).e(e, "Error parsing manifest for $packageName")
        }

        return stringBuilder.toString()
    }


    private fun formatManifest(manifest: String): String {
        return try {
            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
            val result = StreamResult(StringWriter())
            val source = StreamSource(ByteArrayInputStream(manifest.toByteArray(charset("UTF-8"))))
            transformer.transform(source, result)
            result.writer.toString()
        } catch (e: Exception) {
            Timber.tag(TAG_APP_ANALYSIS).e(e, "Error formatting manifest $manifest")
            ""
        }
    }

    private fun getAttributeValue(attributeName: String, attributeValue: String, resources: Resources): String {
        if (attributeValue.startsWith("@")) {
            try {
                val id = Integer.valueOf(attributeValue.substring(1))

                val value: String = when (attributeName) {
                    "theme", "resource" -> resources.getResourceEntryName(id)
                    else -> resources.getString(id)
                }

                return TextUtils.htmlEncode(value)
            } catch (e: Exception) {
                Timber.tag(TAG_APP_ANALYSIS).w(e, "Error reading attribute value $attributeName, $attributeValue")
            }

        }
        return attributeValue
    }

    companion object {

        /**
         * It is not possible to get minSdkVersions using Android PackageManager - parse AndroidManifest of app
         */
        fun getMinSdkVersion(applicationInfo: ApplicationInfo, packageManager: PackageManager): Int? {

            try {
                val apkResources = packageManager.getResourcesForApplication(applicationInfo)
                val parser = apkResources.assets.openXmlResourceParser("AndroidManifest.xml")
                var eventType = -1

                while (eventType != XmlResourceParser.END_DOCUMENT) {
                    if (eventType == XmlResourceParser.START_TAG) {
                        if ("uses-sdk" == parser.name)
                            return parser.getAttributeIntValue("http://schemas.android.com/apk/res/android", "minSdkVersion", 0)
                    }
                    eventType = parser.next()
                }

            } catch (e: Exception) {
                Timber.tag(TAG_APP_ANALYSIS).e(e, "Error reading minSdkValue for $applicationInfo")
            }

            return null
        }
    }
}
