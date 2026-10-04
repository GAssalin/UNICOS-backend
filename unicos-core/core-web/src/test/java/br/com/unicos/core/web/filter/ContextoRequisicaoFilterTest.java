package br.com.unicos.core.web.filter;

import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.usuario.context.UserContext;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class ContextoRequisicaoFilterTest {

    private static final String SEGREDO = "segredo-de-teste-com-mais-de-32-caracteres";
    private static final String ISSUER = "unicos-teste";
    private static final String TOKEN_INTERNO = "token-interno-de-teste";

    private final TokenCoreService tokenCoreService = new TokenCoreService(SEGREDO, ISSUER);
    private final TokenInternoService tokenInternoService = new TokenInternoService(TOKEN_INTERNO);

    @Test
    void deveBloquearCaminhoInternoSemTokenInterno() throws Exception {
        FiltroDeTeste filtro = new FiltroDeTeste(true);
        MockHttpServletResponse response = new MockHttpServletResponse();
        ChainRegistradora chain = new ChainRegistradora();

        filtro.doFilter(request("GET", "/internal/auth/by-email"), response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getContentType()).startsWith("application/problem+json");
        assertThat(chain.chamada).isFalse();
    }

    @Test
    void deveBloquearCaminhoInternoCodificadoSemTokenInterno() throws Exception {
        FiltroDeTeste filtro = new FiltroDeTeste(true);
        MockHttpServletResponse response = new MockHttpServletResponse();
        ChainRegistradora chain = new ChainRegistradora();

        filtro.doFilter(request("GET", "/%69nternal/auth/by-email"), response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(chain.chamada).isFalse();
    }

    @Test
    void devePermitirCaminhoInternoComTokenInterno() throws Exception {
        FiltroDeTeste filtro = new FiltroDeTeste(true);
        MockHttpServletRequest request = request("GET", "/internal/auth/by-email");
        request.addHeader(TokenInternoService.HEADER, TOKEN_INTERNO);
        ChainRegistradora chain = new ChainRegistradora();

        filtro.doFilter(request, new MockHttpServletResponse(), chain);

        assertThat(chain.chamada).isTrue();
        assertThat(filtro.permissoesVerificadas).isEmpty();
    }

    @Test
    void deveRejeitarTokenInvalidoCom401() throws Exception {
        FiltroDeTeste filtro = new FiltroDeTeste(true);
        MockHttpServletRequest request = request("GET", "/v1/recursos");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer token-invalido");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ChainRegistradora chain = new ChainRegistradora();

        filtro.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(chain.chamada).isFalse();
    }

    @Test
    void deveDefinirContextoAPartirDoJwtEIgnorarHeadersDeIdentidadeDoCliente() throws Exception {
        FiltroDeTeste filtro = new FiltroDeTeste(true);
        MockHttpServletRequest request = request("GET", "/v1/recursos/1");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken(10L, 20L));
        request.addHeader("X-Usuario-Id", "999");
        request.addHeader("X-Tenant-Id", "999");

        AtomicReference<Long> usuario = new AtomicReference<>();
        AtomicReference<Long> empresa = new AtomicReference<>();
        AtomicReference<Object> principal = new AtomicReference<>();
        FilterChain chain = (req, res) -> {
            usuario.set(UserContext.getUsuarioId());
            empresa.set(TenantContext.getEmpresaId());
            principal.set(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        };

        filtro.doFilter(request, new MockHttpServletResponse(), chain);

        assertThat(usuario.get()).isEqualTo(10L);
        assertThat(empresa.get()).isEqualTo(20L);
        assertThat(principal.get()).isEqualTo(10L);
        assertThat(filtro.permissoesVerificadas).containsExactly("RECURSO_LISTAR");
    }

    @Test
    void deveLimparContextosAoFinal() throws Exception {
        FiltroDeTeste filtro = new FiltroDeTeste(true);
        MockHttpServletRequest request = request("GET", "/v1/recursos");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken(10L, 20L));

        filtro.doFilter(request, new MockHttpServletResponse(), new ChainRegistradora());

        assertThat(UserContext.isUsuarioDefined()).isFalse();
        assertThat(TenantContext.isEmpresaDefined()).isFalse();
        assertThat(AuthContext.isTokenDefined()).isFalse();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void deveRetornar403QuandoUsuarioNaoPossuiPermissao() throws Exception {
        FiltroDeTeste filtro = new FiltroDeTeste(false);
        MockHttpServletRequest request = request("DELETE", "/v1/recursos/1");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken(10L, 20L));
        MockHttpServletResponse response = new MockHttpServletResponse();
        ChainRegistradora chain = new ChainRegistradora();

        filtro.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(filtro.permissoesVerificadas).containsExactly("RECURSO_EXCLUIR");
        assertThat(chain.chamada).isFalse();
    }

    @Test
    void deveRepassarStatusQuandoServicoDePermissaoFalhar() throws Exception {
        FiltroDeTeste filtro = new FiltroDeTeste(true) {
            @Override
            protected boolean usuarioPossuiPermissao(String permissao) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "indisponível");
            }
        };
        MockHttpServletRequest request = request("GET", "/v1/recursos");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken(10L, 20L));
        MockHttpServletResponse response = new MockHttpServletResponse();

        filtro.doFilter(request, response, new ChainRegistradora());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
        assertThat(response.getContentAsString()).contains("indisponível");
    }

    @Test
    void deveSeguirAnonimoQuandoNaoHaToken() throws Exception {
        FiltroDeTeste filtro = new FiltroDeTeste(true);
        ChainRegistradora chain = new ChainRegistradora();
        MockHttpServletRequest request = request("GET", "/v1/recursos");
        request.addHeader("X-Usuario-Id", "1");
        request.addHeader("X-Tenant-Id", "1");

        filtro.doFilter(request, new MockHttpServletResponse(), chain);

        assertThat(chain.chamada).isTrue();
        assertThat(chain.autenticado).isFalse();
        assertThat(filtro.permissoesVerificadas).isEmpty();
    }

    @Test
    void deveIdentificarCaminhosInternos() {
        assertThat(ContextoRequisicaoFilter.isCaminhoInterno("/internal")).isTrue();
        assertThat(ContextoRequisicaoFilter.isCaminhoInterno("/internal/x")).isTrue();
        assertThat(ContextoRequisicaoFilter.isCaminhoInterno("/internalx")).isFalse();
        assertThat(ContextoRequisicaoFilter.isCaminhoInterno("/v1/internal/x")).isFalse();
    }

    private static MockHttpServletRequest request(String metodo, String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest(metodo, uri);
        request.setServletPath(uri);
        return request;
    }

    private static String accessToken(Long usuarioId, Long empresaId) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim(JwtClaims.TIPO, JwtClaims.TIPO_ACCESS)
                .withClaim(JwtClaims.USUARIO_ID, usuarioId)
                .withClaim(JwtClaims.TENANT_ID, empresaId)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .sign(Algorithm.HMAC256(SEGREDO));
    }

    private class FiltroDeTeste extends ContextoRequisicaoFilter {

        private final boolean possuiPermissao;
        private final List<String> permissoesVerificadas = new ArrayList<>();

        FiltroDeTeste(boolean possuiPermissao) {
            super(tokenCoreService, tokenInternoService);
            this.possuiPermissao = possuiPermissao;
        }

        @Override
        protected String resolverPermissao(String metodoHttp, String path) {
            if (!path.startsWith("/v1/recursos"))
                return null;
            return "GET".equals(metodoHttp) ? "RECURSO_LISTAR" : "RECURSO_EXCLUIR";
        }

        @Override
        protected boolean usuarioPossuiPermissao(String permissao) {
            permissoesVerificadas.add(permissao);
            return possuiPermissao;
        }
    }

    private static class ChainRegistradora implements FilterChain {

        private boolean chamada;
        private boolean autenticado;

        @Override
        public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response) {
            chamada = true;
            autenticado = SecurityContextHolder.getContext().getAuthentication() != null;
        }
    }
}
