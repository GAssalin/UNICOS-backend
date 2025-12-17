package br.com.unicos.ms_auth.security_access;

import br.com.unicos.ms_auth.exception.TenantNotAssociatedException;
import br.com.unicos.ms_auth.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String segredo;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.tempo.exp.token}")
    private Integer tempoExpToken; //minutos

    @Value("${jwt.tempo.exp.refresh.token}")
    private Integer tempoExpRefreshToken; //minutos

    public String gerarAccessToken(Usuario usuario) {
        if (usuario.getEmpresaId() == null)
            throw new TenantNotAssociatedException();

        Algorithm algorithm = Algorithm.HMAC256(segredo);

        List<String> roles = usuario.getRoles().stream()
                .map(role -> "ROLE_" + role.getNome())
                .toList();

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(usuario.getEmail())
                .withClaim("roles", roles)
                .withClaim("usuarioId", usuario.getId())
                .withClaim("tenantId", usuario.getEmpresaId())
                .withClaim("typ", "access")
                .withExpiresAt(expiracao(tempoExpToken))
                .sign(algorithm);
    }

    public String gerarRefreshToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(segredo);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(usuario.getId().toString())
                    .withClaim("typ", "refresh")
                    .withJWTId(UUID.randomUUID().toString())
                    .withExpiresAt(expiracao(tempoExpRefreshToken))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Erro ao gerar token refresh JWT de acesso!", exception);
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