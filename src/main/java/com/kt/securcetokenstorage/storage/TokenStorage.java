package com.kt.securcetokenstorage.storage;

import java.util.List;

/**
 * The {@code TokenStorage} interface defines methods for storing, retrieving, deleting, and managing tokens.
 * Implementing classes should handle the storage of tokens in a secure and efficient manner.
 */
public interface TokenStorage {

    /**
     * Saves a token associated with a specified key.
     * This method stores the token in a secure storage solution with no expiration time specified.
     *
     * @param key   The key to associate with the token.
     * @param token The token to store.
     */
    void saveToken(String key, String token);

    void logAllTokens();

    /**
     * Retrieves all tokens associated with the specified key.
     * This method returns a list of all tokens stored under the given key, which may include access tokens,
     * refresh tokens, or other types of tokens.
     *
     * @return A list of tokens associated with the specified key.
     */
    List<String> getAllTokens();


    /**
     * Saves a token associated with a specified key and sets an expiration time for the token.
     * This method stores the token with an associated expiration time.
     *
     * @param key         The key to associate with the token.
     * @param token       The token to store.
     * @param expiredTime The expiration time of the token (in milliseconds).
     */
    void saveToken(String key, String token, Long expiredTime);

    /**
     * Retrieves the token associated with the specified key.
     * If the token exists and is valid, it is returned. If no valid token is found, an exception or null may be returned.
     *
     * @param key The key identifying the token to retrieve.
     * @return The stored token associated with the key, or {@code null} if not found.
     */
    String getToken(String key);

    /**
     * Deletes the token associated with the specified key.
     * This method removes the token from storage and invalidates it.
     *
     * @param key The key identifying the token to delete.
     */
    void deleteToken(String key);

    /**
     * Clears all tokens from storage.
     * This method removes all tokens, effectively clearing the entire token storage.
     */
    void clearAllToken();
}
