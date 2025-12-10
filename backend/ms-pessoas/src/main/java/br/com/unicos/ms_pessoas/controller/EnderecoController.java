package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.endereco.EnderecoListDTO;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoRequest;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoResponse;
import br.com.unicos.ms_pessoas.service.interfaces.EnderecoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento de endereços associados
 * a pessoas dentro do UniCoS.
 *
 * <p>Permite operações de criação, atualização, remoção e consultas
 * filtradas por pessoa, tipo de endereço, município, CEP e endereço
 * principal.</p>
 */
@RestController
@RequestMapping("/v1/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService service;

    // ============================================================
    // Criar
    // ============================================================

    /**
     * Cria um novo endereço para uma pessoa.
     *
     * @param request dados do endereço a ser cadastrado.
     * @return ResponseEntity com o endereço criado.
     */
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

    /**
     * Atualiza um endereço existente.
     *
     * @param id      identificador do endereço.
     * @param request dados atualizados.
     * @return ResponseEntity contendo o endereço atualizado.
     */
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

    /**
     * Remove um endereço pelo ID.
     *
     * @param id identificador do endereço.
     * @return ResponseEntity sem conteúdo (204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    /**
     * Busca um endereço pelo seu identificador.
     *
     * @param id identificador do endereço.
     * @return ResponseEntity contendo o endereço ou 404 caso não exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    /**
     * Lista todos os endereços cadastrados.
     *
     * @return lista simplificada de endereços.
     */
    @GetMapping
    public ResponseEntity<List<EnderecoListDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // ============================================================
    // Listar por Pessoa
    // ============================================================

    /**
     * Lista todos os endereços de uma pessoa específica.
     *
     * @param pessoaId identificador da pessoa.
     * @return lista de endereços da pessoa.
     */
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(service.listarPorPessoa(pessoaId));
    }

    // ============================================================
    // Listar por Pessoa e Tipo
    // ============================================================

    /**
     * Lista endereços de uma pessoa filtrados por tipo (Residencial, Comercial, etc.).
     *
     * @param pessoaId identificador da pessoa.
     * @param tipo     nome do tipo de endereço.
     * @return lista filtrada de endereços.
     */
    @GetMapping("/pessoa/{pessoaId}/tipo/{tipo}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorPessoaETipo(
            @PathVariable Long pessoaId,
            @PathVariable String tipo) {
        return ResponseEntity.ok(service.listarPorPessoaETipo(pessoaId, tipo));
    }

    // ============================================================
    // Listar por Município
    // ============================================================

    /**
     * Lista endereços pertencentes a um município específico.
     *
     * @param municipioId identificador do município.
     * @return lista de endereços localizados no município informado.
     */
    @GetMapping("/municipio/{municipioId}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorMunicipio(@PathVariable Long municipioId) {
        return ResponseEntity.ok(service.listarPorMunicipio(municipioId));
    }

    // ============================================================
    // Listar por CEP
    // ============================================================

    /**
     * Lista endereços filtrados por CEP.
     *
     * @param cep código postal.
     * @return lista de endereços com o CEP informado.
     */
    @GetMapping("/cep/{cep}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorCep(@PathVariable String cep) {
        return ResponseEntity.ok(service.listarPorCep(cep));
    }

    // ============================================================
    // Buscar Endereço Principal
    // ============================================================

    /**
     * Retorna o endereço principal de uma pessoa, se existir.
     *
     * @param pessoaId identificador da pessoa.
     * @return ResponseEntity contendo o endereço principal ou 404.
     */
    @GetMapping("/pessoa/{pessoaId}/principal")
    public ResponseEntity<EnderecoResponse> buscarPrincipal(@PathVariable Long pessoaId) {
        return service.buscarPrincipal(pessoaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
