package com.kt.securcetokenstorage.hasher;

/**
 * Interface defining encryption and decryption operations.
 * Implementations of this interface are responsible for providing
 * mechanisms to encrypt, decrypt, and verify matching encrypted data.
 */
public interface EncryptionProvider {

    /**
     * Encrypts a given plaintext string.
     *
     * @param plainText The plaintext string to be encrypted.
     * @return The encrypted string.
     * @throws Exception If encryption fails.
     */
    String encrypt(String plainText) throws Exception;

    /**
     * Decrypts a given ciphertext string.
     *
     * @param cipherText The encrypted string to be decrypted.
     * @return The decrypted plaintext string.
     * @throws Exception If decryption fails.
     */
    String decrypt(String cipherText) throws Exception;

    /**
     * Verifies if the given plaintext matches the hashed/encrypted string.
     *
     * @param plainText The plaintext string to verify.
     * @param hashedString The hashed or encrypted string to compare against.
     * @return True if the plaintext matches the hashed string; false otherwise.
     * @throws Exception If the verification process fails.
     */
    boolean match(String plainText, String hashedString) throws Exception;

}
