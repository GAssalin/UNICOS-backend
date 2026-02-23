package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.ContatoFornecedorDto;
import br.com.unicos.ms_compras.service.ContatoFornecedorService;
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
 * Controller responsável pelos endpoints de Contatos de Fornecedor.
 */
@RestController
@RequestMapping("/v1/fornecedores/contatos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Fornecedores - Contatos",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de contatos de fornecedor."
)
public class ContatoFornecedorController {

    private final ContatoFornecedorService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar contato do fornecedor",
            description = "Cria um novo contato vinculado a um fornecedor.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Contato criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ContatoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ContatoFornecedorDto> salvar(@Valid @RequestBody ContatoFornecedorDto request) {
        ContatoFornecedorDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar contato do fornecedor",
            description = "Atualiza os dados de um contato existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contato atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ContatoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ContatoFornecedorDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContatoFornecedorDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar contato por ID",
            description = "Retorna os dados de um contato específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ContatoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ContatoFornecedorDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar contatos",
            description = "Lista contatos de fornecedores de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ContatoFornecedorDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<ContatoFornecedorDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Listar contatos por fornecedor",
            description = "Lista contatos de um fornecedor específico de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ContatoFornecedorDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<Page<ContatoFornecedorDto>> listarPorFornecedor(
            @PathVariable Long fornecedorId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorFornecedor(fornecedorId, pageable));
    }

    @Operation(
            summary = "Buscar contato principal do fornecedor",
            description = "Retorna o contato principal de um fornecedor (dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ContatoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado ou sem contato principal"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/fornecedor/{fornecedorId}/principal")
    public ResponseEntity<ContatoFornecedorDto> buscarPrincipal(@PathVariable Long fornecedorId) {
        return ResponseEntity.ok(service.buscarPrincipal(fornecedorId));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover contato",
            description = "Remove um contato pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contato removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
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