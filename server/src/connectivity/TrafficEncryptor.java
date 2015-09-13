package connectivity;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * This class is responsible for encrypting and decrypting data.
 * <p>
 */

public class TrafficEncryptor {

    private static SecretKeySpec secretKeySpec;
    private static Cipher decryptor;
    private static Cipher encryptor;

    /**
     * Creates the initialisation vectors and the decryptors and encryptors for the encryption process.
     */
    private static void create() {
        byte[] ivSpec = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivSpec);

        try {
            char[] password = new char[]{'s', 'b', '4', '9', 'w', 'a', '2'};
            byte[] salt = new byte[]{'4', 4, 'f', 'h', 'f', 2, 5};

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            secretKeySpec = new SecretKeySpec(factory.generateSecret(new PBEKeySpec(password, salt, 65536, 128)).getEncoded(), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            encryptor = Cipher.getInstance("AES/CBC/NoPadding");
            encryptor.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            decryptor = Cipher.getInstance("AES/CBC/NoPadding");
            decryptor.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Decrypts the message.
     *
     * @param message ciphertext
     * @return plaintext
     */
    public static String decrypt(String message) {
        if (secretKeySpec == null) {
            create();
        }
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decodedBytes = decoder.decodeBuffer(message);
            return new String(decryptor.doFinal(decodedBytes), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failed";
    }

    /**
     * Encrypts the a message.
     *
     * @param message plaintext
     * @return ciphertext
     */
    public static String encrypt(String message) {
        if (secretKeySpec == null) {
            create();
        }
        try {
            return DatatypeConverter.printBase64Binary(encryptor.doFinal(message.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "secret is null";
    }

    public static String encryptAndFormat(String message) {
        return encrypt(message) + "FINISH";
    }
}