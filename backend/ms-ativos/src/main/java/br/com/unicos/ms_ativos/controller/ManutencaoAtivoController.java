package br.com.unicos.ms_ativos.controller;

import br.com.unicos.ms_ativos.dto.ManutencaoAtivoListDTO;
import br.com.unicos.ms_ativos.dto.ManutencaoAtivoRequest;
import br.com.unicos.ms_ativos.dto.ManutencaoAtivoResponse;
import br.com.unicos.ms_ativos.enums.StatusManutencao;
import br.com.unicos.ms_ativos.enums.TipoManutencao;
import br.com.unicos.ms_ativos.service.ManutencaoAtivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento das manutenções de ativos patrimoniais.
 * <p>
 * Permite o registro, atualização, exclusão e consultas de manutenções preventivas e corretivas,
 * além de relatórios financeiros e operacionais.
 */
@RestController
@RequestMapping("/v1/manutencoes-ativos")
@RequiredArgsConstructor
public class ManutencaoAtivoController {

    private final ManutencaoAtivoService manutencaoAtivoService;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Registra uma nova manutenção de ativo.
     *
     * @param request DTO contendo os dados da manutenção.
     * @return resposta com os dados da manutenção criada.
     */
    @PostMapping
    public ResponseEntity<ManutencaoAtivoResponse> salvar(@Valid @RequestBody ManutencaoAtivoRequest request) {
        ManutencaoAtivoResponse response = manutencaoAtivoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza as informações de uma manutenção existente.
     *
     * @param id      identificador da manutenção.
     * @param request DTO com os novos dados.
     * @return resposta com os dados atualizados.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ManutencaoAtivoResponse> atualizar(@PathVariable Long id,
                                                             @Valid @RequestBody ManutencaoAtivoRequest request) {
        ManutencaoAtivoResponse response = manutencaoAtivoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui uma manutenção com base no seu ID.
     *
     * @param id identificador da manutenção.
     * @return status HTTP 204 em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        manutencaoAtivoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca uma manutenção específica pelo seu ID.
     *
     * @param id identificador da manutenção.
     * @return resposta com os dados detalhados, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ManutencaoAtivoResponse> buscarPorId(@PathVariable Long id) {
        Optional<ManutencaoAtivoResponse> response = manutencaoAtivoService.buscarPorId(id);
        return response.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as manutenções registradas.
     *
     * @return lista de manutenções em formato resumido.
     */
    @GetMapping
    public ResponseEntity<List<ManutencaoAtivoListDTO>> listarTodos() {
        List<ManutencaoAtivoListDTO> lista = manutencaoAtivoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna todas as manutenções vinculadas a um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return lista de manutenções do ativo.
     */
    @GetMapping("/ativo/{ativoId}")
    public ResponseEntity<List<ManutencaoAtivoListDTO>> buscarPorAtivo(@PathVariable Long ativoId) {
        List<ManutencaoAtivoListDTO> lista = manutencaoAtivoService.buscarPorAtivo(ativoId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna manutenções realizadas por um fornecedor específico.
     *
     * @param fornecedorId identificador do fornecedor.
     * @return lista de manutenções associadas ao fornecedor.
     */
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<ManutencaoAtivoListDTO>> buscarPorFornecedor(@PathVariable Long fornecedorId) {
        List<ManutencaoAtivoListDTO> lista = manutencaoAtivoService.buscarPorFornecedor(fornecedorId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna manutenções filtradas por tipo (preventiva, corretiva, etc.).
     *
     * @param tipo tipo da manutenção.
     * @return lista de manutenções do tipo informado.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ManutencaoAtivoListDTO>> buscarPorTipo(@PathVariable TipoManutencao tipo) {
        List<ManutencaoAtivoListDTO> lista = manutencaoAtivoService.buscarPorTipo(tipo);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna manutenções filtradas por status (aberta, concluída, etc.).
     *
     * @param status status da manutenção.
     * @return lista de manutenções com o status informado.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ManutencaoAtivoListDTO>> buscarPorStatus(@PathVariable StatusManutencao status) {
        List<ManutencaoAtivoListDTO> lista = manutencaoAtivoService.buscarPorStatus(status);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna manutenções realizadas dentro de um período específico.
     *
     * @param inicio data inicial.
     * @param fim    data final.
     * @return lista de manutenções no intervalo informado.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<ManutencaoAtivoListDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<ManutencaoAtivoListDTO> lista = manutencaoAtivoService.buscarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna manutenções agendadas para uma data específica.
     *
     * @param data data da manutenção.
     * @return lista de manutenções agendadas.
     */
    @GetMapping("/agendadas")
    public ResponseEntity<List<ManutencaoAtivoListDTO>> buscarAgendadasPara(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<ManutencaoAtivoListDTO> lista = manutencaoAtivoService.buscarAgendadasPara(data);
        return ResponseEntity.ok(lista);
    }

    // ===========================================================
    // 💰 RELATÓRIOS E ANÁLISES
    // ===========================================================

    /**
     * Calcula o custo total de manutenções de um ativo específico.
     *
     * @param ativoId identificador do ativo.
     * @return valor total gasto em manutenções do ativo.
     */
    @GetMapping("/ativo/{ativoId}/custo-total")
    public ResponseEntity<BigDecimal> calcularCustoTotalPorAtivo(@PathVariable Long ativoId) {
        BigDecimal total = manutencaoAtivoService.calcularCustoTotalPorAtivo(ativoId);
        return ResponseEntity.ok(total);
    }

    /**
     * Calcula o custo total de manutenções realizadas em um período.
     *
     * @param inicio data inicial.
     * @param fim    data final.
     * @return valor total gasto no período.
     */
    @GetMapping("/custo-periodo")
    public ResponseEntity<BigDecimal> calcularCustoTotalPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        BigDecimal total = manutencaoAtivoService.calcularCustoTotalPorPeriodo(inicio, fim);
        return ResponseEntity.ok(total);
    }

    /**
     * Retorna o número total de manutenções registradas.
     *
     * @return total de manutenções cadastradas.
     */
    @GetMapping("/total")
    public ResponseEntity<Long> contarTotal() {
        Long total = manutencaoAtivoService.contarTotal();
        return ResponseEntity.ok(total);
    }

    /**
     * Retorna a contagem de manutenções agrupadas por tipo.
     *
     * @return lista de pares (tipo, quantidade).
     */
    @GetMapping("/por-tipo")
    public ResponseEntity<List<Object[]>> contarPorTipo() {
        List<Object[]> resultado = manutencaoAtivoService.contarPorTipo();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Retorna a contagem de manutenções agrupadas por status.
     *
     * @return lista de pares (status, quantidade).
     */
    @GetMapping("/por-status")
    public ResponseEntity<List<Object[]>> contarPorStatus() {
        List<Object[]> resultado = manutencaoAtivoService.contarPorStatus();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Retorna os ativos que possuem manutenções frequentes (mais de uma ocorrência).
     *
     * @return lista de ativos com manutenção recorrente.
     */
    @GetMapping("/ativos-frequentes")
    public ResponseEntity<List<Object[]>> buscarAtivosComManutencoesFrequentes() {
        List<Object[]> lista = manutencaoAtivoService.buscarAtivosComManutencoesFrequentes();
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna as manutenções mais recentes registradas no sistema.
     *
     * @param limite número máximo de registros a retornar (padrão: 10).
     * @return lista de manutenções recentes.
     */
    @GetMapping("/recentes")
    public ResponseEntity<List<ManutencaoAtivoListDTO>> buscarRecentes(
            @RequestParam(defaultValue = "10") int limite) {
        List<ManutencaoAtivoListDTO> lista = manutencaoAtivoService.buscarRecentes(limite);
        return ResponseEntity.ok(lista);
    }
}
