package br.com.unicos.ms_funcionario.filter;

import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.ms_funcionario.client.PermissaoService;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class FuncionarioRequestFilterTest {

    private final FuncionarioRequestFilter filter = new FuncionarioRequestFilter(
            new TokenCoreService("segredo-de-teste-com-mais-de-32-caracteres", "unicos-teste"),
            new TokenInternoService("token-interno-de-teste"),
            mock(PermissaoService.class)
    );

    @Test
    void deveDiferenciarCargosDeFuncionarios() {
        assertThat(filter.resolverPermissao("GET", "/v1/funcionarios")).isEqualTo("FUNCIONARIO_LISTAR");
        assertThat(filter.resolverPermissao("GET", "/v1/funcionarios/3/subordinados")).isEqualTo("FUNCIONARIO_LISTAR");
        assertThat(filter.resolverPermissao("PUT", "/v1/funcionarios/3")).isEqualTo("FUNCIONARIO_EDITAR");
        assertThat(filter.resolverPermissao("POST", "/v1/funcionarios/cargos")).isEqualTo("FUNCIONARIO_CARGO_CRIAR");
        assertThat(filter.resolverPermissao("DELETE", "/v1/funcionarios/cargos/2")).isEqualTo("FUNCIONARIO_CARGO_EXCLUIR");
    }

    @Test
    void consultaDoProprioCadastroExigeApenasAutenticacao() {
        assertThat(filter.resolverPermissao("GET", "/v1/funcionarios/me")).isNull();
        assertThat(filter.resolverPermissao("HEAD", "/v1/funcionarios/me")).isNull();
        assertThat(filter.resolverPermissao("PUT", "/v1/funcionarios/me")).isEqualTo("FUNCIONARIO_EDITAR");
        assertThat(filter.resolverPermissao("GET", "/v1/funcionarios/me/qualquer")).isEqualTo("FUNCIONARIO_LISTAR");
    }

    @Test
    void naoDeveExigirPermissaoParaRotasNaoMapeadas() {
        assertThat(filter.resolverPermissao("GET", "/actuator/health")).isNull();
        assertThat(filter.resolverPermissao("GET", "/v1/funcionariosx")).isNull();
    }

    @Test
    void naoDeveLiberarMetodosSemPermissaoCorrespondenteEmRecursosProtegidos() {
        assertThat(filter.resolverPermissao("HEAD", "/v1/funcionarios")).isEqualTo("FUNCIONARIO_LISTAR");
        assertThatThrownBy(() -> filter.resolverPermissao("OPTIONS", "/v1/funcionarios"))
                .isInstanceOf(AccessDeniedException.class);
    }
}
