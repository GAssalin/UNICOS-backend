package br.com.unicos.core.auth;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthContextInitializer {
    void initialize(HttpServletRequest request);
}