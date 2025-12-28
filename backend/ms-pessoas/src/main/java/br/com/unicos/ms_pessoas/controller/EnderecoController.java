package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.endereco.EnderecoListDTO;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoRequest;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoResponse;
import br.com.unicos.ms_pessoas.service.EnderecoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de endereços
 * vinculados a pessoas.
 *
 * <p>
 * Disponibiliza endpoints para criação, atualização, consulta,
 * listagem e exclusão de endereços, incluindo filtros por pessoa,
 * tipo, município, CEP e endereço principal.
 * </p>
 */
@RestController
@RequestMapping("/v1/enderecos")
@RequiredArgsConstructor
@Tag(
        name = "Endereços",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de endereços de pessoas."
)
public class EnderecoController {

    private final EnderecoService enderecoService;

    // =============================================================
    // CREATE
    // =============================================================

    @PostMapping
    public ResponseEntity<EnderecoResponse> criar(@Valid @RequestBody EnderecoRequest request) {
        EnderecoResponse response = enderecoService.criar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody EnderecoRequest request) {
        return ResponseEntity.ok(enderecoService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponse> buscarPorId(@PathVariable Long id) {
        Optional<EnderecoResponse> response = enderecoService.buscarPorId(id);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    /**
     * Lista todos os endereços cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<EnderecoListDTO>> listarTodos() {
        return ResponseEntity.ok(enderecoService.listarTodos());
    }

    /**
     * Lista endereços vinculados a uma pessoa.
     */
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(enderecoService.listarPorPessoa(pessoaId));
    }

    /**
     * Lista endereços de uma pessoa filtrando por tipo.
     */
    @GetMapping("/pessoa/{pessoaId}/tipo/{tipo}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorPessoaETipo(@PathVariable Long pessoaId, @PathVariable String tipo) {
        return ResponseEntity.ok(enderecoService.listarPorPessoaETipo(pessoaId, tipo));
    }

    /**
     * Lista endereços filtrando por município.
     */
    @GetMapping("/municipio/{municipioId}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorMunicipio(@PathVariable Long municipioId) {
        return ResponseEntity.ok(enderecoService.listarPorMunicipio(municipioId));
    }

    /**
     * Lista endereços filtrando por CEP.
     */
    @GetMapping("/cep/{cep}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorCep(@PathVariable String cep) {
        return ResponseEntity.ok(enderecoService.listarPorCep(cep));
    }

    /**
     * Busca o endereço principal de uma pessoa.
     */
    @GetMapping("/pessoa/{pessoaId}/principal")
    public ResponseEntity<EnderecoResponse> buscarPrincipal(@PathVariable Long pessoaId) {
        Optional<EnderecoResponse> response =
                enderecoService.buscarPrincipal(pessoaId);

        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // DELETE
    // =============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        enderecoService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
