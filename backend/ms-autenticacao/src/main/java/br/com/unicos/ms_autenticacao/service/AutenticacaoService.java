package br.com.unicos.ms_autenticacao.service;

import br.com.unicos.ms_autenticacao.dto.login.DadosLogin;
import br.com.unicos.ms_autenticacao.dto.token.DadosRefreshToken;
import br.com.unicos.ms_autenticacao.dto.token.DadosToken;
import br.com.unicos.ms_autenticacao.dto.token.TokenUserData;
import br.com.unicos.ms_autenticacao.loader.AutenticacaoLoader;
import br.com.unicos.ms_autenticacao.model.AuthenticatedUser;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AutenticacaoService {

    private final AutenticacaoLoader autenticacaoLoader;
    private final TokenService tokenService;

    @CircuitBreaker(name = "autenticacao-login", fallbackMethod = "fallbackLogin")
    public DadosToken autenticar(DadosLogin dados) {
        try {
            AuthenticatedUser user = autenticacaoLoader.authenticate(dados.email(), dados.senha());

            TokenUserData tokenUser = new TokenUserData(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmpresaId()
            );

            String accessToken = tokenService.gerarAccessToken(tokenUser);
            String refreshToken = tokenService.gerarRefreshToken(tokenUser);

            return new DadosToken(accessToken, refreshToken);

        } catch (DisabledException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }
    }

    public DadosToken atualizarToken(@Valid DadosRefreshToken dados) {
        try {
            DecodedJWT jwt = tokenService.verificarRefreshToken(dados.refreshToken());

            TokenUserData tokenUser = new TokenUserData(
                    jwt.getClaim("usuarioId").asLong(),
                    jwt.getClaim("username").asString(),
                    jwt.getClaim("tenantId").asLong()
            );

            String novoAccessToken = tokenService.gerarAccessToken(tokenUser);
            String novoRefreshToken = tokenService.gerarRefreshToken(tokenUser);

            return new DadosToken(novoAccessToken, novoRefreshToken);

        } catch (JWTVerificationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido ou expirado");
        }
    }

    private DadosToken fallbackLogin(DadosLogin dados, Throwable ex) {
        log.error(
                "Fallback do CircuitBreaker acionado no login para o email [{}]. Causa: {}",
                dados.email(),
                ex.getMessage(),
                ex
        );

        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de autenticação temporariamente indisponível"
        );
    }
}