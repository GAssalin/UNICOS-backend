package br.com.unicos.ms_ativos.controller;

import br.com.unicos.ms_ativos.dto.FornecedorManutencaoListDTO;
import br.com.unicos.ms_ativos.dto.FornecedorManutencaoRequest;
import br.com.unicos.ms_ativos.dto.FornecedorManutencaoResponse;
import br.com.unicos.ms_ativos.service.FornecedorManutencaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento dos fornecedores de manutenção.
 * <p>
 * Permite o cadastro, atualização, exclusão e consultas filtradas por nome, CNPJ, e-mail
 * e telefone, além de relatórios sobre fornecedores mais ativos e com manutenções associadas.
 */
@RestController
@RequestMapping("/v1/fornecedores-manutencao")
@RequiredArgsConstructor
public class FornecedorManutencaoController {

    private final FornecedorManutencaoService fornecedorService;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Registra um novo fornecedor de manutenção.
     *
     * @param request DTO contendo os dados do fornecedor.
     * @return resposta com os dados do fornecedor criado.
     */
    @PostMapping
    public ResponseEntity<FornecedorManutencaoResponse> salvar(@Valid @RequestBody FornecedorManutencaoRequest request) {
        FornecedorManutencaoResponse response = fornecedorService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza as informações de um fornecedor existente.
     *
     * @param id      identificador do fornecedor.
     * @param request DTO contendo os novos dados.
     * @return resposta com os dados atualizados.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorManutencaoResponse> atualizar(@PathVariable Long id,
                                                                  @Valid @RequestBody FornecedorManutencaoRequest request) {
        FornecedorManutencaoResponse response = fornecedorService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui um fornecedor de manutenção com base em seu ID.
     *
     * @param id identificador do fornecedor.
     * @return status HTTP 204 em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        fornecedorService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca um fornecedor de manutenção pelo seu ID.
     *
     * @param id identificador do fornecedor.
     * @return resposta com os dados detalhados, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorManutencaoResponse> buscarPorId(@PathVariable Long id) {
        return fornecedorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os fornecedores de manutenção cadastrados.
     *
     * @return lista de fornecedores.
     */
    @GetMapping
    public ResponseEntity<List<FornecedorManutencaoListDTO>> listarTodos() {
        List<FornecedorManutencaoListDTO> lista = fornecedorService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Busca fornecedores pelo nome (contendo o termo informado).
     *
     * @param nome termo parcial ou completo do nome.
     * @return lista de fornecedores correspondentes.
     */
    @GetMapping("/nome")
    public ResponseEntity<List<FornecedorManutencaoListDTO>> buscarPorNome(@RequestParam String nome) {
        List<FornecedorManutencaoListDTO> lista = fornecedorService.buscarPorNome(nome);
        return ResponseEntity.ok(lista);
    }

    /**
     * Busca um fornecedor pelo seu CNPJ.
     *
     * @param cnpj CNPJ do fornecedor.
     * @return fornecedor correspondente, se encontrado.
     */
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<FornecedorManutencaoResponse> buscarPorCnpj(@PathVariable String cnpj) {
        Optional<FornecedorManutencaoResponse> response = fornecedorService.buscarPorCnpj(cnpj);
        return response.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Verifica se existe um fornecedor cadastrado com o CNPJ informado.
     *
     * @param cnpj CNPJ a ser verificado.
     * @return true se existir, false caso contrário.
     */
    @GetMapping("/existe/{cnpj}")
    public ResponseEntity<Boolean> existePorCnpj(@PathVariable String cnpj) {
        boolean existe = fornecedorService.existePorCnpj(cnpj);
        return ResponseEntity.ok(existe);
    }

    /**
     * Busca fornecedores cujo e-mail contenha o termo informado.
     *
     * @param email termo parcial do e-mail.
     * @return lista de fornecedores com e-mails correspondentes.
     */
    @GetMapping("/email")
    public ResponseEntity<List<FornecedorManutencaoListDTO>> buscarPorEmail(@RequestParam String email) {
        List<FornecedorManutencaoListDTO> lista = fornecedorService.buscarPorEmail(email);
        return ResponseEntity.ok(lista);
    }

    /**
     * Busca fornecedores cujo telefone contenha o termo informado.
     *
     * @param telefone termo parcial do telefone.
     * @return lista de fornecedores com telefones correspondentes.
     */
    @GetMapping("/telefone")
    public ResponseEntity<List<FornecedorManutencaoListDTO>> buscarPorTelefone(@RequestParam String telefone) {
        List<FornecedorManutencaoListDTO> lista = fornecedorService.buscarPorTelefone(telefone);
        return ResponseEntity.ok(lista);
    }

    // ===========================================================
    // 📊 RELATÓRIOS E INDICADORES
    // ===========================================================

    /**
     * Retorna fornecedores que possuem manutenções registradas.
     *
     * @return lista de fornecedores com manutenções associadas.
     */
    @GetMapping("/com-manutencoes")
    public ResponseEntity<List<FornecedorManutencaoListDTO>> buscarFornecedoresComManutencoes() {
        List<FornecedorManutencaoListDTO> lista = fornecedorService.buscarFornecedoresComManutencoes();
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna fornecedores que não possuem manutenções associadas.
     *
     * @return lista de fornecedores sem manutenções.
     */
    @GetMapping("/sem-manutencoes")
    public ResponseEntity<List<FornecedorManutencaoListDTO>> buscarFornecedoresSemManutencoes() {
        List<FornecedorManutencaoListDTO> lista = fornecedorService.buscarFornecedoresSemManutencoes();
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna fornecedores mais ativos (com maior número de manutenções realizadas).
     *
     * @return lista de fornecedores mais ativos.
     */
    @GetMapping("/mais-ativos")
    public ResponseEntity<List<FornecedorManutencaoListDTO>> buscarFornecedoresMaisAtivos() {
        List<FornecedorManutencaoListDTO> lista = fornecedorService.buscarFornecedoresMaisAtivos();
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna fornecedores que realizaram mais de uma quantidade específica de manutenções.
     *
     * @param quantidade número mínimo de manutenções.
     * @return lista de fornecedores que atendem ao critério.
     */
    @GetMapping("/com-mais-de")
    public ResponseEntity<List<FornecedorManutencaoListDTO>> buscarComMaisDe(@RequestParam int quantidade) {
        List<FornecedorManutencaoListDTO> lista = fornecedorService.buscarComMaisDe(quantidade);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna fornecedores com e-mails inválidos.
     *
     * @return lista de fornecedores com e-mails incorretos.
     */
    @GetMapping("/email-invalido")
    public ResponseEntity<List<FornecedorManutencaoListDTO>> buscarComEmailInvalido() {
        List<FornecedorManutencaoListDTO> lista = fornecedorService.buscarComEmailInvalido();
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna fornecedores sem telefone cadastrado.
     *
     * @return lista de fornecedores sem telefone.
     */
    @GetMapping("/sem-telefone")
    public ResponseEntity<List<FornecedorManutencaoListDTO>> buscarSemTelefone() {
        List<FornecedorManutencaoListDTO> lista = fornecedorService.buscarSemTelefone();
        return ResponseEntity.ok(lista);
    }
}
