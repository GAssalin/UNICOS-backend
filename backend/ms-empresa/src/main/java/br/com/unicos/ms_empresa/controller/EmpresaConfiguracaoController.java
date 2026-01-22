package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoUpdateRequest;
import br.com.unicos.ms_empresa.service.EmpresaConfiguracaoService;
import br.com.unicos.ms_empresa.service.UtilsService;
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

@RestController
@RequestMapping("/v1/empresas/{empresaRefId}/configuracoes")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Configurações da Empresa",
        description = "Endpoints para gerenciamento de configurações globais da empresa (tenant)."
)
public class EmpresaConfiguracaoController {

    private final UtilsService utilsService;
    private final EmpresaConfiguracaoService empresaConfiguracaoService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar configuração da empresa",
            description = "Cria uma nova configuração global para a empresa.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Configuração criada com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaConfiguracaoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<EmpresaConfiguracaoResponse> criar(
            @PathVariable Long empresaRefId,
            @RequestBody @Validated EmpresaConfiguracaoCreateRequest request
    ) {
        if (utilsService.verificarPermissao("EMPRESA_CONFIGURACAO_CRIAR")) {
            EmpresaConfiguracaoCreateRequest normalized =
                    new EmpresaConfiguracaoCreateRequest(
                            request.empresaId(),
                            empresaRefId,
                            request.chave(),
                            request.valor()
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(empresaConfiguracaoService.criar(normalized));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar configuração da empresa",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Configuração atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaConfiguracaoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Configuração não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{chave}")
    public ResponseEntity<EmpresaConfiguracaoResponse> atualizar(
            @PathVariable Long empresaRefId,
            @PathVariable String chave,
            @RequestBody @Validated EmpresaConfiguracaoUpdateRequest request
    ) {
        if (utilsService.verificarPermissao("EMPRESA_CONFIGURACAO_EDITAR"))
            return ResponseEntity.ok(empresaConfiguracaoService.atualizar(empresaRefId, chave, request));
        else
            return ResponseEntity.status(403).build();
    }

    // ============================================================
    // GET BY KEY
    // ============================================================

    @Operation(
            summary = "Buscar configuração por chave",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaConfiguracaoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Configuração não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{chave}")
    public ResponseEntity<EmpresaConfiguracaoResponse> buscarPorChave(
            @PathVariable Long empresaRefId,
            @PathVariable String chave
    ) {
        if (utilsService.verificarPermissao("EMPRESA_CONFIGURACAO_LISTAR"))
            return ResponseEntity.ok(empresaConfiguracaoService.buscarPorChave(empresaRefId, chave));
        else
            return ResponseEntity.status(403).build();
    }

    // ============================================================
    // LIST (PAGINATED)
    // ============================================================

    @Operation(
            summary = "Listar configurações da empresa",
            description = "Lista todas as configurações da empresa de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = EmpresaConfiguracaoResumoResponse.class
                                            )
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<EmpresaConfiguracaoResumoResponse>> listar(
            @PathVariable Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        if (utilsService.verificarPermissao("EMPRESA_CONFIGURACAO_LISTAR"))
            return ResponseEntity.ok(empresaConfiguracaoService.listar(empresaRefId, pageable));
        else
            return ResponseEntity.status(403).build();
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover configuração da empresa",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Configuração removida"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Configuração não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{chave}")
    public ResponseEntity<Void> remover(
            @PathVariable Long empresaRefId,
            @PathVariable String chave
    ) {
        if (utilsService.verificarPermissao("EMPRESA_CONFIGURACAO_EXCLUIR")) {
            empresaConfiguracaoService.remover(empresaRefId, chave);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
