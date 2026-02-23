package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.EnderecoFornecedorDto;
import br.com.unicos.ms_compras.service.EnderecoFornecedorService;
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
 * Controller responsável pelos endpoints de Endereços de Fornecedor.
 */
@RestController
@RequestMapping("/v1/fornecedores/enderecos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Fornecedores - Endereços",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de endereços de fornecedor."
)
public class EnderecoFornecedorController {

    private final EnderecoFornecedorService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar endereço do fornecedor",
            description = "Cria um novo endereço vinculado a um fornecedor.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Endereço criado com sucesso",
                            content = @Content(schema = @Schema(implementation = EnderecoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<EnderecoFornecedorDto> salvar(@Valid @RequestBody EnderecoFornecedorDto request) {
        EnderecoFornecedorDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar endereço do fornecedor",
            description = "Atualiza os dados de um endereço existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Endereço atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = EnderecoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoFornecedorDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EnderecoFornecedorDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar endereço por ID",
            description = "Retorna os dados de um endereço específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = EnderecoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoFornecedorDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar endereços de fornecedor",
            description = "Lista endereços de fornecedor de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = EnderecoFornecedorDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<EnderecoFornecedorDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Listar endereços por fornecedor",
            description = "Lista endereços de um fornecedor específico de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = EnderecoFornecedorDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<Page<EnderecoFornecedorDto>> listarPorFornecedor(
            @PathVariable Long fornecedorId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorFornecedor(fornecedorId, pageable));
    }

    @Operation(
            summary = "Listar endereços por CEP",
            description = "Lista endereços por CEP de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = EnderecoFornecedorDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/cep/{cep}")
    public ResponseEntity<Page<EnderecoFornecedorDto>> listarPorCep(
            @PathVariable String cep,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorCep(cep, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover endereço",
            description = "Remove um endereço pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereço removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Remover endereços por fornecedor",
            description = "Remove todos os endereços vinculados a um fornecedor (dentro do tenant).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereços removidos com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<Void> deletarPorFornecedor(@PathVariable Long fornecedorId) {
        service.deletarPorFornecedor(fornecedorId);
        return ResponseEntity.ok().build();
    }
}