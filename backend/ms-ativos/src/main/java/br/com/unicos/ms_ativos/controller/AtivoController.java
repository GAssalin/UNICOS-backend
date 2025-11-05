package br.com.unicos.ms_ativos.controller;

import br.com.unicos.ms_ativos.dto.AtivoListDTO;
import br.com.unicos.ms_ativos.dto.AtivoRequest;
import br.com.unicos.ms_ativos.dto.AtivoResponse;
import br.com.unicos.ms_ativos.enums.StatusAtivo;
import br.com.unicos.ms_ativos.enums.TipoAtivo;
import br.com.unicos.ms_ativos.service.AtivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos ativos patrimoniais.
 * <p>
 * Fornece endpoints para operações de CRUD, filtros por tipo, status, empresa,
 * filial e responsável, além de consultas especializadas para relatórios e dashboards.
 */
@RestController
@RequestMapping("/v1/ativos")
@RequiredArgsConstructor
public class AtivoController {

    private final AtivoService ativoService;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Cria e registra um novo ativo no sistema.
     *
     * @param request DTO contendo os dados do ativo.
     * @return resposta com os dados do ativo criado.
     */
    @PostMapping
    public ResponseEntity<AtivoResponse> salvar(@Valid @RequestBody AtivoRequest request) {
        AtivoResponse response = ativoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza as informações de um ativo existente.
     *
     * @param id      identificador do ativo.
     * @param request DTO contendo os novos dados.
     * @return resposta com os dados atualizados.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AtivoResponse> atualizar(@PathVariable Long id,
                                                   @Valid @RequestBody AtivoRequest request) {
        AtivoResponse response = ativoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui um ativo com base no seu identificador.
     *
     * @param id identificador do ativo.
     * @return status HTTP 204 em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        ativoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca um ativo específico pelo seu ID.
     *
     * @param id identificador do ativo.
     * @return resposta com os dados detalhados, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AtivoResponse> buscarPorId(@PathVariable Long id) {
        return ativoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retorna todos os ativos cadastrados.
     *
     * @return lista de ativos.
     */
    @GetMapping
    public ResponseEntity<List<AtivoListDTO>> listarTodos() {
        List<AtivoListDTO> lista = ativoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna todos os ativos filtrados por tipo.
     *
     * @param tipo tipo do ativo (ex: MOVEL, IMOVEL, VEICULO).
     * @return lista de ativos do tipo informado.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<AtivoListDTO>> buscarPorTipo(@PathVariable TipoAtivo tipo) {
        List<AtivoListDTO> lista = ativoService.buscarPorTipo(tipo);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna todos os ativos filtrados por status.
     *
     * @param status status do ativo (ex: ATIVO, INATIVO, EM_MANUTENCAO).
     * @return lista de ativos com o status informado.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AtivoListDTO>> buscarPorStatus(@PathVariable StatusAtivo status) {
        List<AtivoListDTO> lista = ativoService.buscarPorStatus(status);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna todos os ativos pertencentes a uma empresa específica.
     *
     * @param empresaId identificador da empresa.
     * @return lista de ativos da empresa.
     */
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<AtivoListDTO>> buscarPorEmpresa(@PathVariable Long empresaId) {
        List<AtivoListDTO> lista = ativoService.buscarPorEmpresa(empresaId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna todos os ativos pertencentes a uma filial específica.
     *
     * @param filialId identificador da filial.
     * @return lista de ativos da filial.
     */
    @GetMapping("/filial/{filialId}")
    public ResponseEntity<List<AtivoListDTO>> buscarPorFilial(@PathVariable Long filialId) {
        List<AtivoListDTO> lista = ativoService.buscarPorFilial(filialId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna todos os ativos sob responsabilidade de um colaborador específico.
     *
     * @param responsavelId identificador do colaborador responsável.
     * @return lista de ativos do responsável.
     */
    @GetMapping("/responsavel/{responsavelId}")
    public ResponseEntity<List<AtivoListDTO>> buscarPorResponsavel(@PathVariable Long responsavelId) {
        List<AtivoListDTO> lista = ativoService.buscarPorResponsavel(responsavelId);
        return ResponseEntity.ok(lista);
    }
}
