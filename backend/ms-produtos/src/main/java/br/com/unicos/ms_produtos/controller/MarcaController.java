package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.MarcaListDTO;
import br.com.unicos.ms_produtos.dto.MarcaRequest;
import br.com.unicos.ms_produtos.dto.MarcaResponse;
import br.com.unicos.ms_produtos.service.MarcaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das marcas.
 *
 * Fornece endpoints para operações de CRUD e consultas específicas.
 */
@RestController
@RequestMapping("/v1/marcas")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria uma nova marca.
     *
     * @param request Dados da marca a ser criada.
     * @return MarcaResponse representando a marca criada.
     */
    @PostMapping
    public ResponseEntity<MarcaResponse> criarMarca(@Valid @RequestBody MarcaRequest request) {
        MarcaResponse response = marcaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de uma marca existente.
     *
     * @param id      Identificador da marca.
     * @param request Dados atualizados.
     * @return MarcaResponse atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponse> atualizarMarca(@PathVariable Long id,
                                                        @Valid @RequestBody MarcaRequest request) {
        MarcaResponse response = marcaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma marca pelo ID.
     *
     * @param id Identificador da marca.
     * @return MarcaResponse encontrada, se existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponse> buscarPorId(@PathVariable Long id) {
        return marcaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as marcas cadastradas.
     *
     * @return Lista completa de MarcaResponse.
     */
    @GetMapping
    public ResponseEntity<List<MarcaResponse>> listarTodas() {
        List<MarcaResponse> marcas = marcaService.listarTodas();
        return ResponseEntity.ok(marcas);
    }

    /**
     * Lista as marcas em formato simplificado (id + nome).
     *
     * @return Lista simplificada de MarcaListDTO.
     */
    @GetMapping("/simples")
    public ResponseEntity<List<MarcaListDTO>> listarSimples() {
        List<MarcaListDTO> marcas = marcaService.listarSimples();
        return ResponseEntity.ok(marcas);
    }

    /**
     * Busca marcas pelo nome (parcial ou completo).
     *
     * @param nome Termo de busca.
     * @return Lista de MarcaResponse correspondentes.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<MarcaResponse>> buscarPorNome(@RequestParam String nome) {
        List<MarcaResponse> marcas = marcaService.buscarPorNome(nome);
        return ResponseEntity.ok(marcas);
    }

    /**
     * Exclui uma marca pelo ID.
     *
     * @param id Identificador da marca.
     * @return Resposta 204 (sem conteúdo) se a exclusão for bem-sucedida.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMarca(@PathVariable Long id) {
        marcaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica se já existe uma marca com o nome informado.
     *
     * @param nome Nome da marca.
     * @return true se já existir, false caso contrário.
     */
    @GetMapping("/verificar-nome")
    public ResponseEntity<Boolean> verificarNome(@RequestParam String nome) {
        boolean existe = marcaService.existePorNome(nome);
        return ResponseEntity.ok(existe);
    }
}