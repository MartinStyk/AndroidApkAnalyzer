package sk.styk.martin.apkanalyzer.util;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Martin Styk on 10.12.2017.
 */

public class DigestHelper {

    public static String md5Digest(byte[] input) throws IOException {
        MessageDigest digest = getDigest("Md5");
        digest.update(input);
        return getHexString(digest.digest());
    }

    public static String md5Digest(String input) throws IOException {
        MessageDigest digest = getDigest("Md5");
        digest.update(input.getBytes());
        return getHexString(digest.digest());
    }

    public static String byteToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        byte[] var4 = bArray;
        int var5 = bArray.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            byte aBArray = var4[var6];
            String sTemp = Integer.toHexString(255 & (char) aBArray);
            if (sTemp.length() < 2) {
                sb.append(0);
            }

            sb.append(sTemp.toUpperCase());
        }

        return sb.toString();
    }

    public static String getHexString(byte[] digest) {
        BigInteger bi = new BigInteger(1, digest);
        return String.format("%032x", new Object[]{bi});
    }

    public static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException var3) {
            throw new RuntimeException(var3.getMessage());
        }
    }

}
