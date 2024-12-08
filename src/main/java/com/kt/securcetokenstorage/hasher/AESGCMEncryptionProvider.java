package com.kt.securcetokenstorage.hasher;

import android.os.Build;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Implementation of the {@link EncryptionProvider} interface using AES-GCM encryption.
 * This class provides methods to securely encrypt, decrypt, and verify strings with AES-GCM mode.
 */
public class AESGCMEncryptionProvider implements EncryptionProvider {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12; // 96-bit IV (recommended length for GCM)
    private static final int TAG_LENGTH = 128; // 128-bit authentication tag
    private final SecretKey secretKey;

    /**
     * Constructs an AESGCMEncryptionProvider with the provided SecretKey.
     *
     * @param key The SecretKey used for encryption and decryption.
     * @throws Exception If the key is null or invalid.
     */
    public AESGCMEncryptionProvider(SecretKey key) throws Exception {
        if (key == null) {
            throw new IllegalArgumentException("SecretKey cannot be null");
        }
        this.secretKey = key;
    }

    /**
     * Encrypts the given plaintext using AES-GCM encryption.
     *
     * @param plainText The plaintext to encrypt.
     * @return The encrypted string encoded in Base64, including the IV.
     * @throws Exception If encryption fails.
     */
    @Override
    public String encrypt(String plainText) throws Exception {
        // Generate random IV
        byte[] iv = new byte[IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        // Initialize cipher in GCM mode
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        // Encrypt data
        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // Combine IV and ciphertext for storage
        byte[] encryptedData = new byte[IV_LENGTH + cipherText.length];
        System.arraycopy(iv, 0, encryptedData, 0, IV_LENGTH);
        System.arraycopy(cipherText, 0, encryptedData, IV_LENGTH, cipherText.length);

        // Encode as Base64 string
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(encryptedData);
        }
        throw new RuntimeException("This implementation requires SDK Version 26 or higher");
    }

    /**
     * Decrypts the given ciphertext using AES-GCM decryption.
     *
     * @param cipherText The Base64-encoded encrypted string to decrypt (includes IV).
     * @return The decrypted plaintext string.
     * @throws Exception If decryption fails.
     */
    @Override
    public String decrypt(String cipherText) throws Exception {
        byte[] encryptedData;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encryptedData = Base64.getDecoder().decode(cipherText);
        } else {
            throw new RuntimeException("This implementation requires SDK Version 26 or higher");
        }

        // Extract IV and ciphertext
        byte[] iv = new byte[IV_LENGTH];
        System.arraycopy(encryptedData, 0, iv, 0, IV_LENGTH);
        byte[] actualCipherText = new byte[encryptedData.length - IV_LENGTH];
        System.arraycopy(encryptedData, IV_LENGTH, actualCipherText, 0, actualCipherText.length);

        // Initialize cipher in GCM mode
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        // Decrypt data
        byte[] plainTextBytes = cipher.doFinal(actualCipherText);

        return new String(plainTextBytes, StandardCharsets.UTF_8);
    }

    /**
     * Verifies if the given plaintext matches the provided encrypted string.
     *
     * @param plainText The plaintext to verify.
     * @param hashedString The Base64-encoded encrypted string to compare against.
     * @return True if the plaintext matches the encrypted string; false otherwise.
     * @throws Exception If encryption or comparison fails.
     */
    @Override
    public boolean match(String plainText, String hashedString) throws Exception {
        return Objects.equals(this.encrypt(plainText), hashedString);
    }
}
