package com.kt.securcetokenstorage.network.token;

public class OauthTokenInfo {
    private String clientId;
    private String clientSecret;
    private String refreshToken;
    private String tokenEndpoint;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    public OauthTokenInfo() {
    }

    public OauthTokenInfo(String clientId, String clientSecret, String refreshToken, String tokenEndpoint) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.refreshToken = refreshToken;
        this.tokenEndpoint = tokenEndpoint;
    }
}
