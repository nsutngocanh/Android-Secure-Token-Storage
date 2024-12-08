package com.kt.securcetokenstorage.hasher;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.Key;
import java.security.KeyStore;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyStoreHelper {

    private static final String KEYSTORE_INSTANCE = "AndroidKeyStore";
    private static final String DEFAULT_KEY_ALIAS = "MyKeyAlias";

    private final KeyStore keyStore;

    public KeyStoreHelper() throws Exception {
        // Initialize the KeyStore instance
        keyStore = KeyStore.getInstance(KEYSTORE_INSTANCE);
        keyStore.load(null);

        // Ensure the default key exists
        if (!keyStore.containsAlias(DEFAULT_KEY_ALIAS)) {
            generateDefaultKey();
        }
    }

    private void generateDefaultKey() throws Exception {
        KeyGenParameterSpec keySpec = new KeyGenParameterSpec.Builder(
                DEFAULT_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
        )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setUserAuthenticationRequired(true) // Requires user authentication
                .build();

        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                KEYSTORE_INSTANCE
        );
        keyGenerator.init(keySpec);
        keyGenerator.generateKey();
    }

    /**
     * Generate a new secret key with a unique alias.
     *
     * @return The alias of the newly generated key.
     * @throws Exception If an error occurs during key generation.
     */
    public String generateSecretKey() throws Exception {
        String keyAlias = "KeyAlias-" + UUID.randomUUID();

        KeyGenParameterSpec keySpec = new KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
        )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setUserAuthenticationRequired(true)
                .build();

        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                KEYSTORE_INSTANCE
        );
        keyGenerator.init(keySpec);
        keyGenerator.generateKey();

        return keyAlias;
    }

    /**
     * Retrieve a secret key by alias.
     *
     * @param keyAlias The alias of the key to retrieve.
     * @return The SecretKey if found, or null if not.
     * @throws Exception If an error occurs during key retrieval.
     */
    public SecretKey getSecretKey(String keyAlias) throws Exception {
        Key key = keyStore.getKey(keyAlias, null);
        if (key instanceof SecretKey) {
            return (SecretKey) key;
        }
        return null;
    }

    /**
     * Delete a key from the KeyStore.
     *
     * @param keyAlias The alias of the key to delete.
     * @throws Exception If an error occurs during key deletion.
     */
    public void deleteKey(String keyAlias) throws Exception {
        keyStore.deleteEntry(keyAlias);
    }

    /**
     * Check if a key exists in the KeyStore.
     *
     * @param keyAlias The alias of the key to check.
     * @return True if the key exists, false otherwise.
     * @throws Exception If an error occurs during the check.
     */
    public boolean containsKey(String keyAlias) throws Exception {
        return keyStore.containsAlias(keyAlias);
    }
}
