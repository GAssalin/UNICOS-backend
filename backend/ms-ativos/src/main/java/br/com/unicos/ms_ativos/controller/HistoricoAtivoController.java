package br.com.unicos.ms_ativos.controller;

import br.com.unicos.ms_ativos.dto.HistoricoAtivoListDTO;
import br.com.unicos.ms_ativos.dto.HistoricoAtivoRequest;
import br.com.unicos.ms_ativos.dto.HistoricoAtivoResponse;
import br.com.unicos.ms_ativos.service.HistoricoAtivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento dos históricos de eventos dos ativos patrimoniais.
 * <p>
 * Permite registrar, atualizar, excluir e consultar eventos como transferências, alterações de status,
 * manutenções e outras ações relevantes vinculadas aos ativos.
 */
@RestController
@RequestMapping("/v1/historicos-ativos")
@RequiredArgsConstructor
public class HistoricoAtivoController {

    private final HistoricoAtivoService historicoAtivoService;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Registra um novo evento no histórico de um ativo.
     *
     * @param request DTO contendo os dados do evento.
     * @return resposta com os dados do evento criado.
     */
    @PostMapping
    public ResponseEntity<HistoricoAtivoResponse> salvar(@Valid @RequestBody HistoricoAtivoRequest request) {
        HistoricoAtivoResponse response = historicoAtivoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um evento existente no histórico.
     *
     * @param id      identificador do evento.
     * @param request DTO contendo os novos dados.
     * @return resposta com os dados atualizados.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HistoricoAtivoResponse> atualizar(@PathVariable Long id,
                                                            @Valid @RequestBody HistoricoAtivoRequest request) {
        HistoricoAtivoResponse response = historicoAtivoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui um registro do histórico.
     *
     * @param id identificador do evento.
     * @return status HTTP 204 em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        historicoAtivoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca um evento específico pelo ID.
     *
     * @param id identificador do evento.
     * @return resposta com os detalhes do evento, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoricoAtivoResponse> buscarPorId(@PathVariable Long id) {
        return historicoAtivoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os registros de histórico de ativos.
     *
     * @return lista de eventos registrados.
     */
    @GetMapping
    public ResponseEntity<List<HistoricoAtivoListDTO>> listarTodos() {
        List<HistoricoAtivoListDTO> lista = historicoAtivoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna os eventos de um ativo específico.
     *
     * @param ativoId identificador do ativo.
     * @return lista de eventos do ativo.
     */
    @GetMapping("/ativo/{ativoId}")
    public ResponseEntity<List<HistoricoAtivoListDTO>> buscarPorAtivo(@PathVariable Long ativoId) {
        List<HistoricoAtivoListDTO> lista = historicoAtivoService.buscarPorAtivo(ativoId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna os eventos registrados por um usuário responsável.
     *
     * @param usuarioId identificador do usuário.
     * @return lista de eventos realizados pelo usuário.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<HistoricoAtivoListDTO>> buscarPorUsuarioResponsavel(@PathVariable Long usuarioId) {
        List<HistoricoAtivoListDTO> lista = historicoAtivoService.buscarPorUsuarioResponsavel(usuarioId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna eventos registrados em um período específico.
     *
     * @param inicio data e hora inicial.
     * @param fim    data e hora final.
     * @return lista de eventos dentro do intervalo.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<HistoricoAtivoListDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        List<HistoricoAtivoListDTO> lista = historicoAtivoService.buscarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna o último evento registrado para um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return último evento do ativo, se existente.
     */
    @GetMapping("/ativo/{ativoId}/ultimo")
    public ResponseEntity<HistoricoAtivoResponse> buscarUltimoEventoPorAtivo(@PathVariable Long ativoId) {
        Optional<HistoricoAtivoResponse> response = historicoAtivoService.buscarUltimoEventoPorAtivo(ativoId);
        return response.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retorna eventos relacionados a alterações de status de um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return lista de eventos de mudança de status.
     */
    @GetMapping("/ativo/{ativoId}/eventos-status")
    public ResponseEntity<List<HistoricoAtivoListDTO>> buscarEventosDeStatus(@PathVariable Long ativoId) {
        List<HistoricoAtivoListDTO> lista = historicoAtivoService.buscarEventosDeStatus(ativoId);
        return ResponseEntity.ok(lista);
    }

    // ===========================================================
    // 📊 RELATÓRIOS E ANÁLISE
    // ===========================================================

    /**
     * Retorna a contagem total de eventos registrados.
     *
     * @return número total de eventos.
     */
    @GetMapping("/total")
    public ResponseEntity<Long> contarTotalEventos() {
        Long total = historicoAtivoService.contarTotalEventos();
        return ResponseEntity.ok(total);
    }

    /**
     * Retorna a quantidade de eventos de um ativo específico.
     *
     * @param ativoId identificador do ativo.
     * @return número de eventos do ativo.
     */
    @GetMapping("/ativo/{ativoId}/total")
    public ResponseEntity<Long> contarEventosPorAtivo(@PathVariable Long ativoId) {
        Long total = historicoAtivoService.contarEventosPorAtivo(ativoId);
        return ResponseEntity.ok(total);
    }

    /**
     * Retorna a contagem de eventos agrupados por usuário responsável.
     *
     * @return lista de objetos com o ID do usuário e a quantidade de eventos.
     */
    @GetMapping("/por-usuario")
    public ResponseEntity<List<Object[]>> contarEventosPorUsuario() {
        List<Object[]> resultado = historicoAtivoService.contarEventosPorUsuario();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Retorna os eventos mais recentes registrados no sistema.
     *
     * @param limite quantidade máxima de eventos a retornar.
     * @return lista de eventos recentes.
     */
    @GetMapping("/recentes")
    public ResponseEntity<List<HistoricoAtivoListDTO>> buscarEventosRecentes(@RequestParam(defaultValue = "10") int limite) {
        List<HistoricoAtivoListDTO> lista = historicoAtivoService.buscarEventosRecentes(limite);
        return ResponseEntity.ok(lista);
    }
}
