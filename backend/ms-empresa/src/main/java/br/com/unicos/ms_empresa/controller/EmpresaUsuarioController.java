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
    // CREATE
    // ============================================================

    @Operation(
            summary = "Vincular usuário à empresa",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário vinculado com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaUsuarioResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
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

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaUsuarioService.criar(normalized));
    }

    // ============================================================
    // UPDATE PERFIL
    // ============================================================

    @Operation(
            summary = "Atualizar perfil do usuário na empresa",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Violação de regra (último ADMIN)"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
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
    // GET
    // ============================================================

    @Operation(
            summary = "Buscar vínculo de usuário",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaUsuarioResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{usuarioId}")
    public ResponseEntity<EmpresaUsuarioResponse> buscar(
            @PathVariable Long empresaRefId,
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(empresaUsuarioService.buscar(empresaRefId, usuarioId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Operation(
            summary = "Listar usuários da empresa",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = EmpresaUsuarioResumoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<EmpresaUsuarioResumoResponse>> listar(
            @PathVariable Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaUsuarioService.listar(empresaRefId, pageable));
    }

    // ============================================================
    // LIST BY PERFIL
    // ============================================================

    @Operation(
            summary = "Listar usuários por perfil",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
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
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover usuário da empresa",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "409", description = "Violação de regra (último ADMIN)"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> remover(
            @PathVariable Long empresaRefId,
            @PathVariable Long usuarioId
    ) {
        empresaUsuarioService.remover(empresaRefId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
