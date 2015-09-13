package com.yogeshn.fyp.androidclient.backend.connectivity;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * This class is resposible for the encryption and decryption of data to and from server.
 *
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
            byte[] encrypted = Base64.decode(message.getBytes("UTF-8"), Base64.DEFAULT);
            byte[] decrypted = decryptor.doFinal(encrypted);
            return new String(decrypted, "UTF-8");
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
            byte[] encrypted = encryptor.doFinal(message.getBytes("UTF-8"));
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "secret is null";
    }
}