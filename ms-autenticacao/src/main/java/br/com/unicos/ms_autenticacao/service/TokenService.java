package br.com.unicos.ms_autenticacao.service;

import br.com.unicos.core.auth.model.AuthenticatedUser;
import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.core.usuario.dto.UsuarioAuthResponse;
import br.com.unicos.ms_autenticacao.dto.login.DadosLoginDto;
import br.com.unicos.ms_autenticacao.dto.token.DadosTokenDto;
import br.com.unicos.ms_autenticacao.dto.token.TokenUserDataDto;
import br.com.unicos.ms_autenticacao.loader.AutenticacaoLoader;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
public class TokenService {

    private static final String MENSAGEM_REFRESH_INVALIDO = "Refresh token inválido ou expirado";

    private final String issuer;
    private final long tempoExpTokenMinutos;
    private final long tempoExpRefreshTokenMinutos;
    private final AutenticacaoLoader autenticacaoLoader;
    private final UsuarioService usuarioService;
    private final TokenCoreService tokenCoreService;

    public TokenService(
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.tempo.exp.token}") long tempoExpTokenMinutos,
            @Value("${jwt.tempo.exp.refresh.token}") long tempoExpRefreshTokenMinutos,
            AutenticacaoLoader autenticacaoLoader,
            UsuarioService usuarioService,
            TokenCoreService tokenCoreService
    ) {
        if (tempoExpTokenMinutos <= 0 || tempoExpRefreshTokenMinutos <= 0)
            throw new IllegalStateException("Os tempos de expiração dos tokens devem ser maiores que zero.");

        this.issuer = issuer;
        this.tempoExpTokenMinutos = tempoExpTokenMinutos;
        this.tempoExpRefreshTokenMinutos = tempoExpRefreshTokenMinutos;
        this.autenticacaoLoader = autenticacaoLoader;
        this.usuarioService = usuarioService;
        this.tokenCoreService = tokenCoreService;
    }

    public DadosTokenDto autenticar(DadosLoginDto dados) {
        try {
            AuthenticatedUser user = autenticacaoLoader.authenticate(dados.email(), dados.senha());

            return gerarTokens(new TokenUserDataDto(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmpresaId()
            ));
        } catch (DisabledException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }
    }

    /**
     * Emite um novo par de tokens a partir de um refresh token válido.
     *
     * <p>
     * O usuário é consultado novamente: usuários desativados, removidos ou transferidos de
     * empresa não conseguem renovar a sessão.
     * </p>
     */
    public DadosTokenDto atualizarToken(String refreshToken) {
        DecodedJWT jwt;
        try {
            jwt = tokenCoreService.validarRefreshToken(refreshToken);
        } catch (JWTVerificationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, MENSAGEM_REFRESH_INVALIDO);
        }

        Long usuarioId = jwt.getClaim(JwtClaims.USUARIO_ID).asLong();
        Long tenantId = jwt.getClaim(JwtClaims.TENANT_ID).asLong();

        UsuarioAuthResponse usuario;
        try {
            usuario = usuarioService.buscarUsuarioPorId(usuarioId);
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, MENSAGEM_REFRESH_INVALIDO);
        }

        if (usuario == null || !Objects.equals(usuario.empresaId(), tenantId))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, MENSAGEM_REFRESH_INVALIDO);

        if (!usuario.ativo())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário desabilitado");

        return gerarTokens(new TokenUserDataDto(usuario.userId(), usuario.login(), usuario.empresaId()));
    }

    private DadosTokenDto gerarTokens(TokenUserDataDto user) {
        return new DadosTokenDto(gerarAccessToken(user), gerarRefreshToken(user));
    }

    private String gerarAccessToken(TokenUserDataDto user) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.userId().toString())
                .withClaim(JwtClaims.USERNAME, user.username())
                .withClaim(JwtClaims.USUARIO_ID, user.userId())
                .withClaim(JwtClaims.TENANT_ID, user.tenantId())
                .withClaim(JwtClaims.TIPO, JwtClaims.TIPO_ACCESS)
                .withIssuedAt(Instant.now())
                .withExpiresAt(calcularExpiracao(tempoExpTokenMinutos))
                .sign(tokenCoreService.getAlgorithm());
    }

    private String gerarRefreshToken(TokenUserDataDto user) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.userId().toString())
                .withClaim(JwtClaims.USERNAME, user.username())
                .withClaim(JwtClaims.USUARIO_ID, user.userId())
                .withClaim(JwtClaims.TENANT_ID, user.tenantId())
                .withClaim(JwtClaims.TIPO, JwtClaims.TIPO_REFRESH)
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(Instant.now())
                .withExpiresAt(calcularExpiracao(tempoExpRefreshTokenMinutos))
                .sign(tokenCoreService.getAlgorithm());
    }

    private static Instant calcularExpiracao(long minutos) {
        return Instant.now().plusSeconds(minutos * 60);
    }
}
