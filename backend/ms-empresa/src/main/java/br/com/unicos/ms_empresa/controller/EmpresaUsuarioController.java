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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
 * <p>Todos os endpoints são restritos ao tenant
 * e respeitam as permissões do usuário autenticado.</p>
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

    @Operation(
            summary = "Vincular usuário à empresa",
            description = "Cria um vínculo entre um usuário e a empresa informada.",
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
            @PathVariable @Positive Long empresaRefId,
            @RequestBody @Valid EmpresaUsuarioCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaUsuarioService.criar(request));
    }

    @Operation(
            summary = "Atualizar perfil do usuário na empresa",
            description = "Atualiza o perfil do usuário vinculado à empresa.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Perfil atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaUsuarioResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Violação de regra de negócio"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{usuarioId}/perfil")
    public ResponseEntity<EmpresaUsuarioResponse> atualizarPerfil(
            @PathVariable @Positive Long empresaRefId,
            @PathVariable @Positive Long usuarioId,
            @RequestBody @Valid EmpresaUsuarioUpdateRequest request
    ) {
        return ResponseEntity.ok(empresaUsuarioService.atualizarPerfil(usuarioId, request));
    }

    @Operation(
            summary = "Buscar vínculo de usuário",
            description = "Busca o vínculo entre usuário e empresa pelo identificador do usuário.",
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
            @PathVariable @Positive Long empresaRefId,
            @PathVariable @Positive Long usuarioId
    ) {
        return ResponseEntity.ok(empresaUsuarioService.buscar(usuarioId));
    }

    @Operation(
            summary = "Listar usuários da empresa",
            description = "Lista os usuários vinculados à empresa de forma paginada.",
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
            @PathVariable @Positive Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaUsuarioService.listar(pageable));
    }

    @Operation(
            summary = "Listar usuários por perfil",
            description = "Lista os usuários da empresa filtrando pelo perfil informado.",
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
    @GetMapping("/perfil/{perfil}")
    public ResponseEntity<Page<EmpresaUsuarioResumoResponse>> listarPorPerfil(
            @PathVariable @Positive Long empresaRefId,
            @PathVariable PerfilEmpresaUsuario perfil,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaUsuarioService.listarPorPerfil(perfil, pageable));
    }

    @Operation(
            summary = "Remover usuário da empresa",
            description = "Remove o vínculo entre usuário e empresa.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "409", description = "Violação de regra de negócio"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> remover(
            @PathVariable @Positive Long empresaRefId,
            @PathVariable @Positive Long usuarioId
    ) {
        empresaUsuarioService.remover(usuarioId);
        return ResponseEntity.noContent().build();
    }
}