package br.com.unicos.ms_ativos.service;

import br.com.unicos.ms_ativos.dto.FornecedorManutencaoListDTO;
import br.com.unicos.ms_ativos.dto.FornecedorManutencaoRequest;
import br.com.unicos.ms_ativos.dto.FornecedorManutencaoResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Interface responsável pelas regras de negócio da entidade {@code FornecedorManutencao}.
 * <p>
 * Define os métodos de gerenciamento dos fornecedores responsáveis por serviços
 * de manutenção preventiva ou corretiva dos ativos patrimoniais.
 */
public interface FornecedorManutencaoService {

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Cadastra um novo fornecedor de manutenção.
     *
     * @param request DTO contendo os dados do fornecedor.
     * @return DTO com os dados do fornecedor cadastrado.
     */
    @Transactional
    FornecedorManutencaoResponse salvar(FornecedorManutencaoRequest request);

    /**
     * Atualiza os dados de um fornecedor existente.
     *
     * @param id      identificador do fornecedor.
     * @param request DTO contendo os novos dados.
     * @return DTO atualizado do fornecedor.
     */
    @Transactional
    FornecedorManutencaoResponse atualizar(Long id, FornecedorManutencaoRequest request);

    /**
     * Remove um fornecedor do sistema.
     *
     * @param id identificador do fornecedor.
     */
    @Transactional
    void excluir(Long id);

    /**
     * Busca um fornecedor específico pelo seu ID.
     *
     * @param id identificador do fornecedor.
     * @return DTO detalhado, se encontrado.
     */
    Optional<FornecedorManutencaoResponse> buscarPorId(Long id);

    /**
     * Lista todos os fornecedores cadastrados.
     *
     * @return lista resumida de fornecedores.
     */
    List<FornecedorManutencaoListDTO> listarTodos();

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Busca fornecedores que contenham o nome informado (razão social ou fantasia).
     *
     * @param nome parte ou nome completo do fornecedor.
     * @return lista de fornecedores encontrados.
     */
    List<FornecedorManutencaoListDTO> buscarPorNome(String nome);

    /**
     * Busca um fornecedor pelo seu CNPJ.
     *
     * @param cnpj número do CNPJ.
     * @return fornecedor correspondente, se encontrado.
     */
    Optional<FornecedorManutencaoResponse> buscarPorCnpj(String cnpj);

    /**
     * Verifica se já existe um fornecedor cadastrado com o CNPJ informado.
     *
     * @param cnpj número do CNPJ.
     * @return true se existir, false caso contrário.
     */
    boolean existePorCnpj(String cnpj);

    /**
     * Busca fornecedores por e-mail de contato.
     *
     * @param email e-mail de contato.
     * @return lista de fornecedores com o e-mail informado.
     */
    List<FornecedorManutencaoListDTO> buscarPorEmail(String email);

    /**
     * Busca fornecedores por telefone de contato.
     *
     * @param telefone número do telefone.
     * @return lista de fornecedores correspondentes.
     */
    List<FornecedorManutencaoListDTO> buscarPorTelefone(String telefone);

    // ===========================================================
    // 📊 RELATÓRIOS E INDICADORES
    // ===========================================================

    /**
     * Retorna fornecedores que possuem manutenções registradas.
     *
     * @return lista de fornecedores com histórico de manutenção.
     */
    List<FornecedorManutencaoListDTO> buscarFornecedoresComManutencoes();

    /**
     * Retorna fornecedores que ainda não possuem manutenções associadas.
     *
     * @return lista de fornecedores sem histórico de manutenção.
     */
    List<FornecedorManutencaoListDTO> buscarFornecedoresSemManutencoes();

    /**
     * Retorna fornecedores mais ativos, com base no número de manutenções realizadas.
     *
     * @return lista de fornecedores ordenados por volume de serviços.
     */
    List<FornecedorManutencaoListDTO> buscarFornecedoresMaisAtivos();

    /**
     * Retorna fornecedores com mais de um determinado número de manutenções.
     *
     * @param quantidade número mínimo de manutenções.
     * @return lista de fornecedores que atendem ao critério.
     */
    List<FornecedorManutencaoListDTO> buscarComMaisDe(int quantidade);

    /**
     * Retorna fornecedores com e-mails inválidos ou ausentes.
     *
     * @return lista de fornecedores com e-mail incorreto ou não informado.
     */
    List<FornecedorManutencaoListDTO> buscarComEmailInvalido();

    /**
     * Retorna fornecedores sem telefone cadastrado.
     *
     * @return lista de fornecedores que não possuem telefone informado.
     */
    List<FornecedorManutencaoListDTO> buscarSemTelefone();
}
