package br.com.unicos.core.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.RegisteredClaims;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Validação dos tokens JWT emitidos pelo ms-autenticacao.
 *
 * <p>
 * Compartilhado pelo gateway e pelos microserviços, garantindo que todos verifiquem
 * assinatura, emissor, tipo e as claims de identidade da mesma forma.
 * </p>
 */
public class TokenCoreService {

    private static final String BEARER_PREFIX = "Bearer ";

    private final Algorithm algorithm;
    private final JWTVerifier accessTokenVerifier;
    private final JWTVerifier refreshTokenVerifier;

    public TokenCoreService(String segredo, String issuer) {
        this.algorithm = Algorithm.HMAC256(segredo);
        this.accessTokenVerifier = criarVerifier(issuer, JwtClaims.TIPO_ACCESS);
        this.refreshTokenVerifier = criarVerifier(issuer, JwtClaims.TIPO_REFRESH);
    }

    /**
     * Valida o header {@code Authorization} ({@code Bearer <token>}) contendo um access token.
     *
     * @throws JWTVerificationException quando o header ou o token forem inválidos
     */
    public DecodedJWT validarToken(String authorizationHeader) {
        DecodedJWT jwt = accessTokenVerifier.verify(extrairToken(authorizationHeader));
        validarIdentidade(jwt);
        return jwt;
    }

    /**
     * Valida um refresh token (sem o prefixo {@code Bearer}).
     *
     * @throws JWTVerificationException quando o token for inválido
     */
    public DecodedJWT validarRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank())
            throw new JWTVerificationException("Refresh token não informado.");

        DecodedJWT jwt = refreshTokenVerifier.verify(refreshToken.trim());
        validarIdentidade(jwt);
        return jwt;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * A expiração é obrigatória: um token sem {@code exp} seria aceito indefinidamente.
     */
    private JWTVerifier criarVerifier(String issuer, String tipo) {
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .withClaim(JwtClaims.TIPO, tipo)
                .withClaimPresence(RegisteredClaims.EXPIRES_AT)
                .build();
    }

    private void validarIdentidade(DecodedJWT jwt) {
        if (jwt.getClaim(JwtClaims.USUARIO_ID).asLong() == null || jwt.getClaim(JwtClaims.TENANT_ID).asLong() == null)
            throw new JWTVerificationException("Token sem identificação de usuário ou empresa.");
    }

    private String extrairToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new JWTVerificationException("Header Authorization não informado.");
        }

        if (!authorizationHeader.regionMatches(
                true,
                0,
                BEARER_PREFIX,
                0,
                BEARER_PREFIX.length()
        )) {
            throw new JWTVerificationException("Header Authorization deve utilizar o formato Bearer.");
        }

        String token = authorizationHeader
                .substring(BEARER_PREFIX.length())
                .trim();

        if (token.isBlank()) {
            throw new JWTVerificationException("Token JWT não informado.");
        }

        return token;
    }
}
