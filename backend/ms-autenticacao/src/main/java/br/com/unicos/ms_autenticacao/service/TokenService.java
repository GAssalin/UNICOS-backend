package br.com.unicos.ms_autenticacao.service;

import br.com.unicos.core.tenant.exception.TenantNotAssociatedException;
import br.com.unicos.ms_autenticacao.dto.token.TokenUserData;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public String gerarAccessToken(TokenUserData user) {

        if (user.tenantId() == null)
            throw new TenantNotAssociatedException();

        Algorithm algorithm = Algorithm.HMAC256(segredo);

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.username())
                .withClaim("roles", user.roles())
                .withClaim("usuarioId", user.userId())
                .withClaim("tenantId", user.tenantId())
                .withClaim("typ", "access")
                .withExpiresAt(expiracao(tempoExpToken))
                .sign(algorithm);
    }

    public String gerarRefreshToken(Long userId) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(segredo);

            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(userId.toString())
                    .withClaim("typ", "refresh")
                    .withJWTId(UUID.randomUUID().toString())
                    .withExpiresAt(expiracao(tempoExpRefreshToken))
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Erro ao gerar token refresh JWT!", exception);
        }
    }

    public DecodedJWT verificarAccessToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(segredo);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withClaim("typ", "access")
                    .build();

            return verifier.verify(token);

        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token inválido ou expirado", exception);
        }
    }

    public DecodedJWT verificarRefreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(segredo);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withClaim("typ", "refresh")
                    .build();

            return verifier.verify(token);

        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token inválido ou expirado", exception);
        }
    }

    private Instant expiracao(Integer minutos) {
        return Instant.now().plusSeconds(minutos * 60L);
    }
}
