package com.kt.securcetokenstorage.hasher;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

/**
 * Implementation of the {@link EncryptionProvider} interface using MD5 hashing.
 * This class provides methods to hash a plaintext string using MD5 and verify it against a hashed value.
 * Note: MD5 is not considered secure for cryptographic purposes; use stronger algorithms (e.g., SHA-256) for secure applications.
 */
public class MD5EncryptionProvider implements EncryptionProvider {

    /**
     * Hashes the given plaintext using the MD5 algorithm.
     *
     * @param plainText The plaintext to hash.
     * @return A hexadecimal string representation of the MD5 hash.
     * @throws Exception If an error occurs during hashing.
     */
    @Override
    public String encrypt(String plainText) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(plainText.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * This method is not supported for MD5 because hashing is a one-way operation.
     *
     * @param cipherText The hashed value (ignored).
     * @return Nothing; this method always throws an exception.
     * @throws RuntimeException Always thrown, as MD5 does not support decryption.
     */
    @Override
    public String decrypt(String cipherText) throws Exception {
        throw new RuntimeException("MD5 hashing is one-way and does not support decryption");
    }

    /**
     * Verifies if the given plaintext matches the provided hashed string.
     *
     * @param plainText    The plaintext to verify.
     * @param hashedString The hashed value to compare against.
     * @return True if the plaintext matches the hashed string; false otherwise.
     * @throws Exception If an error occurs during hashing or comparison.
     */
    @Override
    public boolean match(String plainText, String hashedString) throws Exception {
        return Objects.equals(this.encrypt(plainText), hashedString);
    }
}
