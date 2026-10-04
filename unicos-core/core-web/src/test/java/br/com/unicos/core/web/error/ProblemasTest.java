package br.com.unicos.core.web.error;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class ProblemasTest {

    @Test
    void deveExporMensagemDeExcecaoLancadaPelaPlataforma() {
        IllegalArgumentException ex = new IllegalArgumentException("Já existe um estoque com o código informado.");

        assertThat(Problemas.lancadaPelaAplicacao(ex)).isTrue();
        assertThat(Problemas.detalheSeguro(ex, "padrão")).isEqualTo("Já existe um estoque com o código informado.");
    }

    @Test
    void deveOcultarMensagemDeExcecaoLancadaPorBiblioteca() {
        IllegalArgumentException ex = catchThrowableOfType(IllegalArgumentException.class,
                () -> TimeUnit.valueOf("INEXISTENTE"));

        assertThat(ex.getMessage()).contains("java.util.concurrent.TimeUnit");
        assertThat(Problemas.lancadaPelaAplicacao(ex)).isFalse();
        assertThat(Problemas.detalheSeguro(ex, "padrão")).isEqualTo("padrão");
    }
}
