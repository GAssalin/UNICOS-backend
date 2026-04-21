package br.com.unicos.core.produto;

import jakarta.servlet.http.HttpServletRequest;

public interface ProductContextInitializer {
    void initialize(HttpServletRequest request);
}