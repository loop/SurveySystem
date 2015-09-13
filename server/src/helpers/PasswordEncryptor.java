package helpers;

import java.security.MessageDigest;

/**
 * This class is responsible for hashing passwords using SHA-256.
 */

public class PasswordEncryptor
{
    private static final String salt = "67856v78e567x453zx7456";

    public static String generateSHA256(String password)
    {
        try
        {
            password = password + salt;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes("UTF-8"));
            byte[] hash = digest.digest();
            StringBuilder stringBuffer = new StringBuilder();
            for (byte aHash : hash) {
                stringBuffer.append(Integer.toString((aHash & 0xff) + 0x100, 16).substring(1));
            }
            return stringBuffer.toString();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
