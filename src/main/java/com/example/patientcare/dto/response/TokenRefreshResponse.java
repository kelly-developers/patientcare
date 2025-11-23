// TokenRefreshResponse.java
package com.example.patientcare.dto.response;

public class TokenRefreshResponse {
    private String token;
    private String refreshToken;

    public TokenRefreshResponse() {}

    public TokenRefreshResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}