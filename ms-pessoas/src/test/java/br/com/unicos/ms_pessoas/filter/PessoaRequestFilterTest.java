package br.com.unicos.ms_pessoas.filter;

import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.ms_pessoas.client.PermissaoService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class PessoaRequestFilterTest {

    private final PessoaRequestFilter filter = new PessoaRequestFilter(
            new TokenCoreService("segredo-de-teste-com-mais-de-32-caracteres", "unicos-teste"),
            new TokenInternoService("token-interno-de-teste"),
            mock(PermissaoService.class)
    );

    @Test
    void deveDiferenciarRecursosComPrefixoComum() {
        assertThat(filter.resolverPermissao("GET", "/v1/pessoas")).isEqualTo("PESSOA_LISTAR");
        assertThat(filter.resolverPermissao("GET", "/v1/pessoas/1")).isEqualTo("PESSOA_LISTAR");
        assertThat(filter.resolverPermissao("POST", "/v1/pessoas-fisicas")).isEqualTo("PESSOA_FISICA_CRIAR");
        assertThat(filter.resolverPermissao("PUT", "/v1/pessoas-juridicas/3")).isEqualTo("PESSOA_JURIDICA_EDITAR");
        assertThat(filter.resolverPermissao("DELETE", "/v1/pessoas-relacoes/9")).isEqualTo("PESSOA_RELACAO_EXCLUIR");
    }

    @Test
    void deveUsarPermissoesDedicadasParaAtivarEDesativarUsuario() {
        assertThat(filter.resolverPermissao("PATCH", "/v1/usuarios/5/ativar")).isEqualTo("USUARIO_ATIVAR");
        assertThat(filter.resolverPermissao("PATCH", "/v1/usuarios/5/desativar")).isEqualTo("USUARIO_DESATIVAR");
        assertThat(filter.resolverPermissao("PUT", "/v1/usuarios/5")).isEqualTo("USUARIO_EDITAR");
    }

    @Test
    void naoDeveExigirPermissaoParaRotasNaoMapeadas() {
        assertThat(filter.resolverPermissao("GET", "/actuator/health")).isNull();
        assertThat(filter.resolverPermissao("GET", "/v1/pessoasx")).isNull();
        assertThat(filter.resolverPermissao("OPTIONS", "/v1/pessoas")).isNull();
    }
}
