package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoUpdateRequest;
import br.com.unicos.ms_empresa.service.EmpresaConfiguracaoService;
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
 * das configurações globais da empresa (tenant).
 *
 * <p>
 * Todos os endpoints são restritos ao tenant
 * e respeitam as permissões do usuário autenticado.
 * </p>
 */
@RestController
@RequestMapping("/v1/empresas/{empresaRefId}/configuracoes")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Configurações da Empresa",
        description = "Endpoints para gerenciamento de configurações globais da empresa (tenant)."
)
public class EmpresaConfiguracaoController {

    private final EmpresaConfiguracaoService empresaConfiguracaoService;

    // ============================================================
    // ➕ CRIAÇÃO
    // ============================================================

    @Operation(
            summary = "Criar configuração da empresa",
            description = "Cria uma nova configuração global para a empresa."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Configuração criada com sucesso",
            content = @Content(
                    schema = @Schema(implementation = EmpresaConfiguracaoResponse.class)
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_CONFIGURACAO_CRIAR')")
    @PostMapping
    public ResponseEntity<EmpresaConfiguracaoResponse> criar(
            @PathVariable Long empresaRefId,
            @RequestBody @Validated EmpresaConfiguracaoCreateRequest request
    ) {
        // garante consistência entre path e body
        EmpresaConfiguracaoCreateRequest normalized =
                new EmpresaConfiguracaoCreateRequest(
                        request.empresaId(),
                        empresaRefId,
                        request.chave(),
                        request.valor()
                );

        EmpresaConfiguracaoResponse response = empresaConfiguracaoService.criar(normalized);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // ✏️ ATUALIZAÇÃO
    // ============================================================

    @Operation(
            summary = "Atualizar configuração da empresa",
            description = "Atualiza uma configuração existente identificada pela chave."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Configuração atualizada com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_CONFIGURACAO_EDITAR')")
    @PutMapping("/{chave}")
    public ResponseEntity<EmpresaConfiguracaoResponse> atualizar(
            @PathVariable Long empresaRefId,
            @PathVariable String chave,
            @RequestBody @Validated EmpresaConfiguracaoUpdateRequest request
    ) {
        return ResponseEntity.ok(
                empresaConfiguracaoService.atualizar(
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
            summary = "Buscar configuração por chave",
            description = "Retorna os detalhes de uma configuração específica da empresa."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso",
            content = @Content(
                    schema = @Schema(implementation = EmpresaConfiguracaoResponse.class)
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_CONFIGURACAO_LISTAR')")
    @GetMapping("/{chave}")
    public ResponseEntity<EmpresaConfiguracaoResponse> buscarPorChave(
            @PathVariable Long empresaRefId,
            @PathVariable String chave
    ) {
        return ResponseEntity.ok(empresaConfiguracaoService.buscarPorChave(empresaRefId, chave));
    }

    // ============================================================
    // 📄 LISTAGEM
    // ============================================================

    @Operation(
            summary = "Listar configurações da empresa",
            description = "Lista todas as configurações da empresa de forma paginada."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso",
            content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = EmpresaConfiguracaoResumoResponse.class)
                    )
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_CONFIGURACAO_LISTAR')")
    @GetMapping
    public ResponseEntity<Page<EmpresaConfiguracaoResumoResponse>> listar(
            @PathVariable Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaConfiguracaoService.listar(empresaRefId, pageable));
    }

    // ============================================================
    // 🗑️ EXCLUSÃO
    // ============================================================

    @Operation(
            summary = "Remover configuração da empresa",
            description = "Remove uma configuração da empresa identificada pela chave."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Configuração removida com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_CONFIGURACAO_EXCLUIR')")
    @DeleteMapping("/{chave}")
    public ResponseEntity<Void> remover(
            @PathVariable Long empresaRefId,
            @PathVariable String chave
    ) {
        empresaConfiguracaoService.remover(empresaRefId, chave);
        return ResponseEntity.noContent().build();
    }
}
