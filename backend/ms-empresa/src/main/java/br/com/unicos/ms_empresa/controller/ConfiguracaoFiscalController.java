package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.ConfiguracaoFiscalRequest;
import br.com.unicos.ms_empresa.dto.ConfiguracaoFiscalResponse;
import br.com.unicos.ms_empresa.service.ConfiguracaoFiscalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento das configurações fiscais das empresas.
 * <p>
 * Fornece endpoints REST para criação, atualização, listagem, busca e exclusão
 * de configurações fiscais.
 */
@RestController
@RequestMapping("/v1/configuracoes-fiscais")
public class ConfiguracaoFiscalController {

    private final ConfiguracaoFiscalService configuracaoFiscalService;

    public ConfiguracaoFiscalController(ConfiguracaoFiscalService configuracaoFiscalService) {
        this.configuracaoFiscalService = configuracaoFiscalService;
    }

    /**
     * Cria uma nova configuração fiscal para a empresa.
     *
     * @param request Dados da configuração fiscal.
     * @return Configuração fiscal criada.
     * @status 201 Created
     */
    @PostMapping
    public ResponseEntity<ConfiguracaoFiscalResponse> criar(@Valid @RequestBody ConfiguracaoFiscalRequest request) {
        ConfiguracaoFiscalResponse response = configuracaoFiscalService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma configuração fiscal existente.
     *
     * @param id      ID da configuração fiscal a ser atualizada.
     * @param request Novos dados da configuração.
     * @return Configuração fiscal atualizada.
     * @status 200 OK / 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracaoFiscalResponse> atualizar(@PathVariable Long id,
                                                                @Valid @RequestBody ConfiguracaoFiscalRequest request) {
        ConfiguracaoFiscalResponse response = configuracaoFiscalService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma configuração fiscal pelo ID.
     *
     * @param id ID da configuração fiscal.
     * @return Configuração encontrada, se existir.
     * @status 200 OK / 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracaoFiscalResponse> buscarPorId(@PathVariable Long id) {
        return configuracaoFiscalService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as configurações fiscais cadastradas.
     *
     * @return Lista de configurações fiscais.
     * @status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<ConfiguracaoFiscalResponse>> listarTodas() {
        List<ConfiguracaoFiscalResponse> lista = configuracaoFiscalService.listarTodas();
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista todas as configurações fiscais ativas.
     *
     * @return Lista de configurações fiscais ativas.
     * @status 200 OK
     */
    @GetMapping("/ativas")
    public ResponseEntity<List<ConfiguracaoFiscalResponse>> listarAtivas() {
        List<ConfiguracaoFiscalResponse> lista = configuracaoFiscalService.listarAtivas();
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista todas as configurações fiscais inativas.
     *
     * @return Lista de configurações fiscais inativas.
     * @status 200 OK
     */
    @GetMapping("/inativas")
    public ResponseEntity<List<ConfiguracaoFiscalResponse>> listarInativas() {
        List<ConfiguracaoFiscalResponse> lista = configuracaoFiscalService.listarInativas();
        return ResponseEntity.ok(lista);
    }

    /**
     * Busca a configuração fiscal vinculada a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Configuração fiscal associada à empresa, se existir.
     * @status 200 OK / 404 Not Found
     */
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<ConfiguracaoFiscalResponse> buscarPorEmpresa(@PathVariable Long empresaId) {
        return configuracaoFiscalService.buscarPorEmpresa(empresaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove uma configuração fiscal do sistema.
     *
     * @param id ID da configuração fiscal.
     * @return 204 sem conteúdo.
     * @status 204 No Content / 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        configuracaoFiscalService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
