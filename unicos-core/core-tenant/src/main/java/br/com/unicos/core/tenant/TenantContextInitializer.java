package br.com.unicos.core.tenant;

import jakarta.servlet.http.HttpServletRequest;

public interface TenantContextInitializer {
    void initialize(HttpServletRequest request);
}