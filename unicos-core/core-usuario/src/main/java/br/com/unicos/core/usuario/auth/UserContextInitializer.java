package br.com.unicos.core.usuario.auth;

import jakarta.servlet.http.HttpServletRequest;

public interface UserContextInitializer {
    void initialize(HttpServletRequest request);
}