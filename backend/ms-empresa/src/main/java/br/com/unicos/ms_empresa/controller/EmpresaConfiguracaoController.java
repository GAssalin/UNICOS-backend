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
 * das configurações globais da empresa.
 *
 * <p>Todos os endpoints são restritos ao tenant
 * e respeitam as permissões do usuário autenticado.</p>
 */
@RestController
@RequestMapping("/v1/empresas/{empresaRefId}/configuracoes")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Configurações da Empresa",
        description = "Endpoints para gerenciamento de configurações globais da empresa."
)
public class EmpresaConfiguracaoController {

    private final EmpresaConfiguracaoService empresaConfiguracaoService;

    @Operation(
            summary = "Criar configuração da empresa",
            description = "Cria uma nova configuração global para a empresa informada.",
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
            @PathVariable @Positive Long empresaRefId,
            @RequestBody @Valid EmpresaConfiguracaoCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaConfiguracaoService.criar(normalizarCreateRequest(request, empresaRefId)));
    }

    @Operation(
            summary = "Atualizar configuração da empresa",
            description = "Atualiza uma configuração existente por chave.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Configuração atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaConfiguracaoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Configuração não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{chave}")
    public ResponseEntity<EmpresaConfiguracaoResponse> atualizar(
            @PathVariable @Positive Long empresaRefId,
            @PathVariable String chave,
            @RequestBody @Valid EmpresaConfiguracaoUpdateRequest request
    ) {
        return ResponseEntity.ok(empresaConfiguracaoService.atualizar(empresaRefId, chave, request));
    }

    @Operation(
            summary = "Buscar configuração por chave",
            description = "Busca uma configuração específica da empresa pela chave.",
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
            @PathVariable @Positive Long empresaRefId,
            @PathVariable String chave
    ) {
        return ResponseEntity.ok(empresaConfiguracaoService.buscarPorChave(empresaRefId, chave));
    }

    @Operation(
            summary = "Listar configurações da empresa",
            description = "Lista todas as configurações da empresa de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = EmpresaConfiguracaoResumoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<EmpresaConfiguracaoResumoResponse>> listar(
            @PathVariable @Positive Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaConfiguracaoService.listar(empresaRefId, pageable));
    }

    @Operation(
            summary = "Remover configuração da empresa",
            description = "Remove uma configuração da empresa pela chave.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Configuração removida com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Configuração não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{chave}")
    public ResponseEntity<Void> remover(
            @PathVariable @Positive Long empresaRefId,
            @PathVariable String chave
    ) {
        empresaConfiguracaoService.remover(empresaRefId, chave);
        return ResponseEntity.noContent().build();
    }

    private EmpresaConfiguracaoCreateRequest normalizarCreateRequest(
            EmpresaConfiguracaoCreateRequest request,
            Long empresaRefId
    ) {
        return new EmpresaConfiguracaoCreateRequest(
                request.empresaId(),
                empresaRefId,
                request.chave(),
                request.valor()
        );
    }
}