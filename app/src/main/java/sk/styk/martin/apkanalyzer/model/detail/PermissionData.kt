package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.content.pm.PermissionInfo
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Single permission entry
 *
 * @author Martin Styk
 * @version 22.06.2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class PermissionData(val name: String,
                          val simpleName: String = Companion.createSimpleName(name),
                          val groupName: String? = null,
                          val protectionLevel: Int = PermissionInfo.PROTECTION_NORMAL) : Parcelable {


    companion object {
        private fun createSimpleName(name: String): String {
            var simpleNameBuilder = StringBuilder(name)

            val lastDot = name.lastIndexOf(".")
            if (lastDot > 0 && lastDot < name.length)
                simpleNameBuilder = StringBuilder(name.substring(lastDot + 1))

            var i = 0
            var previousSpace = false
            while (++i < simpleNameBuilder.length) {
                if (simpleNameBuilder[i] == '_') {
                    simpleNameBuilder.replace(i, i + 1, " ")
                    previousSpace = true
                } else {
                    if (!previousSpace) {
                        val lowercase = Character.toLowerCase(simpleNameBuilder[i])
                        simpleNameBuilder.replace(i, i + 1, lowercase.toString())
                    }
                    previousSpace = false
                }
            }
            return simpleNameBuilder.toString()
        }
    }
}