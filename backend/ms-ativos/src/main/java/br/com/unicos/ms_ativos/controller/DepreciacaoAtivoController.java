package br.com.unicos.ms_ativos.controller;

import br.com.unicos.ms_ativos.dto.DepreciacaoAtivoListDTO;
import br.com.unicos.ms_ativos.dto.DepreciacaoAtivoRequest;
import br.com.unicos.ms_ativos.dto.DepreciacaoAtivoResponse;
import br.com.unicos.ms_ativos.enums.TipoDepreciacao;
import br.com.unicos.ms_ativos.service.DepreciacaoAtivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento das depreciações de ativos patrimoniais.
 * <p>
 * Fornece endpoints para criação, atualização, exclusão e consultas relacionadas às
 * depreciações mensais, valores totais, médias e saldos contábeis.
 */
@RestController
@RequestMapping("/v1/depreciacoes")
@RequiredArgsConstructor
public class DepreciacaoAtivoController {

    private final DepreciacaoAtivoService depreciacaoAtivoService;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Registra uma nova depreciação para um ativo.
     *
     * @param request DTO contendo os dados da depreciação.
     * @return resposta com os dados da depreciação criada.
     */
    @PostMapping
    public ResponseEntity<DepreciacaoAtivoResponse> salvar(@Valid @RequestBody DepreciacaoAtivoRequest request) {
        DepreciacaoAtivoResponse response = depreciacaoAtivoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma depreciação existente.
     *
     * @param id      identificador da depreciação.
     * @param request DTO contendo os novos dados.
     * @return resposta com os dados atualizados.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepreciacaoAtivoResponse> atualizar(@PathVariable Long id,
                                                              @Valid @RequestBody DepreciacaoAtivoRequest request) {
        DepreciacaoAtivoResponse response = depreciacaoAtivoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui uma depreciação com base em seu ID.
     *
     * @param id identificador da depreciação.
     * @return status HTTP 204 em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        depreciacaoAtivoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca uma depreciação específica pelo seu ID.
     *
     * @param id identificador da depreciação.
     * @return resposta com os dados detalhados, se encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepreciacaoAtivoResponse> buscarPorId(@PathVariable Long id) {
        return depreciacaoAtivoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as depreciações cadastradas no sistema.
     *
     * @return lista de depreciações resumidas.
     */
    @GetMapping
    public ResponseEntity<List<DepreciacaoAtivoListDTO>> listarTodos() {
        List<DepreciacaoAtivoListDTO> lista = depreciacaoAtivoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna todas as depreciações de um ativo específico.
     *
     * @param ativoId identificador do ativo.
     * @return lista de depreciações do ativo.
     */
    @GetMapping("/ativo/{ativoId}")
    public ResponseEntity<List<DepreciacaoAtivoListDTO>> buscarPorAtivo(@PathVariable Long ativoId) {
        List<DepreciacaoAtivoListDTO> lista = depreciacaoAtivoService.buscarPorAtivo(ativoId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna todas as depreciações de um determinado tipo.
     *
     * @param tipo tipo da depreciação (ex: LINEAR, ACELERADA).
     * @return lista de depreciações do tipo informado.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<DepreciacaoAtivoListDTO>> buscarPorTipo(@PathVariable TipoDepreciacao tipo) {
        List<DepreciacaoAtivoListDTO> lista = depreciacaoAtivoService.buscarPorTipo(tipo);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna depreciações registradas dentro de um intervalo de competências.
     *
     * @param inicio data inicial do período.
     * @param fim    data final do período.
     * @return lista de depreciações dentro do intervalo.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<DepreciacaoAtivoListDTO>> buscarPorPeriodo(@RequestParam LocalDate inicio,
                                                                          @RequestParam LocalDate fim) {
        List<DepreciacaoAtivoListDTO> lista = depreciacaoAtivoService.buscarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(lista);
    }

    /**
     * Busca a depreciação de um ativo em uma competência específica.
     *
     * @param ativoId          identificador do ativo.
     * @param dataCompetencia  data da competência.
     * @return depreciação da competência, se existir.
     */
    @GetMapping("/competencia")
    public ResponseEntity<DepreciacaoAtivoResponse> buscarPorCompetencia(@RequestParam Long ativoId,
                                                                         @RequestParam LocalDate dataCompetencia) {
        return depreciacaoAtivoService.buscarPorCompetencia(ativoId, dataCompetencia)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===========================================================
    // 💰 RELATÓRIOS E CÁLCULOS
    // ===========================================================

    /**
     * Calcula o valor total depreciado de um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return valor total depreciado.
     */
    @GetMapping("/ativo/{ativoId}/total")
    public ResponseEntity<BigDecimal> calcularValorTotalDepreciado(@PathVariable Long ativoId) {
        BigDecimal total = depreciacaoAtivoService.calcularValorTotalDepreciado(ativoId);
        return ResponseEntity.ok(total);
    }

    /**
     * Retorna o saldo contábil atual do ativo.
     *
     * @param ativoId identificador do ativo.
     * @return saldo contábil mais recente, se existente.
     */
    @GetMapping("/ativo/{ativoId}/saldo")
    public ResponseEntity<BigDecimal> buscarSaldoContabilAtual(@PathVariable Long ativoId) {
        Optional<BigDecimal> saldo = depreciacaoAtivoService.buscarSaldoContabilAtual(ativoId);
        return saldo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Calcula a média mensal de depreciação de um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return média mensal da depreciação.
     */
    @GetMapping("/ativo/{ativoId}/media")
    public ResponseEntity<BigDecimal> calcularMediaDepreciacaoMensal(@PathVariable Long ativoId) {
        BigDecimal media = depreciacaoAtivoService.calcularMediaDepreciacaoMensal(ativoId);
        return ResponseEntity.ok(media);
    }

    /**
     * Retorna todas as depreciações registradas no mês atual.
     *
     * @return lista de depreciações do mês vigente.
     */
    @GetMapping("/mes-atual")
    public ResponseEntity<List<DepreciacaoAtivoListDTO>> buscarDepreciacoesDoMesAtual() {
        List<DepreciacaoAtivoListDTO> lista = depreciacaoAtivoService.buscarDepreciacoesDoMesAtual();
        return ResponseEntity.ok(lista);
    }
}
