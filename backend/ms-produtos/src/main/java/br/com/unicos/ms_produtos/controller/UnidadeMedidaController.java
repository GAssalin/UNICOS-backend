package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.UnidadeMedidaListDTO;
import br.com.unicos.ms_produtos.dto.UnidadeMedidaRequest;
import br.com.unicos.ms_produtos.dto.UnidadeMedidaResponse;
import br.com.unicos.ms_produtos.service.UnidadeMedidaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das unidades de medida.
 *
 * Fornece endpoints para criação, atualização, listagem, exclusão
 * e consultas específicas por nome ou sigla.
 */
@RestController
@RequestMapping("/v1/unidades-medida")
public class UnidadeMedidaController {

    private final UnidadeMedidaService unidadeMedidaService;

    public UnidadeMedidaController(UnidadeMedidaService unidadeMedidaService) {
        this.unidadeMedidaService = unidadeMedidaService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria uma nova unidade de medida.
     *
     * @param request Dados da unidade de medida.
     * @return UnidadeMedidaResponse criada.
     */
    @PostMapping
    public ResponseEntity<UnidadeMedidaResponse> criar(@Valid @RequestBody UnidadeMedidaRequest request) {
        UnidadeMedidaResponse response = unidadeMedidaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma unidade de medida existente.
     *
     * @param id      Identificador da unidade.
     * @param request Dados atualizados.
     * @return UnidadeMedidaResponse atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeMedidaResponse> atualizar(@PathVariable Long id,
                                                           @Valid @RequestBody UnidadeMedidaRequest request) {
        UnidadeMedidaResponse response = unidadeMedidaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma unidade de medida pelo ID.
     *
     * @param id Identificador da unidade.
     * @return UnidadeMedidaResponse, se encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorId(@PathVariable Long id) {
        return unidadeMedidaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as unidades de medida cadastradas.
     *
     * @return Lista de UnidadeMedidaResponse.
     */
    @GetMapping
    public ResponseEntity<List<UnidadeMedidaResponse>> listarTodas() {
        List<UnidadeMedidaResponse> lista = unidadeMedidaService.listarTodas();
        return ResponseEntity.ok(lista);
    }

    /**
     * Exclui uma unidade de medida pelo ID.
     *
     * @param id Identificador da unidade.
     * @return Resposta 204 (sem conteúdo) em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        unidadeMedidaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Busca uma unidade de medida pelo nome.
     *
     * @param nome Nome da unidade.
     * @return UnidadeMedidaResponse, se encontrada.
     */
    @GetMapping("/buscar/nome")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorNome(@RequestParam String nome) {
        return unidadeMedidaService.buscarPorNome(nome)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca uma unidade de medida pela sigla.
     *
     * @param sigla Sigla da unidade.
     * @return UnidadeMedidaResponse, se encontrada.
     */
    @GetMapping("/buscar/sigla")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorSigla(@RequestParam String sigla) {
        return unidadeMedidaService.buscarPorSigla(sigla)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca unidades cujo nome contenha o termo informado.
     *
     * @param nome Termo de busca.
     * @return Lista de UnidadeMedidaListDTO.
     */
    @GetMapping("/buscar/contem")
    public ResponseEntity<List<UnidadeMedidaListDTO>> buscarPorNomeContendo(@RequestParam String nome) {
        List<UnidadeMedidaListDTO> lista = unidadeMedidaService.buscarPorNomeContendo(nome);
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista todas as unidades de medida (modo simplificado).
     *
     * @return Lista de UnidadeMedidaListDTO.
     */
    @GetMapping("/simples")
    public ResponseEntity<List<UnidadeMedidaListDTO>> listarSimples() {
        List<UnidadeMedidaListDTO> lista = unidadeMedidaService.listarSimples();
        return ResponseEntity.ok(lista);
    }

    /**
     * Verifica se uma sigla já está cadastrada.
     *
     * @param sigla Sigla a ser verificada.
     * @return true se já existir, false caso contrário.
     */
    @GetMapping("/verificar-sigla")
    public ResponseEntity<Boolean> verificarSigla(@RequestParam String sigla) {
        boolean existe = unidadeMedidaService.verificarSiglaExistente(sigla);
        return ResponseEntity.ok(existe);
    }
}