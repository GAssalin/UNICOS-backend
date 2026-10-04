package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaResponse;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaListDTO;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaUpdateRequest;
import br.com.unicos.ms_empresa.enums.StatusEmpresa;
import br.com.unicos.ms_empresa.enums.TipoEmpresa;
import br.com.unicos.ms_empresa.mapper.EmpresaMapper;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do service responsável pelas regras de negócio da entidade Empresa.
 *
 * <p>
 * Cada empresa é um tenant. O usuário autenticado enxerga e altera apenas a própria empresa
 * e as filiais vinculadas a ela; empresas fora desse escopo respondem como inexistentes (404).
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;

    /**
     * Cadastra uma filial da empresa do usuário autenticado.
     *
     * <p>
     * Novas matrizes são novos tenants e não podem ser criadas a partir de outra empresa:
     * além de inacessíveis para quem as criou, permitiriam reservar o CNPJ de terceiros.
     * </p>
     *
     * @param request dados para criação da empresa
     * @return empresa cadastrada
     */
    public EmpresaResponse criar(EmpresaCreateRequest request) {
        if (request.tipoEmpresa() != TipoEmpresa.FILIAL)
            throw new AccessDeniedException("Apenas filiais podem ser cadastradas a partir de uma empresa.");

        Empresa matriz = buscarEntidadeNoEscopo(TenantContext.getEmpresaId());

        if (matriz.getTipoEmpresa() != TipoEmpresa.MATRIZ)
            throw new IllegalStateException("Apenas uma matriz pode cadastrar filiais.");

        validarCnpjDuplicado(request.cnpj());

        Empresa empresa = empresaMapper.toEntity(request);
        empresa.setMatrizId(matriz.getId());

        return empresaMapper.toResponse(empresaRepository.save(empresa));
    }

    /**
     * Busca uma empresa pelo ID.
     *
     * @param id identificador da empresa
     * @return empresa encontrada
     */
    @Transactional(readOnly = true)
    public EmpresaResponse buscarPorId(Long id) {
        return empresaMapper.toResponse(buscarEntidadeNoEscopo(id));
    }

    /**
     * Busca uma empresa pelo CNPJ.
     *
     * @param cnpj CNPJ da empresa
     * @return empresa encontrada
     */
    @Transactional(readOnly = true)
    public EmpresaResponse buscarPorCnpj(String cnpj) {
        Empresa empresa = empresaRepository.findByCnpj(cnpj)
                .filter(this::pertenceAoEscopo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empresa não encontrada para o CNPJ informado: " + cnpj));

        return empresaMapper.toResponse(empresa);
    }

    /**
     * Lista a empresa do usuário e suas filiais de forma paginada.
     *
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @Transactional(readOnly = true)
    public Page<EmpresaListDTO> listarTodas(Pageable pageable) {
        return pesquisar(null, null, null, pageable);
    }

    /**
     * Lista empresas por status.
     *
     * @param statusEmpresa status da empresa
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @Transactional(readOnly = true)
    public Page<EmpresaListDTO> listarPorStatus(StatusEmpresa statusEmpresa, Pageable pageable) {
        return pesquisar(statusEmpresa, null, null, pageable);
    }

    /**
     * Lista empresas por tipo.
     *
     * @param tipoEmpresa tipo da empresa
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @Transactional(readOnly = true)
    public Page<EmpresaListDTO> listarPorTipo(TipoEmpresa tipoEmpresa, Pageable pageable) {
        return pesquisar(null, tipoEmpresa, null, pageable);
    }

    /**
     * Lista empresas vinculadas a uma matriz.
     *
     * @param matrizId identificador da matriz
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @Transactional(readOnly = true)
    public Page<EmpresaListDTO> listarPorMatriz(Long matrizId, Pageable pageable) {
        return pesquisar(null, null, matrizId, pageable);
    }

    /**
     * Lista empresas por matriz e tipo.
     *
     * @param matrizId identificador da matriz
     * @param tipoEmpresa tipo da empresa
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @Transactional(readOnly = true)
    public Page<EmpresaListDTO> listarPorMatrizETipo(Long matrizId, TipoEmpresa tipoEmpresa, Pageable pageable) {
        return pesquisar(null, tipoEmpresa, matrizId, pageable);
    }

    /**
     * Lista empresas por matriz e status.
     *
     * @param matrizId identificador da matriz
     * @param statusEmpresa status da empresa
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @Transactional(readOnly = true)
    public Page<EmpresaListDTO> listarPorMatrizEStatus(Long matrizId, StatusEmpresa statusEmpresa, Pageable pageable) {
        return pesquisar(statusEmpresa, null, matrizId, pageable);
    }

    /**
     * Atualiza os dados de uma empresa.
     *
     * <p>O tipo (matriz/filial) não pode ser alterado, pois define o vínculo entre as empresas.</p>
     *
     * @param id identificador da empresa
     * @param request dados para atualização
     * @return empresa atualizada
     */
    public EmpresaResponse atualizar(Long id, EmpresaUpdateRequest request) {
        Empresa empresa = buscarEntidadeNoEscopo(id);

        if (request.tipoEmpresa() != empresa.getTipoEmpresa())
            throw new IllegalArgumentException("O tipo da empresa (matriz ou filial) não pode ser alterado.");

        empresaMapper.updateEntity(empresa, request);

        return empresaMapper.toResponse(empresaRepository.save(empresa));
    }

    /**
     * Remove uma filial da empresa do usuário autenticado.
     *
     * @param id identificador da empresa
     */
    public void deletar(Long id) {
        Empresa empresa = buscarEntidadeNoEscopo(id);

        if (empresa.getId().equals(TenantContext.getEmpresaId()))
            throw new IllegalStateException("A empresa do usuário autenticado não pode ser excluída.");

        empresaRepository.delete(empresa);
    }

    private Page<EmpresaListDTO> pesquisar(StatusEmpresa status, TipoEmpresa tipo, Long matrizId, Pageable pageable) {
        return empresaRepository.pesquisarNoEscopo(TenantContext.getEmpresaId(), status, tipo, matrizId, pageable)
                .map(empresaMapper::toListDTO);
    }

    /**
     * Busca uma empresa dentro do escopo do usuário autenticado.
     *
     * @param id identificador da empresa
     * @return entidade encontrada
     */
    private Empresa buscarEntidadeNoEscopo(Long id) {
        return empresaRepository.findById(id)
                .filter(this::pertenceAoEscopo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empresa não encontrada para o ID informado: " + id));
    }

    /**
     * A própria empresa do usuário autenticado ou uma filial vinculada a ela.
     */
    private boolean pertenceAoEscopo(Empresa empresa) {
        Long empresaId = TenantContext.getEmpresaId();
        return empresaId.equals(empresa.getId()) || empresaId.equals(empresa.getMatrizId());
    }

    /**
     * Valida se já existe empresa cadastrada com o CNPJ informado.
     *
     * @param cnpj CNPJ da empresa
     */
    private void validarCnpjDuplicado(String cnpj) {
        if (empresaRepository.existsByCnpj(cnpj))
            throw new IllegalArgumentException("Já existe uma empresa cadastrada com o CNPJ informado.");
    }
}
