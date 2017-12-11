package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sk.styk.martin.apkanalyzer.model.detail.CertificateData;
import sk.styk.martin.apkanalyzer.util.DigestHelper;

import static javax.security.auth.x500.X500Principal.RFC1779;

/**
 * Created by Martin Styk on 22.06.2017.
 */

public class CertificateService {

    public CertificateData get(@NonNull PackageInfo packageInfo) {

        CertificateData data = new CertificateData();

        Signature sig = packageInfo.signatures[0];

        byte[] rawCert = sig.toByteArray();
        InputStream certStream = new ByteArrayInputStream(rawCert);
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X509");
            X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(certStream);

            data.setCertMd5(DigestHelper.md5Digest(certificate.getEncoded()));
            data.setPublicKeyMd5(DigestHelper.md5Digest(DigestHelper.byteToHexString(certificate.getPublicKey().getEncoded())));

            data.setStartDate(certificate.getNotBefore());
            data.setEndDate(certificate.getNotAfter());
            data.setSignAlgorithm(certificate.getSigAlgName());
            data.setSerialNumber(certificate.getSerialNumber().intValue());

            String issuerRFC1779 = certificate.getIssuerX500Principal().getName(RFC1779);
            if (issuerRFC1779 != null && !issuerRFC1779.isEmpty()) {
                data.setIssuerName(getPrincipalCommonName(issuerRFC1779));
                data.setIssuerOrganization(getPrincipalOrganization(issuerRFC1779));
                data.setIssuerCountry(getPrincipalCountry(issuerRFC1779));
            }

            String subjectRFC1779 = certificate.getSubjectX500Principal().getName(RFC1779);
            if (issuerRFC1779 != null && !issuerRFC1779.isEmpty()) {
                data.setSubjectName(getPrincipalCommonName(subjectRFC1779));
                data.setSubjectOrganization(getPrincipalOrganization(subjectRFC1779));
                data.setSubjectCountry(getPrincipalCountry(subjectRFC1779));
            }

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                certStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public String getSignAlgorithm(@NonNull PackageInfo packageInfo) {
        Signature sig = packageInfo.signatures[0];
        byte[] rawCert = sig.toByteArray();
        InputStream certStream = new ByteArrayInputStream(rawCert);
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X509");
            X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(certStream);

            return certificate.getSigAlgName();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                certStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private String getPrincipalCommonName(String principalName) {
        String pattern = "CN=([^,]*)";
        return parsePrincipal(principalName, pattern);
    }

    private String getPrincipalOrganization(String principalName) {
        String pattern = "O=([^,]*)";
        return parsePrincipal(principalName, pattern);
    }

    private String getPrincipalCountry(String principalName) {
        String pattern = "C=([^,]*)";
        return parsePrincipal(principalName, pattern);
    }

    private String parsePrincipal(String principalName, String patternString) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(principalName);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
