package com.example.patientcare.dto.response;

public class TokenRefreshResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";

    // Constructors
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

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}