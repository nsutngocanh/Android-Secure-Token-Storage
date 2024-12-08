package com.kt.securcetokenstorage.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kt.securcetokenstorage.constraints.StorageConst;

import java.util.ArrayList;
import java.util.List;

public class SQLiteTokenStorage extends SQLiteOpenHelper implements TokenStorage {
    private static final String DATABASE_NAME = "TokenStorage.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tokens";
    private static final String COLUMN_KEY = "token_key";
    private static final String COLUMN_VALUE = "token_value";

    private static final String COLUMN_EXPIRED = "time_expired";

    public SQLiteTokenStorage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void saveToken(String key, String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KEY, key);
        values.put(COLUMN_VALUE, token);
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    @Override
    public void logAllTokens() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_KEY, COLUMN_VALUE, COLUMN_EXPIRED}, null, null, null, null, null);

            if (cursor != null) {
                StringBuilder logMessage = new StringBuilder();
                logMessage.append("Token Log:\n");
                logMessage.append(String.format("%-20s %-50s %-20s\n", "Key", "Value", "Expired"));

                while (cursor.moveToNext()) {
                    String key = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KEY));
                    String token = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE));
                    String expiration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRED));

                    String expiredMessage = "N/A";
                    if (expiration != null) {
                        long expiredTime = Long.parseLong(expiration);
                        expiredMessage = expiredTime > System.currentTimeMillis() ? "Valid" : "Expired";
                    }

                    // Format each row of the log
                    logMessage.append(String.format("%-20s %-50s %-20s\n", key, token, expiredMessage));
                }

                Log.d("TokenStorage", logMessage.toString());
                cursor.close();
            }

            db.close();
        } catch (Exception e) {
            Log.d(StorageConst.LOG_TAG, e.getMessage());
        }
    }

    @Override
    public List<String> getAllTokens() {
        List<String> tokens = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            // Query to select all token values
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_VALUE}, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String token = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE));
                    tokens.add(token);
                }
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            Log.e(StorageConst.LOG_TAG, e.getMessage());
        }
        return tokens;
    }

    @Override
    public void saveToken(String key, String token, Long expiredTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KEY, key);
        values.put(COLUMN_VALUE, token);
        values.put(COLUMN_EXPIRED, System.currentTimeMillis() + expiredTime);
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    @Override
    public String getToken(String key) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_VALUE, COLUMN_EXPIRED}, COLUMN_KEY + "=?",
                new String[]{key}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String token = cursor.getString(0);
            String expireStr = cursor.getString(1);
            if (expireStr != null) {
                long expired = Long.parseLong(expireStr);
                if (System.currentTimeMillis() > expired) {
                    // remove token
                    this.deleteToken(key);
                    return null;
                }
            }
            cursor.close();
            return token;
        }
        return null;
    }

    @Override
    public void deleteToken(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_KEY + "=?", new String[]{key});
        db.close();
    }

    @Override
    public void clearAllToken() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_KEY + " TEXT PRIMARY KEY, " +
                COLUMN_EXPIRED + "TEXT," +
                COLUMN_VALUE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
