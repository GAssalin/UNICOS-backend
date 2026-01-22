package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.contato.ContatoListDTO;
import br.com.unicos.ms_pessoas.dto.contato.ContatoRequest;
import br.com.unicos.ms_pessoas.dto.contato.ContatoResponse;
import br.com.unicos.ms_pessoas.enums.TipoContato;
import br.com.unicos.ms_pessoas.service.ContatoService;
import br.com.unicos.ms_pessoas.service.UtilsService;
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
@RequestMapping("/v1/contatos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Contatos",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de contatos de pessoas."
)
public class ContatoController {

    private final UtilsService utilsService;
    private final ContatoService contatoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar contato",
            description = "Cria um novo contato para uma pessoa.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Contato criado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = ContatoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ContatoResponse> salvar(@Valid @RequestBody ContatoRequest request) {
        if (utilsService.verificarPermissao("PESSOA_CONTATO_CRIAR"))
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(contatoService.salvar(request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar contato",
            description = "Atualiza os dados de um contato existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contato atualizado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = ContatoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ContatoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContatoRequest request
    ) {
        if (utilsService.verificarPermissao("PESSOA_CONTATO_EDITAR"))
            return ResponseEntity.ok(contatoService.atualizar(id, request));
        else
            return ResponseEntity.status(403).build();
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
                            content = @Content(
                                    schema = @Schema(implementation = ContatoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponse> buscarPorId(@PathVariable Long id) {
        if (utilsService.verificarPermissao("PESSOA_CONTATO_LISTAR"))
            return ResponseEntity.ok(contatoService.buscarPorId(id));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar contatos por pessoa",
            description = "Lista os contatos vinculados a uma pessoa de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ContatoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<Page<ContatoListDTO>> listarPorPessoa(
            @PathVariable Long pessoaId,
            @ParameterObject Pageable pageable
    ) {
        if (utilsService.verificarPermissao("PESSOA_CONTATO_LISTAR"))
            return ResponseEntity.ok(contatoService.listarPorPessoa(pessoaId, pageable));
        else
            return ResponseEntity.status(403).build();
    }

    @Operation(
            summary = "Listar contatos por pessoa e tipo",
            description = "Lista os contatos de uma pessoa filtrando pelo tipo de contato.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ContatoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pessoa/{pessoaId}/tipo/{tipo}")
    public ResponseEntity<Page<ContatoListDTO>> listarPorPessoaETipo(
            @PathVariable Long pessoaId,
            @PathVariable TipoContato tipo,
            @ParameterObject Pageable pageable
    ) {
        if (utilsService.verificarPermissao("PESSOA_CONTATO_LISTAR"))
            return ResponseEntity.ok(contatoService.listarPorPessoaETipo(pessoaId, tipo, pageable));
        else
            return ResponseEntity.status(403).build();
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
        if (utilsService.verificarPermissao("PESSOA_CONTATO_EXCLUIR")) {
            contatoService.deletar(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
