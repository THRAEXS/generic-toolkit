package org.thraex.toolkit.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author 鬼王
 * @date 2022/06/06 00:37
 */
public abstract class AESUtil {

    public static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    public static final String ALGORITHM = "AES";
    public static final int SEED_LENGTH = 16;

    public static String encrypt(String key, String input) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] encrypted = encrypt(key.getBytes(StandardCharsets.UTF_8), input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static byte[] encrypt(byte[] key, byte[] input) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
        SecureRandom sr = SecureRandom.getInstanceStrong();
        byte[] iv = sr.generateSeed(SEED_LENGTH);
        IvParameterSpec ivps = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivps);
        byte[] data = cipher.doFinal(input);

        return join(iv, data);
    }

    public static String decrypt(String key, String input) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] decode = Base64.getDecoder().decode(input.getBytes(StandardCharsets.UTF_8));
        byte[] decrypted = decrypt(key.getBytes(StandardCharsets.UTF_8), decode);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    public static byte[] decrypt(byte[] key, byte[] input) throws GeneralSecurityException {
        byte[] iv = new byte[SEED_LENGTH];
        byte[] data = new byte[input.length - SEED_LENGTH];
        System.arraycopy(input, 0, iv, 0, SEED_LENGTH);
        System.arraycopy(input, SEED_LENGTH, data, 0, data.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
        IvParameterSpec ivps = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivps);

        return cipher.doFinal(data);
    }

    public static byte[] join(byte[] bs1, byte[] bs2) {
        byte[] r = new byte[bs1.length + bs2.length];
        System.arraycopy(bs1, 0, r, 0, bs1.length);
        System.arraycopy(bs2, 0, r, bs1.length, bs2.length);

        return r;
    }

}
