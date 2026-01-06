package br.com.unicos.ms_auth.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_auth.dto.login.DadosLogin;
import br.com.unicos.ms_auth.dto.token.DadosRefreshToken;
import br.com.unicos.ms_auth.dto.token.DadosToken;
import br.com.unicos.ms_auth.dto.token.TokenUserData;
import br.com.unicos.ms_auth.loader.AuthAuthenticationLoader;
import br.com.unicos.ms_auth.repository.RoleUsuarioRepository;
import br.com.unicos.ms_auth.security_access.AuthenticatedUser;
import br.com.unicos.ms_auth.security_access.TokenService;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final AuthAuthenticationLoader authenticationLoader;
    private final TokenService tokenService;
    private final RoleUsuarioRepository roleUsuarioRepository;

    // ============================================================
    // LOGIN
    // ============================================================
    @CircuitBreaker(name = "auth-login", fallbackMethod = "fallbackLogin")
    public ResponseEntity<DadosToken> autenticar(DadosLogin dados) {
        AuthenticatedUser user;

        try {
            user = authenticationLoader.authenticate(dados.email(), dados.senha());
        } catch (DisabledException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }

        TokenUserData tokenUser = new TokenUserData(
                user.getUserId(),
                user.getUsername(),
                user.getEmpresaId(),
                new ArrayList<>(roleUsuarioRepository.findRolesByUsuario(
                        user.getUserId(),
                        user.getEmpresaId()
                ))
        );

        String accessToken = tokenService.gerarAccessToken(tokenUser);
        String refreshToken = tokenService.gerarRefreshToken(user.getUserId());

        return ResponseEntity.ok(new DadosToken(accessToken, refreshToken));
    }

    /**
     * Fallback acionado SOMENTE para falhas técnicas
     * (ex.: timeout, ms-pessoas fora, DB indisponível).
     */
    private ResponseEntity<DadosToken> fallbackLogin(DadosLogin dados, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de autenticação temporariamente indisponível"
        );
    }

    // ============================================================
    // REFRESH TOKEN
    // ============================================================

    public ResponseEntity<DadosToken> atualizarToken(@Valid DadosRefreshToken dados) {
        DecodedJWT jwt;

        try {
            jwt = tokenService.verificarRefreshToken(dados.refreshToken());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido ou expirado");
        }

        Long userId = Long.valueOf(jwt.getSubject());
        Long tenantId = TenantContext.getEmpresaId();

        TokenUserData tokenUser = new TokenUserData(
                userId,
                null,
                tenantId,
                List.of()
        );

        String novoAccessToken = tokenService.gerarAccessToken(tokenUser);
        String novoRefreshToken = tokenService.gerarRefreshToken(userId);

        return ResponseEntity.ok(new DadosToken(novoAccessToken, novoRefreshToken));
    }
}
