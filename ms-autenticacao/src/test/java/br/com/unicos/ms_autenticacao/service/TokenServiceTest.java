package br.com.unicos.ms_autenticacao.service;

import br.com.unicos.core.auth.model.AuthenticatedUser;
import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.core.usuario.dto.UsuarioAuthResponse;
import br.com.unicos.ms_autenticacao.dto.login.DadosLoginDto;
import br.com.unicos.ms_autenticacao.dto.token.DadosTokenDto;
import br.com.unicos.ms_autenticacao.loader.AutenticacaoLoader;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenServiceTest {

    private static final String SEGREDO = "segredo-de-teste-com-mais-de-32-caracteres";
    private static final String ISSUER = "unicos-teste";

    private final TokenCoreService tokenCoreService = new TokenCoreService(SEGREDO, ISSUER);
    private final AutenticacaoLoader autenticacaoLoader = mock(AutenticacaoLoader.class);
    private final UsuarioService usuarioService = mock(UsuarioService.class);

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(ISSUER, 15, 1440, autenticacaoLoader, usuarioService, tokenCoreService);
    }

    @Test
    void deveGerarAccessERefreshTokensValidosNoLogin() {
        when(autenticacaoLoader.authenticate("admin@unicos.com", "senha"))
                .thenReturn(new AuthenticatedUser(1L, "admin", "hash", 7L));

        DadosTokenDto tokens = tokenService.autenticar(new DadosLoginDto("admin@unicos.com", "senha"));

        DecodedJWT access = tokenCoreService.validarToken("Bearer " + tokens.tokenAccess());
        DecodedJWT refresh = tokenCoreService.validarRefreshToken(tokens.refreshToken());

        assertThat(access.getClaim(JwtClaims.USUARIO_ID).asLong()).isEqualTo(1L);
        assertThat(access.getClaim(JwtClaims.TENANT_ID).asLong()).isEqualTo(7L);
        assertThat(refresh.getClaim(JwtClaims.USUARIO_ID).asLong()).isEqualTo(1L);
    }

    @Test
    void deveTraduzirFalhasDeLoginEmStatusHttp() {
        when(autenticacaoLoader.authenticate("x@unicos.com", "errada"))
                .thenThrow(new BadCredentialsException("x"));
        when(autenticacaoLoader.authenticate("inativo@unicos.com", "senha"))
                .thenThrow(new DisabledException("Usuário desabilitado"));

        assertStatus(() -> tokenService.autenticar(new DadosLoginDto("x@unicos.com", "errada")), HttpStatus.UNAUTHORIZED);
        assertStatus(() -> tokenService.autenticar(new DadosLoginDto("inativo@unicos.com", "senha")), HttpStatus.FORBIDDEN);
    }

    @Test
    void deveRenovarTokensComRefreshTokenValido() {
        DadosTokenDto tokens = loginComo(1L, 7L);
        when(usuarioService.buscarUsuarioPorId(1L))
                .thenReturn(new UsuarioAuthResponse(1L, "admin", "hash", 7L, true));

        DadosTokenDto renovados = tokenService.atualizarToken(tokens.refreshToken());

        assertThat(tokenCoreService.validarToken("Bearer " + renovados.tokenAccess())).isNotNull();
        assertThat(renovados.refreshToken()).isNotEqualTo(tokens.refreshToken());
    }

    @Test
    void naoDeveRenovarParaUsuarioDesativadoOuDeOutraEmpresa() {
        DadosTokenDto tokens = loginComo(1L, 7L);

        when(usuarioService.buscarUsuarioPorId(1L))
                .thenReturn(new UsuarioAuthResponse(1L, "admin", "hash", 7L, false));
        assertStatus(() -> tokenService.atualizarToken(tokens.refreshToken()), HttpStatus.FORBIDDEN);

        when(usuarioService.buscarUsuarioPorId(1L))
                .thenReturn(new UsuarioAuthResponse(1L, "admin", "hash", 99L, true));
        assertStatus(() -> tokenService.atualizarToken(tokens.refreshToken()), HttpStatus.UNAUTHORIZED);
    }

    @Test
    void naoDeveAceitarAccessTokenComoRefreshToken() {
        DadosTokenDto tokens = loginComo(1L, 7L);

        assertStatus(() -> tokenService.atualizarToken(tokens.tokenAccess()), HttpStatus.UNAUTHORIZED);
        assertStatus(() -> tokenService.atualizarToken("token-invalido"), HttpStatus.UNAUTHORIZED);
    }

    private DadosTokenDto loginComo(Long usuarioId, Long empresaId) {
        when(autenticacaoLoader.authenticate("u@unicos.com", "senha"))
                .thenReturn(new AuthenticatedUser(usuarioId, "u", "hash", empresaId));
        return tokenService.autenticar(new DadosLoginDto("u@unicos.com", "senha"));
    }

    private static void assertStatus(Runnable acao, HttpStatus status) {
        assertThatThrownBy(acao::run)
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(status));
    }
}
