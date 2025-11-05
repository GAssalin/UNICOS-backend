package br.com.unicos.ms_ativos.controller;

import br.com.unicos.ms_ativos.dto.LocalizacaoListDTO;
import br.com.unicos.ms_ativos.dto.LocalizacaoRequest;
import br.com.unicos.ms_ativos.dto.LocalizacaoResponse;
import br.com.unicos.ms_ativos.model.HistoricoAtivo;
import br.com.unicos.ms_ativos.service.LocalizacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento das localizações físicas
 * utilizadas para mapeamento dos ativos dentro das filiais.
 * <p>
 * Permite operações de criação, atualização, exclusão e consultas específicas
 * por filial, descrição e quantidade de ativos vinculados.
 */
@RestController
@RequestMapping("/v1/localizacoes")
@RequiredArgsConstructor
public class LocalizacaoController {

    private final LocalizacaoService localizacaoService;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Registra uma nova localização física.
     *
     * @param request DTO contendo os dados da localização.
     * @return resposta com os dados da localização criada.
     */
    @PostMapping
    public ResponseEntity<LocalizacaoResponse> salvar(@Valid @RequestBody LocalizacaoRequest request) {
        LocalizacaoResponse response = localizacaoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma localização existente.
     *
     * @param id      identificador da localização.
     * @param request DTO contendo os novos dados.
     * @return resposta com os dados atualizados.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LocalizacaoResponse> atualizar(@PathVariable Long id,
                                                         @Valid @RequestBody LocalizacaoRequest request) {
        LocalizacaoResponse response = localizacaoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui uma localização com base no seu ID.
     *
     * @param id identificador da localização.
     * @return status HTTP 204 em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        localizacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca uma localização específica pelo seu ID.
     *
     * @param id identificador da localização.
     * @return resposta com os dados detalhados, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LocalizacaoResponse> buscarPorId(@PathVariable Long id) {
        Optional<LocalizacaoResponse> response = localizacaoService.buscarPorId(id);
        return response.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as localizações cadastradas no sistema.
     *
     * @return lista de localizações.
     */
    @GetMapping
    public ResponseEntity<List<LocalizacaoListDTO>> listarTodos() {
        List<LocalizacaoListDTO> lista = localizacaoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna as localizações pertencentes a uma filial específica.
     *
     * @param filialId identificador da filial.
     * @return lista de localizações da filial.
     */
    @GetMapping("/filial/{filialId}")
    public ResponseEntity<List<LocalizacaoListDTO>> buscarPorFilial(@PathVariable Long filialId) {
        List<LocalizacaoListDTO> lista = localizacaoService.buscarPorFilial(filialId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna localizações cujo nome contenha o termo informado.
     *
     * @param descricao termo parcial da descrição.
     * @return lista de localizações correspondentes.
     */
    @GetMapping("/descricao")
    public ResponseEntity<List<LocalizacaoListDTO>> buscarPorDescricao(@RequestParam String descricao) {
        List<LocalizacaoListDTO> lista = localizacaoService.buscarPorDescricao(descricao);
        return ResponseEntity.ok(lista);
    }

    /**
     * Verifica se já existe uma localização com a descrição informada dentro de uma filial.
     *
     * @param descricao descrição a verificar.
     * @param filialId  identificador da filial.
     * @return true se existir, false caso contrário.
     */
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existePorDescricaoEFilial(@RequestParam String descricao,
                                                             @RequestParam Long filialId) {
        boolean existe = localizacaoService.existePorDescricaoEFilial(descricao, filialId);
        return ResponseEntity.ok(existe);
    }

    /**
     * Retorna localizações que possuem mais de um ativo vinculado.
     *
     * @return lista de localizações com múltiplos ativos.
     */
    @GetMapping("/com-mais-de-um-ativo")
    public ResponseEntity<List<LocalizacaoListDTO>> buscarComMaisDeUmAtivo() {
        List<LocalizacaoListDTO> lista = localizacaoService.buscarComMaisDeUmAtivo();
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna localizações que possuem ativos associados.
     *
     * @return lista de localizações com ativos.
     */
    @GetMapping("/com-ativos")
    public ResponseEntity<List<LocalizacaoListDTO>> buscarComAtivos() {
        List<LocalizacaoListDTO> lista = localizacaoService.buscarComAtivos();
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna localizações que não possuem ativos associados.
     *
     * @return lista de localizações sem ativos.
     */
    @GetMapping("/sem-ativos")
    public ResponseEntity<List<LocalizacaoListDTO>> buscarSemAtivos() {
        List<LocalizacaoListDTO> lista = localizacaoService.buscarSemAtivos();
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna a contagem total de localizações registradas no sistema.
     *
     * @return número total de localizações.
     */
    @GetMapping("/total")
    public ResponseEntity<Long> contarTotal() {
        Long total = localizacaoService.contarTotal();
        return ResponseEntity.ok(total);
    }

    /**
     * Retorna a quantidade de localizações registradas por filial.
     *
     * @param filialId identificador da filial.
     * @return número de localizações vinculadas à filial.
     */
    @GetMapping("/filial/{filialId}/total")
    public ResponseEntity<Long> contarPorFilial(@PathVariable Long filialId) {
        Long total = localizacaoService.contarPorFilial(filialId);
        return ResponseEntity.ok(total);
    }

    // ===========================================================
    // 🕓 RELATÓRIO DE EVENTOS RECENTES (PLACEHOLDER)
    // ===========================================================

    /**
     * Retorna eventos recentes relacionados às localizações.
     * <p>
     * OBS: este método é um placeholder para futuras integrações
     * com o módulo de histórico de ativos.
     *
     * @return lista vazia ou eventos conforme implementação futura.
     */
    @GetMapping("/eventos-recentes")
    public ResponseEntity<List<HistoricoAtivo>> buscarEventosRecentes() {
        List<HistoricoAtivo> eventos = localizacaoService.buscarEventosRecentes();
        return ResponseEntity.ok(eventos);
    }
}
