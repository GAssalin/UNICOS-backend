package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.login.DadosLogin;
import br.com.unicos.ms_auth.dto.token.DadosRefreshToken;
import br.com.unicos.ms_auth.dto.token.DadosToken;
import br.com.unicos.ms_auth.service.AutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    private final AutenticacaoService autenticacaoService;

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
        return autenticacaoService.autenticar(dados);
    }

    // ============================================================
    // REFRESH TOKEN
    // ============================================================
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
    public ResponseEntity<DadosToken> atualizarToken(@Valid @RequestBody DadosRefreshToken dados) {
        return autenticacaoService.atualizarToken(dados);
    }
}
