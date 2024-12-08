package com.kt.securcetokenstorage.network.token;

import java.util.Objects;

public class TokenInfo {
    private String token;
    private String key;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TokenInfo() {
    }

    public TokenInfo(String token, String key) {
        this.token = token;
        this.key = key;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TokenInfo tokenInfo = (TokenInfo) obj;
        return Objects.equals(key, tokenInfo.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
