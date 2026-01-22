package br.com.unicos.ms_usuario.controller;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_usuario.dto.verificacao.ConfirmarEmailVerificacaoRequest;
import br.com.unicos.ms_usuario.dto.verificacao.ConfirmarEmailVerificacaoResponse;
import br.com.unicos.ms_usuario.dto.verificacao.UsuarioEmailVerificacaoListDTO;
import br.com.unicos.ms_usuario.model.Usuario;
import br.com.unicos.ms_usuario.service.UsuarioEmailVerificacaoService;
import br.com.unicos.ms_usuario.service.UtilsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável pelo fluxo de verificação de e-mail dos usuários.
 */
@RestController
@RequestMapping("/v1/auth/verificacao-email")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Verificação de E-mail",
        description = "Endpoints responsáveis pela confirmação de e-mail, reenvio de tokens e listagens administrativas."
)
public class UsuarioEmailVerificacaoController {

    private final UtilsService utilsService;
    private final UsuarioEmailVerificacaoService usuarioEmailVerificacaoService;

    // =============================================================
    // PÚBLICO — CONFIRMAR E-MAIL
    // =============================================================

    @Operation(
            summary = "Confirmar e-mail do usuário",
            description = "Confirma o e-mail do usuário a partir do token de verificação.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "E-mail confirmado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = ConfirmarEmailVerificacaoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Token inválido ou expirado"),
                    @ApiResponse(responseCode = "404", description = "Token não encontrado")
            }
    )
    @PostMapping("/confirmar")
    public ResponseEntity<ConfirmarEmailVerificacaoResponse> confirmar(@Valid @RequestBody ConfirmarEmailVerificacaoRequest request) {

        Usuario usuario = usuarioEmailVerificacaoService.confirmarEmail(request.token());

        return ResponseEntity.ok(
                new ConfirmarEmailVerificacaoResponse(
                        usuario.getId(),
                        usuario.getEmail(),
                        true,
                        "E-mail confirmado com sucesso."
                )
        );
    }

    // =============================================================
    // ADMIN — LISTAR TOKENS PENDENTES (PAGINADO)
    // =============================================================

    @Operation(
            summary = "Listar tokens de verificação pendentes",
            description = "Retorna tokens de verificação de e-mail ainda não confirmados, filtrados por empresa.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @GetMapping("/pendentes")
    public ResponseEntity<Page<UsuarioEmailVerificacaoListDTO>> listarPendentes(@ParameterObject Pageable pageable) {
        if (utilsService.verificarPermissao("USUARIO_EMAIL_LISTAR"))
            return ResponseEntity.ok(usuarioEmailVerificacaoService.listarPendentes(TenantContext.getEmpresaId(), pageable));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // ADMIN — LISTAR TOKENS EXPIRADOS (PAGINADO)
    // =============================================================

    @Operation(
            summary = "Listar tokens de verificação expirados",
            description = "Retorna tokens de verificação expirados, filtrados por empresa.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @GetMapping("/expirados")
    public ResponseEntity<Page<UsuarioEmailVerificacaoListDTO>> listarExpirados(@ParameterObject Pageable pageable) {
        if (utilsService.verificarPermissao("USUARIO_EMAIL_LISTAR"))
            return ResponseEntity.ok(usuarioEmailVerificacaoService.listarExpirados(TenantContext.getEmpresaId(), pageable));
        else
            return ResponseEntity.status(403).build();
    }
}
