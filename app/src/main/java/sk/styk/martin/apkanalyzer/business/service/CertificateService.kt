package sk.styk.martin.apkanalyzer.business.service

import android.content.pm.PackageInfo
import sk.styk.martin.apkanalyzer.model.detail.CertificateData
import sk.styk.martin.apkanalyzer.util.DigestHelper
import java.io.ByteArrayInputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.regex.Pattern
import javax.security.auth.x500.X500Principal
import javax.security.auth.x500.X500Principal.RFC1779

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */

class CertificateService {

    fun get(packageInfo: PackageInfo): CertificateData {

        val data = CertificateData()

        val signature = packageInfo.signatures[0] ?: return data

        return ByteArrayInputStream(signature.toByteArray()).use {
            val certFactory = CertificateFactory.getInstance("X509")
            val certificate = certFactory.generateCertificate(it) as X509Certificate

            data.certificateHash = DigestHelper.md5Digest(certificate.encoded)
            data.publicKeyMd5 = DigestHelper.md5Digest(DigestHelper.byteToHexString(certificate.publicKey.encoded))

            data.startDate = certificate.notBefore
            data.endDate = certificate.notAfter
            data.signAlgorithm = certificate.sigAlgName
            data.serialNumber = certificate.serialNumber.toInt()

            data.issuerName = certificate.issuerX500Principal?.getPrincipalCommonName()
            data.issuerOrganization = certificate.issuerX500Principal?.getPrincipalOrganization()
            data.issuerCountry = certificate.issuerX500Principal?.getPrincipalCountry()

            data.subjectName = certificate.subjectX500Principal?.getPrincipalCommonName()
            data.subjectOrganization = certificate.subjectX500Principal?.getPrincipalOrganization()
            data.subjectCountry = certificate.subjectX500Principal?.getPrincipalCountry()

            data
        }
    }

    fun getSignAlgorithm(packageInfo: PackageInfo): String? {
        val signature = packageInfo.signatures[0]

        ByteArrayInputStream(signature.toByteArray()).use {

            try {
                val certFactory = CertificateFactory.getInstance("X509")
                val certificate = certFactory.generateCertificate(it) as X509Certificate
                return certificate.sigAlgName
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null;
    }

    private fun X500Principal.getPrincipalCommonName(): String? {
        val name: String? = getName(RFC1779)
        return if (name.isNullOrBlank()) null else parsePrincipal(name!!, "CN=([^,]*)")
    }

    private fun X500Principal.getPrincipalOrganization(): String? {
        val name: String? = getName(RFC1779)
        return if (name.isNullOrBlank()) null else parsePrincipal(name!!, "O=([^,]*)")
    }

    private fun X500Principal.getPrincipalCountry(): String? {
        val name: String? = getName(RFC1779)
        return if (name.isNullOrBlank()) null else parsePrincipal(name!!, "C=([^,]*)")
    }

    private fun parsePrincipal(principalName: String, patternString: String): String? {
        val pattern = Pattern.compile(patternString)
        val matcher = pattern.matcher(principalName)
        return if (matcher.find()) {
            matcher.group(1)
        } else null
    }
}

