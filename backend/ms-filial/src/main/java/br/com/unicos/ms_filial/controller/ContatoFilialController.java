package br.com.unicos.ms_filial.controller;

import br.com.unicos.ms_filial.dto.contato.ContatoFilialCreateRequest;
import br.com.unicos.ms_filial.dto.contato.ContatoFilialResponse;
import br.com.unicos.ms_filial.dto.contato.ContatoFilialUpdateRequest;
import br.com.unicos.ms_filial.service.ContatoFilialService;
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

@RestController
@RequestMapping("/v1/contatos-filial")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Contatos de Filial",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de contatos de filiais."
)
public class ContatoFilialController {

    private final ContatoFilialService contatoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar contato de filial",
            description = "Cria um novo contato para uma filial.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Contato criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ContatoFilialResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ContatoFilialResponse> salvar(@Valid @RequestBody ContatoFilialCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoService.salvar(request));
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar contato de filial",
            description = "Atualiza os dados de um contato de filial existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contato atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ContatoFilialResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ContatoFilialResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContatoFilialUpdateRequest request
    ) {
        return ResponseEntity.ok(contatoService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar contato de filial por ID",
            description = "Retorna os dados de um contato de filial específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ContatoFilialResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ContatoFilialResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(contatoService.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar contatos por filial",
            description = "Lista os contatos vinculados a uma filial de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ContatoFilialResponse.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/filial/{filialId}")
    public ResponseEntity<Page<ContatoFilialResponse>> listarPorFilial(
            @PathVariable Long filialId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(contatoService.listarPorFilial(filialId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover contato de filial",
            description = "Remove um contato de filial pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contato removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contatoService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
