package com.kt.securcetokenstorage.network.session;

import com.kt.securcetokenstorage.network.token.TokenHandler;
import com.kt.securcetokenstorage.network.token.TokenInfo;
import com.kt.securcetokenstorage.storage.SecureTokenManager;

/**
 * The {@code SessionManager} interface provides methods for managing secure tokens within a session.
 * It allows the handling of tokens, their validation, refreshing, and anomaly detection.
 * The interface works closely with {@code TokenHandler} and {@code TokenInfo} to interact with and manage
 * token data securely, while also providing token lifecycle management functionality.
 */
public interface SessionManager {

    /**
     * Refreshes the token associated with the specified key.
     * This operation should fetch a new token and replace the old one.
     *
     * @param key The key that identifies the token to refresh.
     * @throws Exception If an error occurs during token refresh.
     */
    void refreshToken(String key) throws Exception;

    /**
     * Sets a new token for the specified key.
     * This method stores the token securely with the associated {@code TokenHandler}.
     *
     * @param key The key to associate with the token.
     * @param token The token string to store.
     * @param tokenHandler The {@code TokenHandler} that will manage the token.
     */
    void setToken(String key, String token, TokenHandler tokenHandler);

    /**
     * Handles anomalies detected in the token or session.
     * This could involve actions such as alerting the user or logging the anomaly.
     *
     * @param secureTokenManager The {@code SecureTokenManager} responsible for managing token data.
     * @param key The key associated with the token that triggered the anomaly.
     */
    void handleAnomaly(SecureTokenManager secureTokenManager, String key);

    /**
     * Checks whether the token associated with the specified key is valid.
     * Validity checks could involve expiration, integrity, or other security concerns.
     *
     * @param key The key identifying the token to validate.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     * @throws Exception If an error occurs during the validation process.
     */
    boolean isTokenValid(String key) throws Exception;

    /**
     * Adds a new token to the session using the provided {@code TokenInfo} and {@code TokenHandler}.
     * This method securely stores the token information.
     *
     * @param tokenInfo The {@code TokenInfo} containing details about the token.
     * @param tokenHandler The {@code TokenHandler} responsible for managing the token.
     */
    void addToken(TokenInfo tokenInfo, TokenHandler tokenHandler);

    /**
     * Removes the token associated with the specified key from the session.
     * This method securely deletes the token and performs any necessary cleanup.
     *
     * @param key The key associated with the token to remove.
     * @return {@code true} if the token was successfully removed, {@code false} otherwise.
     * @throws Exception If an error occurs while removing the token.
     */
    boolean removeToken(String key) throws Exception;

    /**
     * Retrieves the token information associated with the specified key.
     * This method returns the {@code TokenInfo} for the token, which includes details such as the token string
     * and other relevant metadata.
     *
     * @param key The key identifying the token to retrieve.
     * @return The {@code TokenInfo} associated with the specified key.
     * @throws Exception If an error occurs while retrieving the token.
     */
    TokenInfo getToken(String key) throws Exception;

    /**
     * Retrieves the {@code TokenHandler} associated with the specified key.
     * The {@code TokenHandler} manages the lifecycle of the token, including encryption, storage, and other operations.
     *
     * @param key The key identifying the token to retrieve the handler for.
     * @return The {@code TokenHandler} associated with the specified key.
     * @throws Exception If an error occurs while retrieving the token handler.
     */
    TokenHandler getTokenHandle(String key) throws Exception;
}
