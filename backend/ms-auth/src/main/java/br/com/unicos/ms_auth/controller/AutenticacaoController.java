package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.login.DadosLogin;
import br.com.unicos.ms_auth.dto.token.DadosRefreshToken;
import br.com.unicos.ms_auth.dto.token.DadosToken;
import br.com.unicos.ms_auth.model.Usuario;
import br.com.unicos.ms_auth.repository.UsuarioRepository;
import br.com.unicos.ms_auth.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping("/v1/autenticacao")
@RequiredArgsConstructor
@Tag(
        name = "Autenticação",
        description = "Endpoints responsáveis por login, renovação de tokens e gerenciamento de credenciais de acesso."
)
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    // ============================================================
    // LOGIN
    // ============================================================

    @Operation(
            summary = "Efetuar login",
            description = "Autentica o usuário com email e senha, gerando um token JWT de acesso e um refresh token.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login realizado com sucesso",
                            content = @Content(schema = @Schema(implementation = DadosToken.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciais inválidas ou usuário não autorizado",
                            content = @Content
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<DadosToken> efetuarLogin(@Valid @RequestBody DadosLogin dados) {
        UsernamePasswordAuthenticationToken autenticationToken =
                new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());

        Authentication authentication = authenticationManager.authenticate(autenticationToken);

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String tokenAcesso = tokenService.gerarToken(usuario);
        String refreshToken = usuario.novoRefreshToken();
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new DadosToken(tokenAcesso, refreshToken));
    }

    // ============================================================
    // REFRESH TOKEN
    // ============================================================
    @SecurityRequirement(name = "bearer-key")
    @Operation(
            summary = "Atualizar token de acesso",
            description = "Gera um novo token JWT de acesso utilizando um refresh token válido e ainda ativo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Novo token gerado com sucesso",
                            content = @Content(schema = @Schema(implementation = DadosToken.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requisição mal formada",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Refresh token inválido ou expirado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado para o refresh token informado",
                            content = @Content
                    )
            }
    )
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
