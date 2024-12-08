package com.kt.securcetokenstorage.storage;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.kt.securcetokenstorage.constraints.StorageConst;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class FileTokenStorage implements TokenStorage {
    private final File storageDir;
    private final String NAME = "TokenStorage";

    public FileTokenStorage(Context context) {
        storageDir = new File(context.getFilesDir(), NAME);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
    }

    @Override
    public void saveToken(String key, String token) {
        File file = new File(storageDir, key);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(token.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logAllTokens() {
        Log.i(StorageConst.LOG_TAG, "File token doesn't support log allTokens method");
    }

    @Override
    public List<String> getAllTokens() {
        Log.i(StorageConst.LOG_TAG, "File token doesn't support get all method");
        return null;
    }

    @Override
    public void saveToken(String key, String token, Long expiredTime) {
        Log.w("Android Storage Token ::: ", "File Token Not Supported Expired Time");
    }

    @Override
    public String getToken(String key) {
        File file = new File(storageDir, key);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return new String(Files.readAllBytes(file.toPath()));
            }
            throw new RuntimeException("This Build-In Required SDK >= 26");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteToken(String key) {
        File file = new File(storageDir, key);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void clearAllToken() {
        for (File file : Objects.requireNonNull(storageDir.listFiles())) {
            file.delete();
        }
    }
}
