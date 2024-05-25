package laba6.server.modules;

import laba6.server.App;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class PasswordHasher {
    /**
     * Hashes password;.
     *
     * @param password Password itself.
     * @return Hashed password.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            String pepper = "*6%q1!20k*&";
//            String salt = generateRandomString(20);
            byte[] hash = md.digest((password + pepper).getBytes("UTF-8"));
            BigInteger integers = new BigInteger(1, hash);
            String newPassword = integers.toString(16);
            while (newPassword.length() < 32) {
                newPassword = "0" + newPassword;
            }
            return newPassword;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exception) {
            App.logger.info("Password hashing algorithm not found!");
            throw new IllegalStateException(exception);
        }
    }

    private static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char randomChar = (char) (random.nextInt(26) + 'a');
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }
}
