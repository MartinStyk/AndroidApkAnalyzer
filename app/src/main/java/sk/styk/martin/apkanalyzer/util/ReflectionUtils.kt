package sk.styk.martin.apkanalyzer.util

import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

inline fun <reified T : Any, R> T.getPrivateProperty(name: String): R? = try {
    T::class.memberProperties
        .firstOrNull { it.name == name }
        ?.apply { isAccessible = true }
        ?.get(this) as? R
} catch (e: Exception) {
    throw ReflectiveOperationException("Error reading $name on $this", e)
}
