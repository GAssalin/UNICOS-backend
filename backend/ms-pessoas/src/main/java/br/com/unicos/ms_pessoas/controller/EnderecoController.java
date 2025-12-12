package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.endereco.EnderecoListDTO;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoRequest;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoResponse;
import br.com.unicos.ms_pessoas.service.interfaces.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento de endereços associados
 * a pessoas dentro do UniCoS.
 * <p>
 * Permite operações de criação, atualização, remoção e consultas
 * filtradas por pessoa, tipo de endereço, município, CEP e endereço
 * principal.
 */
@RestController
@RequestMapping("/v1/enderecos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Endereços",
        description = "Operações de criação, atualização, exclusão e consultas de endereços vinculados a pessoas."
)
public class EnderecoController {

    private final EnderecoService service;

    // ============================================================
    // Criar
    // ============================================================

    @PreAuthorize("hasAuthority('ENDERECO_CRIAR')")
    @Operation(
            summary = "Criar novo endereço",
            description = "Registra um novo endereço vinculado a uma pessoa.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Endereço criado com sucesso",
                            content = @Content(schema = @Schema(implementation = EnderecoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados na requisição"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<EnderecoResponse> criar(@RequestBody EnderecoRequest request) {
        EnderecoResponse response = service.criar(request);
        return ResponseEntity
                .created(URI.create("/v1/enderecos/" + response.id()))
                .body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @PreAuthorize("hasAuthority('ENDERECO_EDITAR')")
    @Operation(
            summary = "Atualizar endereço existente",
            description = "Atualiza os dados de um endereço já cadastrado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Endereço atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = EnderecoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Endereço não encontrado"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody EnderecoRequest request) {

        EnderecoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    @PreAuthorize("hasAuthority('ENDERECO_EXCLUIR')")
    @Operation(
            summary = "Excluir endereço",
            description = "Remove permanentemente um endereço pelo seu ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Endereço excluído com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Endereço não encontrado"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @PreAuthorize("hasAuthority('ENDERECO_LISTAR')")
    @Operation(
            summary = "Buscar endereço por ID",
            description = "Retorna o endereço correspondente ao ID informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Endereço encontrado",
                            content = @Content(schema = @Schema(implementation = EnderecoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Endereço não encontrado"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    @PreAuthorize("hasAuthority('ENDERECO_LISTAR')")
    @Operation(
            summary = "Listar todos os endereços",
            description = "Retorna todos os endereços cadastrados no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnderecoListDTO.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<EnderecoListDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // ============================================================
    // Listar por Pessoa
    // ============================================================

    @PreAuthorize("hasAuthority('ENDERECO_LISTAR')")
    @Operation(
            summary = "Listar endereços por pessoa",
            description = "Retorna todos os endereços associados à pessoa informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnderecoListDTO.class)))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pessoa não encontrada ou sem endereços"
                    )
            }
    )
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(service.listarPorPessoa(pessoaId));
    }

    // ============================================================
    // Listar por Pessoa e Tipo
    // ============================================================

    @PreAuthorize("hasAuthority('ENDERECO_LISTAR')")
    @Operation(
            summary = "Listar endereços por pessoa e tipo",
            description = "Filtra os endereços de uma pessoa pelo tipo informado (Ex.: Residencial, Comercial).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista filtrada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnderecoListDTO.class)))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pessoa não encontrada"
                    )
            }
    )
    @GetMapping("/pessoa/{pessoaId}/tipo/{tipo}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorPessoaETipo(
            @PathVariable Long pessoaId,
            @PathVariable String tipo) {
        return ResponseEntity.ok(service.listarPorPessoaETipo(pessoaId, tipo));
    }

    // ============================================================
    // Listar por Município
    // ============================================================

    @PreAuthorize("hasAuthority('ENDERECO_LISTAR')")
    @Operation(
            summary = "Listar endereços por município",
            description = "Retorna endereços cadastrados no município informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnderecoListDTO.class)))
                    )
            }
    )
    @GetMapping("/municipio/{municipioId}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorMunicipio(@PathVariable Long municipioId) {
        return ResponseEntity.ok(service.listarPorMunicipio(municipioId));
    }

    // ============================================================
    // Listar por CEP
    // ============================================================

    @PreAuthorize("hasAuthority('ENDERECO_LISTAR')")
    @Operation(
            summary = "Listar endereços por CEP",
            description = "Retorna endereços filtrados pelo CEP informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista obtida com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnderecoListDTO.class)))
                    )
            }
    )
    @GetMapping("/cep/{cep}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorCep(@PathVariable String cep) {
        return ResponseEntity.ok(service.listarPorCep(cep));
    }

    // ============================================================
    // Buscar Endereço Principal
    // ============================================================

    @PreAuthorize("hasAuthority('ENDERECO_LISTAR')")
    @Operation(
            summary = "Buscar endereço principal",
            description = "Retorna o endereço principal definido para a pessoa.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Endereço principal encontrado",
                            content = @Content(schema = @Schema(implementation = EnderecoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Nenhum endereço principal encontrado para esta pessoa"
                    )
            }
    )
    @GetMapping("/pessoa/{pessoaId}/principal")
    public ResponseEntity<EnderecoResponse> buscarPrincipal(@PathVariable Long pessoaId) {
        return service.buscarPrincipal(pessoaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
