package br.com.unicos.ms_autenticacao.service;

import br.com.unicos.core.tenant.exception.TenantNotAssociatedException;
import br.com.unicos.ms_autenticacao.dto.token.TokenUserData;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String segredo;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.tempo.exp.token}")
    private Integer tempoExpToken;

    @Value("${jwt.tempo.exp.refresh.token}")
    private Integer tempoExpRefreshToken;

    public String gerarAccessToken(TokenUserData user) {
        validarTokenUserData(user);

        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.userId().toString())
                    .withClaim("username", user.username())
                    .withClaim("usuarioId", user.userId())
                    .withClaim("tenantId", user.tenantId())
                    .withClaim("typ", "access")
                    .withExpiresAt(calcularExpiracao(tempoExpToken))
                    .sign(getAlgorithm());
        } catch (JWTCreationException ex) {
            throw new JWTCreationException("Erro ao gerar access token JWT", ex);
        }
    }

    public String gerarRefreshToken(TokenUserData user) {
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
                    .sign(getAlgorithm());
        } catch (JWTCreationException ex) {
            throw new JWTCreationException("Erro ao gerar refresh token JWT", ex);
        }
    }

    public DecodedJWT verificarAccessToken(String token) {
        return buildVerifier("access").verify(token);
    }

    public DecodedJWT verificarRefreshToken(String token) {
        return buildVerifier("refresh").verify(token);
    }

    private void validarTokenUserData(TokenUserData user) {
        if (user == null || user.userId() == null) {
            throw new IllegalArgumentException("Usuário inválido para geração do token");
        }

        if (user.tenantId() == null) {
            throw new TenantNotAssociatedException();
        }
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(segredo);
    }

    private JWTVerifier buildVerifier(String type) {
        return JWT.require(getAlgorithm())
                .withIssuer(issuer)
                .withClaim("typ", type)
                .build();
    }

    private Instant calcularExpiracao(Integer minutos) {
        return Instant.now().plusSeconds(minutos.longValue() * 60);
    }
}