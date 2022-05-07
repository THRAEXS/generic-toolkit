package org.thraex.toolkit.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;

/**
 * @author 鬼王
 * @date 2022/03/28 15:23
 */
public abstract class RSAUtil {

    public static final String ALGORITHM = "RSA";
    public static final int KEY_SIZE = 2048;

    public static Pairs generator() throws NoSuchAlgorithmException {
        return generator(KEY_SIZE);
    }

    public static Pairs generator(Integer keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(Optional.ofNullable(keySize).orElse(KEY_SIZE));
        KeyPair keyPair = generator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        byte[] publicKeyEncoded = publicKey.getEncoded();
        byte[] privateKeyEncoded = privateKey.getEncoded();

        Base64.Encoder encoder = Base64.getEncoder();
        String pub = encoder.encodeToString(publicKeyEncoded);
        String prv = encoder.encodeToString(privateKeyEncoded);

        return new Pairs(pub, prv);
    }

    public static byte[] toBytes(String key) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(key);

        return bytes;
    }

    public static PublicKey parsePublicKey(String publicKey) throws GeneralSecurityException {
        byte[] keyBytes = toBytes(publicKey);

        KeyFactory instance = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        PublicKey pubKey = instance.generatePublic(keySpec);

        return pubKey;
    }

    public static PrivateKey parsePrivateKey(String privateKey) throws GeneralSecurityException {
        byte[] keyBytes = toBytes(privateKey);

        KeyFactory instance = KeyFactory.getInstance(ALGORITHM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        PrivateKey prvKey = instance.generatePrivate(keySpec);

        return prvKey;
    }

    public static KeyPair parser(String publicKey, String privateKey) throws GeneralSecurityException {
        PublicKey pub = parsePublicKey(publicKey);
        PrivateKey prv = parsePrivateKey(privateKey);

        return new KeyPair(pub, prv);
    }

    public static String encrypt(String raw, String publicKey) throws GeneralSecurityException {
        PublicKey pubKey = parsePublicKey(publicKey);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        byte[] rawBytes = raw.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = cipher.doFinal(rawBytes);

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encrypted, String privateKey) throws GeneralSecurityException {
        PrivateKey prvKey = parsePrivateKey(privateKey);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, prvKey);

        byte[] encryptedBytes = Base64.getDecoder().decode(encrypted);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static class Pairs {

        private String pub;

        private String prv;

        public Pairs(String pub, String prv) {
            this.pub = pub;
            this.prv = prv;
        }

        public String pub() {
            return this.pub;
        }

        public String prv() {
            return this.prv;
        }

        @Override
        public String toString() {
            return "Keys{" +
                    "pub='" + pub + '\'' +
                    ", prv='" + prv + '\'' +
                    '}';
        }

    }

    public static void main(String[] args) {
        //testAround();
        testDecrypt();
    }

    public static void testAround() {
        try {
            Pairs pairs = generator();
            System.out.println("Public key:");
            System.out.println(pairs.pub());
            System.out.println("Private key:");
            System.out.println(pairs.prv());

            String raw = "hanzo";
            String encrypted = encrypt(raw, pairs.pub());
            System.out.println("Encrypted:");
            System.out.println(encrypted);

            System.out.println("Decrypted:");
            System.out.println(decrypt(encrypted, pairs.prv()));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public static void testDecrypt() {
        try {
            String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCBW3iCo9tEjoj6HHdtf2k49DOmhE1c77MDl6XmBOaE4Tqal8kWaD7T3v8yyfzv0f9ERl+fYYZP6Ma6DhZtHmAwoDYb/vczSB8rr1Sjq91LTMx5R4Ot4CxgblqmmAG3c/nPOIb/UcLPCPDhZ+3nujCENUkU6mQ7rmfqAqI1Herqlt6VWbZRlwsJIkMX+23gOEp3JoHArTEUn/BEY8oFUGa7W4ko3gpshBCOGvnYF6VR2swkAj53ndmM2S50rhNWRCKSZKkcdgaOL+tDU4oSNIQRqeqgbLmgN/M/pev/+gwxIQA8sU7B4kTHvuiZEpSSbCnhFgpR/LQXWWfw7EBY2tB9AgMBAAECggEAGYHq+n/LDfK5SfBxEFNucT+eK7is9KWDfDLcEMZomk07XB6QFW4K7YZsdhxkVvnmxxTFaE03yewRu8BpZaz2tL/yy4R0RFV1aAzTuM57/YwwSb3zVkb9GSrJj04sEHu7B8SSwifiLScLMLdorygExx0mwwwRJb+XWAqa57R1jTpXnQjv4Nft6PVifMVnGymIjHHfVXUJkqXzmlbeWhdJhEwOfJRUMFwTp2Gi1v15Z5FHuIh5/NGxRsPje0s4YMSTbu4FDiMwlzFCy9zkDk/BZsLK9sYEcs+92Ay69z55ZjFWX7xXL7CyPLgTQ4y9bQE1TmqaAyVjyh64Bg/gKM6GYQKBgQDc5k8UDX/ld1i3452LtX8XaHpVY0yQGnbKzp4Zob1YQLUtVpuiTZHzhbM5pSVQFlCXkzhSiPyQvsmaArr6jFriWEUhbYgmIDlFa2StS0ZH0Gcw8rdf6q0vzujchZrRm546oQO/FgdWNz2K85olTVqoM2+4Hbpi8oFvFHSED1hFdQKBgQCV6W3w+Vo6IlUYBzkFKMoLGgjyPJreWotHP+FyzTTjqK0UE/i9P8PbjYpGTEHSOwfzg7DP/qR93arnAhIV6qM4RfrsDj+TuBMD9OaknNvRbl0EpZY5nvSNwqjp62CVPVLSQ9kBW7UtGGSuiVxJHxm9v6N/6PRjGeYNzejdLW0V6QKBgFo+pNPWaAfA6DfH/5cSAOf5QPEdbiv5A8r6+lASaZ5iYSIyncaC1jucxYmpVEMRur8R4BKn8DbaGtaWgvjU2lRaJ3PuoY6h34PiyfCaLg4sr9upbQz8fOBpMWzWEFfNsajWGwe34itwye24c6MFpSHOUbfwPTMrS4Gr46YH9tH5AoGAAS4pU3BjKXoDuYC1DjlX/eZik6WugnmsBw+VstWyyOgXFMVje/n4jM38fLk0+3bDhUNQLRMQMH2CTvdRNSL3zgWfCCTEk2ErpShUeI9Tm76GtPaozCNYQZV6xvy3cfVdpZIrLzuNnaFHiahDNcAs77WGkAdBhVY63Xj1kGg/J6ECgYBAdm6guGXpNCuTnryDN/w+Ufj3BKJm83d+qfBGAw7C3VhtTeARZzaUGugMSZTYC48Xx6K1dgwzYsIJtqkDZVuBLgRI39lXZbiKPGUSWhupzjNymW6Qkjw+SeULXPvwlAVGscm3K0zhf/ovgJiCXc1mzozO0yXWTZaqqHp0jnMZOQ==";
            String encrypted = "EaiySoZKcMvFQpKunwRD+YuhaAIt/sczjuwJzXhmq1j8Oi22zR5skJVGpPfpCWg0FPZ8LMIKepZ2TmkillNyT0a+LYlr1JWtM/UMvmf8uG2FphI/TNFFRqVo3cFBS8iQlvti+JfvTeAdVyyuAQJOo91LD2NSsbCocirRQvCUXbUl9uVm0dYtrV/AJkaUlaEMkbuT273CDmwSGyYYgG3+Aut9ylrMRBtaUaoOLuNb5HVZJSuMdw6SQcy+5LxoJGXbQTFyneQL4wTOLI5Uh1frvCFKLogsRoDQGBn7V04vhmMxZsSs+ujoAzS9nQuleyQGdnJCYdj/zOyYKDKCf5LDvg==";
            String decrypt = decrypt(encrypted, privateKey);
            System.out.println(decrypt);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

}
