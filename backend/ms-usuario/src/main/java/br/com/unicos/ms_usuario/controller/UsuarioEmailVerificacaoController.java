package br.com.unicos.ms_usuario.controller;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_usuario.dto.verificacao.ConfirmarEmailVerificacaoRequest;
import br.com.unicos.ms_usuario.dto.verificacao.ConfirmarEmailVerificacaoResponse;
import br.com.unicos.ms_usuario.dto.verificacao.UsuarioEmailVerificacaoListDTO;
import br.com.unicos.ms_usuario.model.Usuario;
import br.com.unicos.ms_usuario.service.UsuarioEmailVerificacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável pelo fluxo de verificação de e-mail dos usuários.
 * <p>
 * Endpoints:
 * - Confirmar e-mail através do token
 * - Reenviar token de verificação
 * - Listar tokens pendentes
 * - Listar tokens expirados
 */
@RestController
@RequestMapping("/v1/auth/verificacao-email")
@RequiredArgsConstructor
@Tag(
        name = "Verificação de E-mail",
        description = "Endpoints responsáveis pela confirmação de e-mail, reenvio de tokens e listagens administrativas."
)
public class UsuarioEmailVerificacaoController {

    private final UsuarioEmailVerificacaoService service;

    // ============================================================
    // PÚBLICO — CONFIRMAR E-MAIL
    // ============================================================

    @PostMapping("/confirmar")
    public ResponseEntity<ConfirmarEmailVerificacaoResponse> confirmar(@Valid @RequestBody ConfirmarEmailVerificacaoRequest request) {
        Usuario usuario = service.confirmarEmail(request.token());

        return ResponseEntity.ok(
                new ConfirmarEmailVerificacaoResponse(
                        usuario.getId(),
                        usuario.getEmail(),
                        true,
                        "E-mail confirmado com sucesso."
                )
        );
    }

    // ============================================================
    // ADMIN — LISTAR TOKENS PENDENTES (PAGINADO)
    // ============================================================

    @GetMapping("/pendentes")
    public ResponseEntity<Page<UsuarioEmailVerificacaoListDTO>> listarPendentes(Pageable pageable) {
        return ResponseEntity.ok(service.listarPendentes(TenantContext.getEmpresaId(), pageable));
    }

    // ============================================================
    // ADMIN — LISTAR TOKENS EXPIRADOS (PAGINADO)
    // ============================================================

    @GetMapping("/expirados")
    public ResponseEntity<Page<UsuarioEmailVerificacaoListDTO>> listarExpirados(Pageable pageable) {
        return ResponseEntity.ok(service.listarExpirados(TenantContext.getEmpresaId(), pageable));
    }
}
