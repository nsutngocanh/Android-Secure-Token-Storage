package com.kt.securcetokenstorage.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SharedPreferencesTokenStorage implements TokenStorage {
    private final SharedPreferences sharedPreferences;

    private final String EXP_PREFIX = "EXP_";

    public SharedPreferencesTokenStorage(Context context, String name) {
        this.sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    @Override
    public void saveToken(String key, String token) {
        sharedPreferences.edit().putString(key, token).apply();
    }

    @Override
    public void logAllTokens() {
        Map<String, ?> allEntries = sharedPreferences.getAll();
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Token Log:\n");
        logMessage.append(String.format("%-20s %-50s %-20s\n", "Key", "Value", "Expired"));

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();

            // Skip expiration keys for now
            if (key.startsWith(EXP_PREFIX)) {
                continue;
            }

            // Get the token value
            String token = (String) entry.getValue();

            // Check expiration
            String expiredMessage = "N/A";
            long expirationTime = sharedPreferences.getLong(EXP_PREFIX + key, -1);
            if (expirationTime != -1) {
                expiredMessage = expirationTime > System.currentTimeMillis() ? "Valid" : "Expired";
            }

            // Append to log message
            logMessage.append(String.format("%-20s %-50s %-20s\n", key, token, expiredMessage));
        }

        Log.d("SharedPreferencesTokenStorage", logMessage.toString());
    }

    @Override
    public List<String> getAllTokens() {
        List<String> tokens = new ArrayList<>();
        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith(EXP_PREFIX)) {
                String token = (String) entry.getValue();
                tokens.add(token);
            }
        }

        return tokens;
    }

    @Override
    public void saveToken(String key, String token, Long expiredTime) {
        long expTime = System.currentTimeMillis() + expiredTime;
        this.saveToken(key, token);
        sharedPreferences.edit().putLong(String.format("%s%s", EXP_PREFIX, key), expTime).apply();
    }

    @Override
    public String getToken(String key) {
        // check expired to delete
        long exp = sharedPreferences.getLong(String.format("%s%s", EXP_PREFIX, key), -1);
        if (exp != -1) {
            if (exp <= System.currentTimeMillis()) {
                // token expired => delete token
                this.deleteToken(key);
                return null; // return null
            }
        }
        return sharedPreferences.getString(key, null);
    }

    @Override
    public void deleteToken(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    @Override
    public void clearAllToken() {
        sharedPreferences.edit().clear().apply();
    }
}
