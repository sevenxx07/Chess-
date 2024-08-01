package cz.cvut.fel.pjv.chessclient.utilities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hasher takes a String representing password in plain text, and turns
 * it into a hash.
 */
public class Hasher {

    private static final Logger logger = Logger.getLogger(Hasher.class.getName());

    /**
     * Takes a String, and turns it into a hash using SHA-256 algorithm.
     *
     * @param plainPassword String representing user's password to be hashed
     * @return hashed password as a String
     */
    public static String hashPassword(String plainPassword) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "Couldn't hash password", e);
            return null;
        }
        //get digest as a sequence of bytes
        byte[] encodedHash = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
        //make a String out of bytes in the digest
        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte b : encodedHash) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

}
