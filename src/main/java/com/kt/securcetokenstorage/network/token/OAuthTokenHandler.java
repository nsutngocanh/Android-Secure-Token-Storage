package com.kt.securcetokenstorage.network.token;

import android.util.Log;

public class OAuthTokenHandler implements TokenHandler {

    private final OauthTokenInfo info;

    private final OAuthTokenAction action;

    public OAuthTokenHandler(OauthTokenInfo info, OAuthTokenAction action) {
        this.action = action;
        this.info = info;
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            String[] tokenParts = token.split("\\.");
            if (tokenParts.length < 2) {
                return false;
            }

            String payload = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                payload = new String(java.util.Base64.getDecoder().decode(tokenParts[1]));
            } else {
                throw new Exception("This Build-In For SDK >= 26");
            }
            org.json.JSONObject jsonObject = new org.json.JSONObject(payload);

            if (jsonObject.has("exp")) {
                long expiryTime = jsonObject.getLong("exp");
                long currentTime = System.currentTimeMillis() / 1000;

                return currentTime < expiryTime;
            }
        } catch (Exception e) {
            Log.e("Error validating token: ", e.getMessage());
        }
        return false;
    }

    @Override
    public String refresh() {
        return action.whenTokenExpired(this.info);
    }

    public interface OAuthTokenAction {
        String whenTokenExpired(OauthTokenInfo info);
    }
}
