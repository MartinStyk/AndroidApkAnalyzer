package sk.styk.martin.apkanalyzer.manager.appanalysis

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.inject.Inject

class DigestManager @Inject constructor(){

    fun md5Digest(input: ByteArray): String {
        val digest = getDigest("Md5")
        digest.update(input)
        return getHexString(digest.digest())
    }

    fun md5Digest(input: String): String {
        val digest = getDigest("Md5")
        digest.update(input.toByteArray())
        return getHexString(digest.digest())
    }

    fun byteToHexString(bArray: ByteArray): String {
        val sb = StringBuilder(bArray.size)
        val var5 = bArray.size

        for (var6 in 0 until var5) {
            val aBArray = bArray[var6]
            val sTemp = Integer.toHexString(255 and aBArray.toInt().toChar().code)
            if (sTemp.length < 2) {
                sb.append(0)
            }

            sb.append(sTemp.uppercase(Locale.getDefault()))
        }

        return sb.toString()
    }

    private fun getHexString(digest: ByteArray): String {
        val bi = BigInteger(1, digest)
        return String.format("%032x", bi)
    }

    private fun getDigest(algorithm: String): MessageDigest {
        try {
            return MessageDigest.getInstance(algorithm)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e.message, e)
        }

    }

}
