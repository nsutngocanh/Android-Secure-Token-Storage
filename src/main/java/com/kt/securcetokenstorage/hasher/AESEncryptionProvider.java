package com.kt.securcetokenstorage.hasher;

import android.os.Build;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * Implementation of the {@link EncryptionProvider} interface using AES encryption.
 * This class provides methods to encrypt, decrypt, and verify strings with AES.
 */
public class AESEncryptionProvider implements EncryptionProvider {

    private static final String ALGORITHM = "AES";
    private final SecretKey secretKey;

    /**
     * Constructs an AESEncryptionProvider with the provided SecretKey.
     *
     * @param key The SecretKey used for encryption and decryption.
     * @throws Exception If the key is null or invalid.
     */
    public AESEncryptionProvider(SecretKey key) throws Exception {
        if (key == null) {
            throw new IllegalArgumentException("SecretKey cannot be null");
        }
        this.secretKey = key;
    }

    /**
     * Encrypts the given plaintext using AES encryption.
     *
     * @param plainText The plaintext to encrypt.
     * @return The encrypted string encoded in Base64.
     * @throws Exception If encryption fails.
     */
    @Override
    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        throw new RuntimeException("This implementation requires SDK Version 26 or higher");
    }

    /**
     * Decrypts the given ciphertext using AES decryption.
     *
     * @param cipherText The Base64-encoded encrypted string to decrypt.
     * @return The decrypted plaintext string.
     * @throws Exception If decryption fails.
     */
    @Override
    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }
        throw new RuntimeException("This implementation requires SDK Version 26 or higher");
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
