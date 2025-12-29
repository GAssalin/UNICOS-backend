package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroUpdateRequest;
import br.com.unicos.ms_empresa.service.EmpresaParametroService;
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
 * dos parâmetros flexíveis da empresa.
 *
 * <p>
 * Todos os endpoints são restritos ao tenant
 * e respeitam as permissões do usuário autenticado.
 * </p>
 */
@RestController
@RequestMapping("/v1/empresas/{empresaRefId}/parametros")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Parâmetros da Empresa",
        description = "Endpoints para gerenciamento dos parâmetros flexíveis da empresa (chave-valor)."
)
public class EmpresaParametroController {

    private final EmpresaParametroService empresaParametroService;

    // ============================================================
    // ➕ CRIAÇÃO
    // ============================================================

    @Operation(
            summary = "Criar parâmetro da empresa",
            description = "Cria um novo parâmetro (chave-valor) para a empresa."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Parâmetro criado com sucesso",
            content = @Content(
                    schema = @Schema(implementation = EmpresaParametroResponse.class)
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_PARAMETRO_CRIAR')")
    @PostMapping
    public ResponseEntity<EmpresaParametroResponse> criar(
            @PathVariable Long empresaRefId,
            @RequestBody @Validated EmpresaParametroCreateRequest request
    ) {
        EmpresaParametroCreateRequest normalized =
                new EmpresaParametroCreateRequest(
                        request.empresaId(),
                        empresaRefId,
                        request.chave(),
                        request.valor()
                );

        EmpresaParametroResponse response = empresaParametroService.criar(normalized);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // ✏️ ATUALIZAÇÃO
    // ============================================================

    @Operation(
            summary = "Atualizar parâmetro da empresa",
            description = "Atualiza o valor de um parâmetro existente identificado pela chave."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Parâmetro atualizado com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_PARAMETRO_EDITAR')")
    @PutMapping("/{chave}")
    public ResponseEntity<EmpresaParametroResponse> atualizar(
            @PathVariable Long empresaRefId,
            @PathVariable String chave,
            @RequestBody @Validated EmpresaParametroUpdateRequest request
    ) {
        return ResponseEntity.ok(
                empresaParametroService.atualizar(
                        empresaRefId,
                        chave,
                        request
                )
        );
    }

    // ============================================================
    // 🔍 CONSULTA POR CHAVE
    // ============================================================

    @Operation(
            summary = "Buscar parâmetro por chave",
            description = "Retorna os detalhes de um parâmetro específico da empresa."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso",
            content = @Content(
                    schema = @Schema(implementation = EmpresaParametroResponse.class)
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_PARAMETRO_LISTAR')")
    @GetMapping("/{chave}")
    public ResponseEntity<EmpresaParametroResponse> buscarPorChave(
            @PathVariable Long empresaRefId,
            @PathVariable String chave
    ) {
        return ResponseEntity.ok(empresaParametroService.buscarPorChave(empresaRefId, chave));
    }

    // ============================================================
    // 📄 LISTAGEM
    // ============================================================

    @Operation(
            summary = "Listar parâmetros da empresa",
            description = "Lista os parâmetros da empresa de forma paginada."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso",
            content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = EmpresaParametroResumoResponse.class)
                    )
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_PARAMETRO_LISTAR')")
    @GetMapping
    public ResponseEntity<Page<EmpresaParametroResumoResponse>> listar(
            @PathVariable Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaParametroService.listar(empresaRefId, pageable));
    }

    // ============================================================
    // 🗑️ EXCLUSÃO
    // ============================================================

    @Operation(
            summary = "Remover parâmetro da empresa",
            description = "Remove um parâmetro da empresa identificado pela chave."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Parâmetro removido com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_PARAMETRO_EXCLUIR')")
    @DeleteMapping("/{chave}")
    public ResponseEntity<Void> remover(
            @PathVariable Long empresaRefId,
            @PathVariable String chave
    ) {
        empresaParametroService.remover(empresaRefId, chave);
        return ResponseEntity.noContent().build();
    }
}
