package br.com.unicos.ms_empresa.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do service responsável pelas regras de negócio da entidade Empresa.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;

    /**
     * Cadastra uma nova empresa.
     *
     * @param request dados para criação da empresa
     * @return empresa cadastrada
     */
    public EmpresaResponse criar(EmpresaCreateRequest request) {
        validarCnpjDuplicado(request.cnpj());

        Empresa empresa = empresaMapper.toEntity(request);

        if (TipoEmpresa.MATRIZ.equals(request.tipoEmpresa()))
            empresa.setMatrizId(null);

        Empresa empresaSalva = empresaRepository.save(empresa);

        if (TipoEmpresa.MATRIZ.equals(empresaSalva.getTipoEmpresa())) {
            empresaSalva.setMatrizId(empresaSalva.getId());
            empresaSalva = empresaRepository.save(empresaSalva);
        }

        return empresaMapper.toResponse(empresaSalva);
    }

    /**
     * Busca uma empresa pelo ID.
     *
     * @param id identificador da empresa
     * @return empresa encontrada
     */
    @Transactional(readOnly = true)
    public EmpresaResponse buscarPorId(Long id) {
        Empresa empresa = buscarEntidadePorId(id);
        return empresaMapper.toResponse(empresa);
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
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empresa não encontrada para o CNPJ informado: " + cnpj));

        return empresaMapper.toResponse(empresa);
    }

    /**
     * Lista todas as empresas de forma paginada.
     *
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @Transactional(readOnly = true)
    public Page<EmpresaListDTO> listarTodas(Pageable pageable) {
        return empresaRepository.findAll(pageable)
                .map(empresaMapper::toListDTO);
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
        return empresaRepository.findByStatusEmpresa(statusEmpresa, pageable)
                .map(empresaMapper::toListDTO);
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
        return empresaRepository.findByTipoEmpresa(tipoEmpresa, pageable)
                .map(empresaMapper::toListDTO);
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
        return empresaRepository.findByMatrizId(matrizId, pageable)
                .map(empresaMapper::toListDTO);
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
        return empresaRepository.findByMatrizIdAndTipoEmpresa(matrizId, tipoEmpresa, pageable)
                .map(empresaMapper::toListDTO);
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
        return empresaRepository.findByMatrizIdAndStatusEmpresa(matrizId, statusEmpresa, pageable)
                .map(empresaMapper::toListDTO);
    }

    /**
     * Atualiza os dados de uma empresa.
     *
     * @param id identificador da empresa
     * @param request dados para atualização
     * @return empresa atualizada
     */
    public EmpresaResponse atualizar(Long id, EmpresaUpdateRequest request) {
        Empresa empresa = buscarEntidadePorId(id);

        empresaMapper.updateEntity(empresa, request);

        if (TipoEmpresa.MATRIZ.equals(request.tipoEmpresa()))
            empresa.setMatrizId(empresa.getId());

        Empresa empresaAtualizada = empresaRepository.save(empresa);
        return empresaMapper.toResponse(empresaAtualizada);
    }

    /**
     * Remove uma empresa pelo ID.
     *
     * @param id identificador da empresa
     */
    public void deletar(Long id) {
        Empresa empresa = buscarEntidadePorId(id);
        empresaRepository.delete(empresa);
    }

    /**
     * Busca uma entidade Empresa pelo ID.
     *
     * @param id identificador da empresa
     * @return entidade encontrada
     */
    private Empresa buscarEntidadePorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empresa não encontrada para o ID informado: " + id));
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