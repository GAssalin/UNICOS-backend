package br.com.unicos.core.auth.interno;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenInternoServiceTest {

    private final TokenInternoService service = new TokenInternoService("token-interno-de-teste");

    @Test
    void deveAceitarSomenteTokenIdentico() {
        assertThat(service.isValido("token-interno-de-teste")).isTrue();
        assertThat(service.isValido("token-interno-de-test")).isFalse();
        assertThat(service.isValido("TOKEN-INTERNO-DE-TESTE")).isFalse();
        assertThat(service.isValido("")).isFalse();
        assertThat(service.isValido(null)).isFalse();
    }
}
