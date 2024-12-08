package com.kt.securcetokenstorage.storage;

import android.util.Log;

import com.kt.securcetokenstorage.constraints.StorageConst;
import com.kt.securcetokenstorage.device.AnomalyDetector;
import com.kt.securcetokenstorage.hasher.EncryptionProvider;

import java.util.ArrayList;
import java.util.List;

public class SecureTokenManager {
    private final TokenStorage tokenStorage;
    private EncryptionProvider encryptionProvider = null;
    private AnomalyDetector anomalyDetector = null;


    public SecureTokenManager(TokenStorage tokenStorage) {
        this.tokenStorage = tokenStorage;
    }

    public SecureTokenManager(TokenStorage tokenStorage, EncryptionProvider encryptionProvider) {
        this.tokenStorage = tokenStorage;
        this.encryptionProvider = encryptionProvider;
    }

    public SecureTokenManager(TokenStorage tokenStorage, EncryptionProvider encryptionProvider, AnomalyDetector anomalyDetector) {
        this.tokenStorage = tokenStorage;
        this.encryptionProvider = encryptionProvider;
        this.anomalyDetector = anomalyDetector;
    }

    public void logAllToken() {
        try {
            tokenStorage.logAllTokens();
        } catch (Exception e) {
            Log.d(StorageConst.LOG_TAG, e.getMessage());
        }
    }

    public void saveToken(String key, String token, Long expiredTime) {
        try {
            if (anomalyDetector != null) {
                if (anomalyDetector.isDeviceRooted()) {
                    throw new SecurityException("Cannot save token on a rooted device.");
                }
            }
            if (encryptionProvider != null) {
                String encryptedToken = encryptionProvider.encrypt(token);
                tokenStorage.saveToken(key, encryptedToken);
            }
            tokenStorage.saveToken(key, token, expiredTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllToken() {
        try {
            if (anomalyDetector != null) {
                if (anomalyDetector.isDeviceRooted()) {
                    throw new SecurityException("Cannot retrieve token on a rooted device.");
                }
            } else {
                Log.w(StorageConst.LOG_TAG, "Device Root Checker Not Enable");
            }
            if (encryptionProvider != null) {
                List<String> tokens = new ArrayList<>();
                List<String> encryptedTokens = tokenStorage.getAllTokens();
                for (String encryptedToken : encryptedTokens) {
                    tokens.add(encryptedToken != null ? encryptionProvider.decrypt(encryptedToken) : null);
                }
                return tokens;
            } else {
                Log.w(StorageConst.LOG_TAG, "Your Key Not Encrypt");
            }
            return tokenStorage.getAllTokens();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveToken(String key, String token) {
        try {
            if (anomalyDetector != null) {
                if (anomalyDetector.isDeviceRooted()) {
                    throw new SecurityException("Cannot save token on a rooted device.");
                }
            }
            if (encryptionProvider != null) {
                String encryptedToken = encryptionProvider.encrypt(token);
                tokenStorage.saveToken(key, encryptedToken);
            }
            tokenStorage.saveToken(key, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getToken(String key) {
        try {
            if (anomalyDetector != null) {
                if (anomalyDetector.isDeviceRooted()) {
                    throw new SecurityException("Cannot retrieve token on a rooted device.");
                }
            } else {
                Log.w(StorageConst.LOG_TAG, "Device Root Checker Not Enable");
            }
            if (encryptionProvider != null) {
                String encryptedToken = tokenStorage.getToken(key);
                return encryptedToken != null ? encryptionProvider.decrypt(encryptedToken) : null;
            } else {
                Log.w(StorageConst.LOG_TAG, "Your Key Not Encrypt");
            }
            return tokenStorage.getToken(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteToken(String key) {
        tokenStorage.deleteToken(key);
    }

    public void clearAllTokens() {
        tokenStorage.clearAllToken();
    }

    public static class Builder {
        private TokenStorage tokenStorage;
        private EncryptionProvider encryptionProvider = null;
        private AnomalyDetector rootChecker = null;

        public Builder signStorage(TokenStorage tokenStorage) {
            this.tokenStorage = tokenStorage;
            return this;
        }

        public Builder enableAnomalyDetector(boolean enable) {
            if (enable) {
                this.rootChecker = new AnomalyDetector();
            }
            return this;
        }

        public Builder setEncryptionProvider(EncryptionProvider encryptionProvider) throws Exception {
            this.encryptionProvider = encryptionProvider;
            return this;
        }

        public SecureTokenManager build() {
            if (tokenStorage == null) {
                throw new IllegalStateException("TokenStorage must not be null");
            }
            return new SecureTokenManager(tokenStorage, encryptionProvider, rootChecker);
        }
    }
}
