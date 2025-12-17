package br.com.unicos.ms_auth.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_auth.dto.login.DadosLogin;
import br.com.unicos.ms_auth.dto.token.DadosRefreshToken;
import br.com.unicos.ms_auth.dto.token.DadosToken;
import br.com.unicos.ms_auth.model.Usuario;
import br.com.unicos.ms_auth.repository.UsuarioRepository;
import br.com.unicos.ms_auth.security_access.TokenService;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public ResponseEntity<DadosToken> autenticar(DadosLogin dados) {

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dados.email(), dados.senha()));
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }

        Usuario usuario = (Usuario) authentication.getPrincipal();

        try {
            String accessToken = tokenService.gerarAccessToken(usuario);
            String refreshToken = tokenService.gerarRefreshToken(usuario);

            usuario.setRefreshToken(refreshToken);
            usuario = usuarioRepository.save(usuario);

            TenantContext.setUsuarioId(usuario.getId());
            TenantContext.setEmpresaId(usuario.getEmpresaId());

            return ResponseEntity.ok(new DadosToken(accessToken, refreshToken));
        } finally {
            TenantContext.clear();
        }
    }

    public ResponseEntity<DadosToken> atualizarToken(@Valid DadosRefreshToken dados) {
        String refreshToken = dados.refreshToken();

        DecodedJWT jwt;
        try {
            jwt = tokenService.verificarRefreshToken(refreshToken);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido ou expirado");
        }
        Long userId = Long.valueOf(jwt.getSubject());

        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido ou expirado"));

        if (usuario.getRefreshToken() == null || usuario.isRefreshTokenExpirado() || !usuario.getRefreshToken().equals(refreshToken))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido ou expirado");

        String tokenAcesso = tokenService.gerarAccessToken(usuario);
        String novoRefreshToken = tokenService.gerarRefreshToken(usuario);

        usuario.setRefreshToken(novoRefreshToken);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new DadosToken(tokenAcesso, novoRefreshToken));
    }
}
