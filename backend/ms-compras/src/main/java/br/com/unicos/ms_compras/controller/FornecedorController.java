package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.FornecedorDto;
import br.com.unicos.ms_compras.service.FornecedorService;
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
 * Controller responsável pelos endpoints de Fornecedores.
 */
@RestController
@RequestMapping("/v1/fornecedores")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Fornecedores",
        description = "Endpoints para criação, atualização, consulta, listagem, pesquisa e remoção de fornecedores."
)
public class FornecedorController {

    private final FornecedorService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar fornecedor",
            description = "Cria um novo fornecedor dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Fornecedor criado com sucesso",
                            content = @Content(schema = @Schema(implementation = FornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<FornecedorDto> salvar(@Valid @RequestBody FornecedorDto request) {
        FornecedorDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar fornecedor",
            description = "Atualiza os dados de um fornecedor existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Fornecedor atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = FornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FornecedorDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET
    // =============================================================

    @Operation(
            summary = "Buscar fornecedor por ID",
            description = "Retorna os dados de um fornecedor específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = FornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar fornecedor por código",
            description = "Retorna os dados de um fornecedor pelo código (dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = FornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<FornecedorDto> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(service.buscarPorCodigo(codigo));
    }

    @Operation(
            summary = "Buscar fornecedor por CNPJ",
            description = "Retorna os dados de um fornecedor pelo CNPJ (dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = FornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<FornecedorDto> buscarPorCnpj(@PathVariable String cnpj) {
        return ResponseEntity.ok(service.buscarPorCnpj(cnpj));
    }

    // =============================================================
    // LISTAGENS / PESQUISAS
    // =============================================================

    @Operation(
            summary = "Listar fornecedores",
            description = "Lista fornecedores de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = FornecedorDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<FornecedorDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Pesquisar fornecedor por razão social",
            description = "Pesquisa fornecedores por razão social (contendo, case insensitive) de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = FornecedorDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pesquisa/razao-social")
    public ResponseEntity<Page<FornecedorDto>> pesquisarPorRazaoSocial(
            @RequestParam String razaoSocial,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.pesquisarPorRazaoSocial(razaoSocial, pageable));
    }

    @Operation(
            summary = "Pesquisar fornecedor por nome fantasia",
            description = "Pesquisa fornecedores por nome fantasia (contendo, case insensitive) de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = FornecedorDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pesquisa/nome-fantasia")
    public ResponseEntity<Page<FornecedorDto>> pesquisarPorNomeFantasia(
            @RequestParam String nomeFantasia,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.pesquisarPorNomeFantasia(nomeFantasia, pageable));
    }

    @Operation(
            summary = "Pesquisar fornecedor por CNPJ (parcial)",
            description = "Pesquisa fornecedores por CNPJ (contendo) de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = FornecedorDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pesquisa/cnpj")
    public ResponseEntity<Page<FornecedorDto>> pesquisarPorCnpj(
            @RequestParam String cnpj,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.pesquisarPorCnpj(cnpj, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover fornecedor",
            description = "Remove um fornecedor pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Fornecedor removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
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