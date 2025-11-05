package br.com.unicos.ms_ativos.controller;

import br.com.unicos.ms_ativos.dto.TransferenciaAtivoListDTO;
import br.com.unicos.ms_ativos.dto.TransferenciaAtivoRequest;
import br.com.unicos.ms_ativos.dto.TransferenciaAtivoResponse;
import br.com.unicos.ms_ativos.enums.TipoTransferencia;
import br.com.unicos.ms_ativos.service.TransferenciaAtivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento das transferências de ativos.
 * <p>
 * Permite registrar, atualizar, excluir e consultar movimentações de ativos entre locais ou filiais,
 * assegurando rastreabilidade e integridade das operações.
 */
@RestController
@RequestMapping("/v1/transferencias-ativos")
@RequiredArgsConstructor
public class TransferenciaAtivoController {

    private final TransferenciaAtivoService transferenciaAtivoService;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Registra uma nova transferência de ativo.
     *
     * @param request DTO contendo os dados da transferência.
     * @return resposta com os dados da transferência criada.
     */
    @PostMapping
    public ResponseEntity<TransferenciaAtivoResponse> salvar(@Valid @RequestBody TransferenciaAtivoRequest request) {
        TransferenciaAtivoResponse response = transferenciaAtivoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza as informações de uma transferência existente.
     *
     * @param id      identificador da transferência.
     * @param request DTO com os novos dados.
     * @return resposta com os dados atualizados.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransferenciaAtivoResponse> atualizar(@PathVariable Long id,
                                                                @Valid @RequestBody TransferenciaAtivoRequest request) {
        TransferenciaAtivoResponse response = transferenciaAtivoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui uma transferência com base no seu ID.
     *
     * @param id identificador da transferência.
     * @return status HTTP 204 em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        transferenciaAtivoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca uma transferência específica pelo seu ID.
     *
     * @param id identificador da transferência.
     * @return resposta com os dados detalhados, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaAtivoResponse> buscarPorId(@PathVariable Long id) {
        Optional<TransferenciaAtivoResponse> response = transferenciaAtivoService.buscarPorId(id);
        return response.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as transferências registradas.
     *
     * @return lista de transferências.
     */
    @GetMapping
    public ResponseEntity<List<TransferenciaAtivoListDTO>> listarTodos() {
        List<TransferenciaAtivoListDTO> lista = transferenciaAtivoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna todas as transferências vinculadas a um ativo específico.
     *
     * @param ativoId identificador do ativo.
     * @return lista de transferências do ativo.
     */
    @GetMapping("/ativo/{ativoId}")
    public ResponseEntity<List<TransferenciaAtivoListDTO>> buscarPorAtivo(@PathVariable Long ativoId) {
        List<TransferenciaAtivoListDTO> lista = transferenciaAtivoService.buscarPorAtivo(ativoId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna as transferências originadas de uma unidade específica.
     *
     * @param origemId identificador da unidade de origem.
     * @return lista de transferências da origem informada.
     */
    @GetMapping("/origem/{origemId}")
    public ResponseEntity<List<TransferenciaAtivoListDTO>> buscarPorOrigem(@PathVariable Long origemId) {
        List<TransferenciaAtivoListDTO> lista = transferenciaAtivoService.buscarPorOrigem(origemId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna as transferências destinadas a uma unidade específica.
     *
     * @param destinoId identificador da unidade de destino.
     * @return lista de transferências do destino informado.
     */
    @GetMapping("/destino/{destinoId}")
    public ResponseEntity<List<TransferenciaAtivoListDTO>> buscarPorDestino(@PathVariable Long destinoId) {
        List<TransferenciaAtivoListDTO> lista = transferenciaAtivoService.buscarPorDestino(destinoId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna transferências realizadas dentro de um período específico.
     *
     * @param inicio data inicial.
     * @param fim    data final.
     * @return lista de transferências no intervalo.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<TransferenciaAtivoListDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<TransferenciaAtivoListDTO> lista = transferenciaAtivoService.buscarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna transferências realizadas por um responsável específico.
     *
     * @param responsavelId identificador do responsável.
     * @return lista de transferências do responsável.
     */
    @GetMapping("/responsavel/{responsavelId}")
    public ResponseEntity<List<TransferenciaAtivoListDTO>> buscarPorResponsavel(@PathVariable Long responsavelId) {
        List<TransferenciaAtivoListDTO> lista = transferenciaAtivoService.buscarPorResponsavel(responsavelId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna transferências de acordo com o tipo (interna, entre filiais, etc.).
     *
     * @param tipo tipo da transferência.
     * @return lista de transferências do tipo informado.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<TransferenciaAtivoListDTO>> buscarPorTipo(@PathVariable TipoTransferencia tipo) {
        List<TransferenciaAtivoListDTO> lista = transferenciaAtivoService.buscarPorTipo(tipo);
        return ResponseEntity.ok(lista);
    }

    // ===========================================================
    // 📊 RELATÓRIOS E RASTREABILIDADE
    // ===========================================================

    /**
     * Retorna o histórico completo de movimentações de um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return lista ordenada das transferências do ativo.
     */
    @GetMapping("/ativo/{ativoId}/historico")
    public ResponseEntity<List<TransferenciaAtivoListDTO>> buscarHistoricoDeMovimentacoes(@PathVariable Long ativoId) {
        List<TransferenciaAtivoListDTO> lista = transferenciaAtivoService.buscarHistoricoDeMovimentacoes(ativoId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna o total de transferências registradas.
     *
     * @return número total de transferências.
     */
    @GetMapping("/total")
    public ResponseEntity<Long> contarTotal() {
        Long total = transferenciaAtivoService.contarTotal();
        return ResponseEntity.ok(total);
    }

    /**
     * Retorna a contagem de transferências agrupadas por tipo.
     *
     * @return lista de pares (tipo, quantidade).
     */
    @GetMapping("/por-tipo")
    public ResponseEntity<List<Object[]>> contarPorTipo() {
        List<Object[]> resultado = transferenciaAtivoService.contarPorTipo();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Retorna a contagem de transferências agrupadas por origem.
     *
     * @return lista de pares (origem, quantidade).
     */
    @GetMapping("/por-origem")
    public ResponseEntity<List<Object[]>> contarPorOrigem() {
        List<Object[]> resultado = transferenciaAtivoService.contarPorOrigem();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Retorna a contagem de transferências agrupadas por destino.
     *
     * @return lista de pares (destino, quantidade).
     */
    @GetMapping("/por-destino")
    public ResponseEntity<List<Object[]>> contarPorDestino() {
        List<Object[]> resultado = transferenciaAtivoService.contarPorDestino();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Retorna as transferências mais recentes registradas no sistema.
     *
     * @param limite número máximo de registros a retornar (padrão: 10).
     * @return lista de transferências recentes.
     */
    @GetMapping("/recentes")
    public ResponseEntity<List<TransferenciaAtivoListDTO>> buscarRecentes(
            @RequestParam(defaultValue = "10") int limite) {
        List<TransferenciaAtivoListDTO> lista = transferenciaAtivoService.buscarRecentes(limite);
        return ResponseEntity.ok(lista);
    }
}
