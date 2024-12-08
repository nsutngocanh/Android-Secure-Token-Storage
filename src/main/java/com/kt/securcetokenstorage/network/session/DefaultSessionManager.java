package com.kt.securcetokenstorage.network.session;

import android.util.Log;

import com.kt.securcetokenstorage.constraints.StorageConst;
import com.kt.securcetokenstorage.network.token.TokenHandler;
import com.kt.securcetokenstorage.network.token.TokenInfo;
import com.kt.securcetokenstorage.storage.SecureTokenManager;

import java.util.HashMap;
import java.util.Map;

public class DefaultSessionManager implements SessionManager {
    private final Map<TokenInfo, TokenHandler> tokenHandlerMap = new HashMap<>();

    private TokenHandler getTokenHandlerByKey(String key) {
        for (TokenInfo tokenInfo : tokenHandlerMap.keySet()) {
            if (tokenInfo.getKey().equals(key)) {
                return tokenHandlerMap.get(tokenInfo);
            }
        }
        return null;
    }

    private TokenInfo getTokenInfoByKey(String key) {
        for (TokenInfo tokenInfo : tokenHandlerMap.keySet()) {
            if (tokenInfo.getKey().equals(key)) {
                return tokenInfo;
            }
        }
        return null;
    }

    @Override
    public void refreshToken(String key) throws Exception {
        TokenHandler tokenHandler = getTokenHandlerByKey(key);
        if (tokenHandler == null) throw new Exception("No token found by key : " + key);
        String newToken = tokenHandler.refresh();
        this.setToken(key, newToken, tokenHandler);
    }

    @Override
    public void setToken(String key, String token, TokenHandler tokenHandler) {
        TokenInfo tokenInfo = getTokenInfoByKey(key);
        if (tokenInfo != null) {
            tokenInfo.setToken(token);
            tokenHandlerMap.put(tokenInfo, tokenHandler);
        } else {
            TokenInfo newToken = new TokenInfo(token, key);
            tokenHandlerMap.put(newToken, tokenHandler);
        }
    }

    @Override
    public void handleAnomaly(SecureTokenManager secureTokenManager, String key) {
        TokenInfo tokenInfo = getTokenInfoByKey(key);
        if (tokenInfo != null) {
            tokenHandlerMap.remove(tokenInfo);
            secureTokenManager.deleteToken(key);
        } else {
            Log.d(StorageConst.LOG_TAG, "No token found for the provided key: " + key);
        }
    }

    @Override
    public boolean isTokenValid(String key) throws Exception {
        TokenInfo tokenInfo = getTokenInfoByKey(key);
        TokenHandler tokenHandler = getTokenHandlerByKey(key);
        if (tokenInfo == null || tokenHandler == null) {
            throw new Exception("Token not found by key : " + key);
        }
        return tokenHandler.isTokenValid(tokenInfo.getToken());
    }

    @Override
    public void addToken(TokenInfo tokenInfo, TokenHandler tokenHandler) {
        tokenHandlerMap.put(tokenInfo, tokenHandler);
    }

    @Override
    public boolean removeToken(String key) {
        TokenInfo tokenInfo = getTokenInfoByKey(key);
        if (tokenInfo != null) {
            tokenHandlerMap.remove(tokenInfo);
            return true;
        } else {
            Log.d(StorageConst.LOG_TAG, "No token found for the provided key: " + key);
        }
        return false;
    }

    @Override
    public TokenInfo getToken(String key) {
        return getTokenInfoByKey(key);
    }

    @Override
    public TokenHandler getTokenHandle(String key) {
        return getTokenHandlerByKey(key);
    }
}
