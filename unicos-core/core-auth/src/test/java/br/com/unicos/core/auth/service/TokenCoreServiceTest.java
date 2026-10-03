package br.com.unicos.core.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenCoreServiceTest {

    private static final String SEGREDO = "segredo-de-teste-com-mais-de-32-caracteres";
    private static final String ISSUER = "unicos-teste";

    private final TokenCoreService service = new TokenCoreService(SEGREDO, ISSUER);

    @Test
    void deveValidarAccessTokenNoHeaderBearer() {
        DecodedJWT jwt = service.validarToken("Bearer " + token(JwtClaims.TIPO_ACCESS, SEGREDO, ISSUER));

        assertThat(jwt.getClaim(JwtClaims.USUARIO_ID).asLong()).isEqualTo(10L);
        assertThat(jwt.getClaim(JwtClaims.TENANT_ID).asLong()).isEqualTo(20L);
    }

    @Test
    void deveAceitarPrefixoBearerSemDiferenciarMaiusculas() {
        assertThat(service.validarToken("bearer " + token(JwtClaims.TIPO_ACCESS, SEGREDO, ISSUER))).isNotNull();
    }

    @Test
    void deveRejeitarRefreshTokenComoAccessToken() {
        String refresh = token(JwtClaims.TIPO_REFRESH, SEGREDO, ISSUER);

        assertThatThrownBy(() -> service.validarToken("Bearer " + refresh))
                .isInstanceOf(JWTVerificationException.class);
        assertThat(service.validarRefreshToken(refresh)).isNotNull();
    }

    @Test
    void deveRejeitarAccessTokenComoRefreshToken() {
        String access = token(JwtClaims.TIPO_ACCESS, SEGREDO, ISSUER);

        assertThatThrownBy(() -> service.validarRefreshToken(access))
                .isInstanceOf(JWTVerificationException.class);
    }

    @Test
    void deveRejeitarAssinaturaOuEmissorDiferentes() {
        String outroSegredo = token(JwtClaims.TIPO_ACCESS, "outro-segredo-com-mais-de-32-caracteres!!", ISSUER);
        String outroIssuer = token(JwtClaims.TIPO_ACCESS, SEGREDO, "outro-issuer");

        assertThatThrownBy(() -> service.validarToken("Bearer " + outroSegredo))
                .isInstanceOf(JWTVerificationException.class);
        assertThatThrownBy(() -> service.validarToken("Bearer " + outroIssuer))
                .isInstanceOf(JWTVerificationException.class);
    }

    @Test
    void deveRejeitarTokenExpirado() {
        String expirado = JWT.create()
                .withIssuer(ISSUER)
                .withClaim(JwtClaims.TIPO, JwtClaims.TIPO_ACCESS)
                .withClaim(JwtClaims.USUARIO_ID, 10L)
                .withClaim(JwtClaims.TENANT_ID, 20L)
                .withExpiresAt(Instant.now().minusSeconds(60))
                .sign(Algorithm.HMAC256(SEGREDO));

        assertThatThrownBy(() -> service.validarToken("Bearer " + expirado))
                .isInstanceOf(JWTVerificationException.class);
    }

    @Test
    void deveRejeitarTokenSemIdentidade() {
        String semTenant = JWT.create()
                .withIssuer(ISSUER)
                .withClaim(JwtClaims.TIPO, JwtClaims.TIPO_ACCESS)
                .withClaim(JwtClaims.USUARIO_ID, 10L)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .sign(Algorithm.HMAC256(SEGREDO));

        assertThatThrownBy(() -> service.validarToken("Bearer " + semTenant))
                .isInstanceOf(JWTVerificationException.class);
    }

    @Test
    void deveRejeitarHeaderAusenteOuMalFormado() {
        assertThatThrownBy(() -> service.validarToken(null)).isInstanceOf(JWTVerificationException.class);
        assertThatThrownBy(() -> service.validarToken("Basic abc")).isInstanceOf(JWTVerificationException.class);
        assertThatThrownBy(() -> service.validarToken("Bearer   ")).isInstanceOf(JWTVerificationException.class);
    }

    private static String token(String tipo, String segredo, String issuer) {
        JWTCreator.Builder builder = JWT.create()
                .withIssuer(issuer)
                .withClaim(JwtClaims.TIPO, tipo)
                .withClaim(JwtClaims.USUARIO_ID, 10L)
                .withClaim(JwtClaims.TENANT_ID, 20L)
                .withExpiresAt(Instant.now().plusSeconds(60));
        return builder.sign(Algorithm.HMAC256(segredo));
    }
}
