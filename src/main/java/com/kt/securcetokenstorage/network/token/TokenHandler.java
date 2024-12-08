package com.kt.securcetokenstorage.network.token;

/**
 * The {@code TokenHandler} interface defines methods for managing and validating tokens.
 * Implementing classes should handle the validation of tokens and provide functionality for refreshing tokens.
 */
public interface TokenHandler {

    /**
     * Validates the given token.
     * This method should perform checks to ensure the token is valid, such as verifying its integrity,
     * expiration, and other security criteria.
     *
     * @param token The token to validate.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    boolean isTokenValid(String token);

    /**
     * Refreshes the current token and returns the new token.
     * This method is typically used to obtain a new token when the current one is near expiration or invalid.
     *
     * @return The refreshed token string.
     */
    String refresh();
}
