package br.com.unicos.ms_autenticacao.dto.token;

import java.util.List;

public record TokenUserData(
        Long userId,
        String username,
        Long tenantId,
        List<String> roles
) {}