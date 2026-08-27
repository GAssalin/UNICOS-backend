package br.com.unicos.core.usuario;

import jakarta.servlet.http.HttpServletRequest;

public interface UserContextInitializer {
    void initialize(HttpServletRequest request);
}