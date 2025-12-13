package br.com.unicos.ms_auth.security_access;

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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String segredo;

    @Value("${jwt.issuer}")
    String issuer;

    public String gerarToken(Usuario usuario) {
        if (usuario.getEmpresaId() == null)
            throw new IllegalArgumentException("Empresa ID não pode ser nulo");

        Algorithm algorithm = Algorithm.HMAC256(segredo);

        List<String> roles = usuario.getRoles().stream()
                .map(role -> "ROLE_" + role.getNome())
                .toList();

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(usuario.getEmail())
                .withClaim("roles", roles)
                .withClaim("tenant_id", usuario.getEmpresaId())
                .withExpiresAt(expiracao(15))
                .sign(algorithm);
    }

    public String gerarRefreshToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(segredo);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(usuario.getId().toString())
                    .withExpiresAt(expiracao(1440))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Erro ao gerar token refresh JWT de acesso!", exception);
        }
    }

    public DecodedJWT verificarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(segredo);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();

            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new JWTCreationException("Erro ao verificar token JWT!", exception);
        }
    }

    private Instant expiracao(Integer minutos) {
        return LocalDateTime.now().plusMinutes(minutos).toInstant(ZoneOffset.of("-03:00"));
    }
}