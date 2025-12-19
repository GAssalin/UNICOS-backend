package br.com.unicos.ms_auth.dto.token;

import java.util.List;

public record TokenUserData(
        Long userId,
        String username,
        Long tenantId,
        List<String> roles
) {}