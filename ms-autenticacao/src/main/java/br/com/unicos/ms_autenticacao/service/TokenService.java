package br.com.unicos.ms_autenticacao.service;

import br.com.unicos.core.auth.model.AuthenticatedUser;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.ms_autenticacao.dto.login.DadosLoginDto;
import br.com.unicos.ms_autenticacao.dto.token.DadosTokenDto;
import br.com.unicos.ms_autenticacao.dto.token.TokenUserDataDto;
import br.com.unicos.ms_autenticacao.loader.AutenticacaoLoader;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${jwt.secret}")
    private String segredo;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.tempo.exp.token}")
    private Integer tempoExpToken;

    @Value("${jwt.tempo.exp.refresh.token}")
    private Integer tempoExpRefreshToken;

    private final AutenticacaoLoader autenticacaoLoader;
    private final TokenCoreService tokenCoreService;

    public DadosTokenDto autenticar(DadosLoginDto dados) {
        try {
            AuthenticatedUser user = autenticacaoLoader.authenticate(dados.email(), dados.senha());

            TokenUserDataDto tokenUser = new TokenUserDataDto(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmpresaId()
                    );

            return new DadosTokenDto(gerarAccessToken(tokenUser), gerarRefreshToken(tokenUser));
        } catch (DisabledException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }
    }

    private String gerarAccessToken(TokenUserDataDto user) {
        validarTokenUserData(user);

        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withClaim("username", user.username())
                    .withClaim("usuarioId", user.userId())
                    .withClaim("tenantId", user.tenantId())
                    .withClaim("typ", "access")
                    .withExpiresAt(Instant.now().plusSeconds(tempoExpToken.longValue() * 60))
                    .sign(tokenCoreService.getAlgorithm());
        } catch (JWTCreationException ex) {
            throw new JWTCreationException("Erro ao gerar access token JWT", ex);
        }
    }

    private void validarTokenUserData(TokenUserDataDto user) {
        if (user == null)
            throw new IllegalArgumentException("Usuário inválido para geração do token");
    }

    public String gerarRefreshToken(TokenUserDataDto user) {
        validarTokenUserData(user);

        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.userId().toString())
                    .withClaim("username", user.username())
                    .withClaim("usuarioId", user.userId())
                    .withClaim("tenantId", user.tenantId())
                    .withClaim("typ", "refresh")
                    .withJWTId(UUID.randomUUID().toString())
                    .withExpiresAt(calcularExpiracao(tempoExpRefreshToken))
                    .sign(tokenCoreService.getAlgorithm());
        } catch (JWTCreationException ex) {
            throw new JWTCreationException("Erro ao gerar refresh token JWT", ex);
        }
    }

    private Instant calcularExpiracao(Integer minutos) {
        return Instant.now().plusSeconds(minutos.longValue() * 60);
    }
}