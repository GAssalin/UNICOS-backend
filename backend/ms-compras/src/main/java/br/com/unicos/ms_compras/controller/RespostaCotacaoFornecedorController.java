package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.RespostaCotacaoFornecedorDto;
import br.com.unicos.ms_compras.service.RespostaCotacaoFornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints de Resposta de Cotação do Fornecedor.
 */
@RestController
@RequestMapping("/v1/respostas-cotacao-fornecedor")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Respostas de Cotação (Fornecedor)",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de respostas de cotação por fornecedor."
)
public class RespostaCotacaoFornecedorController {

    private final RespostaCotacaoFornecedorService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar resposta de cotação do fornecedor",
            description = "Cria uma resposta de cotação vinculada a uma cotação e fornecedor dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Resposta criada com sucesso",
                            content = @Content(schema = @Schema(implementation = RespostaCotacaoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<RespostaCotacaoFornecedorDto> salvar(@Valid @RequestBody RespostaCotacaoFornecedorDto request) {
        RespostaCotacaoFornecedorDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar resposta de cotação do fornecedor",
            description = "Atualiza os dados de uma resposta de cotação existente. Não permite trocar cotacaoCompraId/fornecedorId via este endpoint.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resposta atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = RespostaCotacaoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Resposta não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<RespostaCotacaoFornecedorDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RespostaCotacaoFornecedorDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET
    // =============================================================

    @Operation(
            summary = "Buscar resposta por ID",
            description = "Retorna os dados de uma resposta de cotação do fornecedor.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = RespostaCotacaoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Resposta não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RespostaCotacaoFornecedorDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar resposta por cotação e fornecedor",
            description = "Busca uma resposta única pela combinação (cotacaoCompraId, fornecedorId) dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = RespostaCotacaoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Resposta não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/cotacao/{cotacaoCompraId}/fornecedor/{fornecedorId}")
    public ResponseEntity<RespostaCotacaoFornecedorDto> buscarPorCotacaoEFornecedor(
            @PathVariable Long cotacaoCompraId,
            @PathVariable Long fornecedorId
    ) {
        return ResponseEntity.ok(service.buscarPorCotacaoEFornecedor(cotacaoCompraId, fornecedorId));
    }

    // =============================================================
    // LIST
    // =============================================================

    @Operation(
            summary = "Listar respostas de cotação do fornecedor",
            description = "Lista respostas de cotação de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RespostaCotacaoFornecedorDto.class)))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<RespostaCotacaoFornecedorDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Listar respostas por cotação",
            description = "Lista respostas vinculadas a uma cotação (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RespostaCotacaoFornecedorDto.class)))
                    ),
                    @ApiResponse(responseCode = "404", description = "Cotação não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/cotacao/{cotacaoCompraId}")
    public ResponseEntity<Page<RespostaCotacaoFornecedorDto>> listarPorCotacao(
            @PathVariable Long cotacaoCompraId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorCotacao(cotacaoCompraId, pageable));
    }

    @Operation(
            summary = "Listar respostas por fornecedor",
            description = "Lista respostas vinculadas a um fornecedor (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RespostaCotacaoFornecedorDto.class)))
                    ),
                    @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<Page<RespostaCotacaoFornecedorDto>> listarPorFornecedor(
            @PathVariable Long fornecedorId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorFornecedor(fornecedorId, pageable));
    }

    @Operation(
            summary = "Listar respostas por status",
            description = "Lista respostas por status (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RespostaCotacaoFornecedorDto.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<RespostaCotacaoFornecedorDto>> listarPorStatus(
            @PathVariable String status,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorStatus(status, pageable));
    }

    @Operation(
            summary = "Listar respostas por condição de pagamento",
            description = "Lista respostas por condição de pagamento (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RespostaCotacaoFornecedorDto.class)))
                    ),
                    @ApiResponse(responseCode = "404", description = "Condição de pagamento não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/condicao-pagamento/{condicaoPagamentoId}")
    public ResponseEntity<Page<RespostaCotacaoFornecedorDto>> listarPorCondicaoPagamento(
            @PathVariable Long condicaoPagamentoId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorCondicaoPagamento(condicaoPagamentoId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover resposta de cotação do fornecedor",
            description = "Remove uma resposta de cotação do fornecedor pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resposta removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Resposta não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok().build();
    }
}