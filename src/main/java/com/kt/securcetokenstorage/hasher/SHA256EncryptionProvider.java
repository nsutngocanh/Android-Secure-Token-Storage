package com.kt.securcetokenstorage.hasher;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

/**
 * Implementation of the {@link EncryptionProvider} interface using SHA-256 hashing.
 * This class provides methods to hash a plaintext string using SHA-256 and verify it against a hashed value.
 * SHA-256 is a secure hashing algorithm suitable for cryptographic purposes.
 */
public class SHA256EncryptionProvider implements EncryptionProvider {

    /**
     * Hashes the given plaintext using the SHA-256 algorithm.
     *
     * @param plainText The plaintext to hash.
     * @return A hexadecimal string representation of the SHA-256 hash.
     * @throws Exception If an error occurs during hashing.
     */
    @Override
    public String encrypt(String plainText) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(plainText.getBytes(StandardCharsets.UTF_8));
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
     * This method is not supported for SHA-256 because hashing is a one-way operation.
     *
     * @param cipherText The hashed value (ignored).
     * @return Nothing; this method always throws an exception.
     * @throws UnsupportedOperationException Always thrown, as SHA-256 does not support decryption.
     */
    @Override
    public String decrypt(String cipherText) throws Exception {
        throw new UnsupportedOperationException("SHA-256 is one-way encryption and cannot be decrypted.");
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
