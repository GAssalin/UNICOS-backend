package br.com.unicos.core.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenCoreService {

    private static final String BEARER_PREFIX = "Bearer ";

    private final String segredo;
    private final String issuer;

    public TokenCoreService(String segredo, String issuer) {
        this.segredo = segredo;
        this.issuer = issuer;
    }

    public DecodedJWT validarToken(String authorizationHeader) {
        String token = extrairToken(authorizationHeader);

        return JWT.require(getAlgorithm())
                .withIssuer(issuer)
                .withClaim("typ", "access")
                .build()
                .verify(token);
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

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(segredo);
    }
}
