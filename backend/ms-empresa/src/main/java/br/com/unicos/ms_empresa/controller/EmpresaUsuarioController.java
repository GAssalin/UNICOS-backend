package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioUpdateRequest;
import br.com.unicos.ms_empresa.enums.PerfilEmpresaUsuario;
import br.com.unicos.ms_empresa.service.EmpresaUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável pelo gerenciamento
 * do vínculo entre usuários e empresas.
 *
 * <p>
 * Todos os endpoints são restritos ao tenant
 * e respeitam as permissões do usuário autenticado.
 * </p>
 */
@RestController
@RequestMapping("/v1/empresas/{empresaRefId}/usuarios")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Usuários da Empresa",
        description = "Endpoints para gerenciamento do vínculo entre usuários e empresas."
)
public class EmpresaUsuarioController {

    private final EmpresaUsuarioService empresaUsuarioService;

    // ============================================================
    // ➕ CRIAÇÃO
    // ============================================================

    @Operation(
            summary = "Vincular usuário à empresa",
            description = "Cria o vínculo entre um usuário e uma empresa."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Usuário vinculado com sucesso",
            content = @Content(
                    schema = @Schema(implementation = EmpresaUsuarioResponse.class)
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_USUARIO_CRIAR')")
    @PostMapping
    public ResponseEntity<EmpresaUsuarioResponse> criar(
            @PathVariable Long empresaRefId,
            @RequestBody @Validated EmpresaUsuarioCreateRequest request
    ) {
        EmpresaUsuarioCreateRequest normalized =
                new EmpresaUsuarioCreateRequest(
                        request.empresaId(),
                        empresaRefId,
                        request.usuarioId(),
                        request.perfil()
                );

        EmpresaUsuarioResponse response = empresaUsuarioService.criar(normalized);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // ✏️ ATUALIZAÇÃO DE PERFIL
    // ============================================================

    @Operation(
            summary = "Atualizar perfil do usuário na empresa",
            description = "Atualiza o perfil de um usuário dentro da empresa."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Perfil atualizado com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_USUARIO_EDITAR')")
    @PutMapping("/{usuarioId}/perfil")
    public ResponseEntity<EmpresaUsuarioResponse> atualizarPerfil(
            @PathVariable Long empresaRefId,
            @PathVariable Long usuarioId,
            @RequestBody @Validated EmpresaUsuarioUpdateRequest request
    ) {
        return ResponseEntity.ok(
                empresaUsuarioService.atualizarPerfil(
                        empresaRefId,
                        usuarioId,
                        request
                )
        );
    }

    // ============================================================
    // 🔍 CONSULTA POR USUÁRIO
    // ============================================================

    @Operation(
            summary = "Buscar vínculo de usuário",
            description = "Retorna o vínculo de um usuário específico com a empresa."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso",
            content = @Content(
                    schema = @Schema(implementation = EmpresaUsuarioResponse.class)
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_USUARIO_LISTAR')")
    @GetMapping("/{usuarioId}")
    public ResponseEntity<EmpresaUsuarioResponse> buscar(
            @PathVariable Long empresaRefId,
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(empresaUsuarioService.buscar(empresaRefId, usuarioId));
    }

    // ============================================================
    // 📄 LISTAGEM
    // ============================================================

    @Operation(
            summary = "Listar usuários da empresa",
            description = "Lista os usuários vinculados à empresa de forma paginada."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso",
            content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = EmpresaUsuarioResumoResponse.class)
                    )
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_USUARIO_LISTAR')")
    @GetMapping
    public ResponseEntity<Page<EmpresaUsuarioResumoResponse>> listar(
            @PathVariable Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaUsuarioService.listar(empresaRefId, pageable));
    }

    // ============================================================
    // 📄 LISTAGEM POR PERFIL
    // ============================================================

    @Operation(
            summary = "Listar usuários por perfil",
            description = "Lista os usuários vinculados à empresa filtrando pelo perfil."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_USUARIO_LISTAR')")
    @GetMapping("/perfil/{perfil}")
    public ResponseEntity<Page<EmpresaUsuarioResumoResponse>> listarPorPerfil(
            @PathVariable Long empresaRefId,
            @PathVariable PerfilEmpresaUsuario perfil,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                empresaUsuarioService.listarPorPerfil(
                        empresaRefId,
                        perfil,
                        pageable
                )
        );
    }

    // ============================================================
    // 🗑️ EXCLUSÃO
    // ============================================================

    @Operation(
            summary = "Remover usuário da empresa",
            description = "Remove o vínculo de um usuário com a empresa."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Usuário removido com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_USUARIO_EXCLUIR')")
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> remover(
            @PathVariable Long empresaRefId,
            @PathVariable Long usuarioId
    ) {
        empresaUsuarioService.remover(empresaRefId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
