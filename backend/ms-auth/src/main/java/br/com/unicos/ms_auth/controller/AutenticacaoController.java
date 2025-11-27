package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.login.DadosLogin;
import br.com.unicos.ms_auth.dto.token.DadosRefreshToken;
import br.com.unicos.ms_auth.dto.token.DadosToken;
import br.com.unicos.ms_auth.model.Usuario;
import br.com.unicos.ms_auth.repository.UsuarioRepository;
import br.com.unicos.ms_auth.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST responsável pelo gerenciamento dos usuários autenticáveis do sistema.
 * <p>
 * Disponibiliza endpoints para criação, atualização, consulta, desativação
 * e exclusão definitiva de usuários.
 */
@RestController
@RequestMapping("/v1/autenticacao")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<DadosToken> efetuarLogin(@Valid @RequestBody DadosLogin dados) {
        UsernamePasswordAuthenticationToken autenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        Authentication authentication = authenticationManager.authenticate(autenticationToken);

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String tokenAcesso = tokenService.gerarToken(usuario);
        String refreshToken = usuario.novoRefreshToken();
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new DadosToken(tokenAcesso, refreshToken));
    }

    @PostMapping("/atualizar-token")
    public ResponseEntity<DadosToken> atualizarToken(@Valid @RequestBody DadosRefreshToken dados) throws Exception {
        String refreshToken = dados.refreshToken();
        Usuario usuario = usuarioRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new Exception("Refresh token inválido!"));

        if (usuario.isRefreshTokenExpirado())
            throw new Exception("Refresh token expirado!");

        String tokenAcesso = tokenService.gerarToken(usuario);
        String novoRefreshToken = usuario.novoRefreshToken();

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new DadosToken(tokenAcesso, novoRefreshToken));
    }
}