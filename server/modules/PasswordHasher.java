package laba8.laba8.server.modules;

import laba8.laba8.server.App;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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
            byte[] hash = md.digest((password + pepper).getBytes(StandardCharsets.UTF_8));
            BigInteger integers = new BigInteger(1, hash);
            String newPassword = integers.toString(16);
            while (newPassword.length() < 32) {
                newPassword = "0" + newPassword;
            }
            return newPassword;
        } catch (NoSuchAlgorithmException exception) {
            App.logger.info("Password hashing algorithm not found!");
            throw new IllegalStateException(exception);
        }
    }

}
