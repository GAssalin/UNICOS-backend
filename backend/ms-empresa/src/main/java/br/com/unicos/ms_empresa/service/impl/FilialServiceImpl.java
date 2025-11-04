package br.com.unicos.ms_empresa.service.impl;

import br.com.unicos.ms_empresa.dto.*;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EnderecoEmpresa;
import br.com.unicos.ms_empresa.model.Filial;
import br.com.unicos.ms_empresa.repository.EmpresaRepository;
import br.com.unicos.ms_empresa.repository.FilialRepository;
import br.com.unicos.ms_empresa.service.FilialService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link FilialService}.
 * Contém as regras de negócio e interações com o repositório de Filial.
 */
@Service
@Transactional
public class FilialServiceImpl implements FilialService {

    private final FilialRepository filialRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    public FilialServiceImpl(FilialRepository filialRepository,
                             EmpresaRepository empresaRepository,
                             ModelMapper modelMapper) {
        this.filialRepository = filialRepository;
        this.empresaRepository = empresaRepository;
        this.modelMapper = modelMapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public FilialResponse salvar(FilialRequest request) {
        if (filialRepository.existsByCnpj(request.cnpj())) {
            throw new DataIntegrityViolationException("Já existe uma filial cadastrada com o CNPJ informado.");
        }

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        Filial filial = modelMapper.map(request, Filial.class);
        filial.setEmpresa(empresa);

        Filial salva = filialRepository.save(filial);
        return toResponse(salva);
    }

    @Override
    public FilialResponse atualizar(Long id, FilialRequest request) {
        Filial existente = filialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada."));

        // Evita duplicidade de CNPJ em outra filial
        filialRepository.findByCnpj(request.cnpj()).ifPresent(outro -> {
            if (!outro.getId().equals(id)) {
                throw new DataIntegrityViolationException("Já existe outra filial com o mesmo CNPJ informado.");
            }
        });

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        modelMapper.map(request, existente);
        existente.setEmpresa(empresa);

        Filial atualizada = filialRepository.save(existente);
        return toResponse(atualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FilialResponse> buscarPorId(Long id) {
        return filialRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FilialResponse> buscarPorCnpj(String cnpj) {
        return filialRepository.findByCnpj(cnpj)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> listarTodas() {
        return filialRepository.findAll()
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> listarPorEmpresa(Long empresaId) {
        return filialRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> listarAtivas() {
        return filialRepository.findByAtivoTrue()
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> listarInativas() {
        return filialRepository.findByAtivoFalse()
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> listarPorEmpresaAtivas(Long empresaId) {
        return filialRepository.findByEmpresaIdAndAtivoTrue(empresaId)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> buscarPorNome(String nome) {
        return filialRepository.findByNomeFantasiaContainingIgnoreCase(nome)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> listarOrdenadasPorNome() {
        return filialRepository.findAll().stream()
                .sorted(Comparator.comparing(Filial::getNome, String.CASE_INSENSITIVE_ORDER))
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> listarPorEmpresaOrdenadas(Long empresaId) {
        return filialRepository.findByEmpresaId(empresaId).stream()
                .sorted(Comparator.comparing(Filial::getNome, String.CASE_INSENSITIVE_ORDER))
                .map(this::toListDTO)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        if (!filialRepository.existsById(id)) {
            throw new EntityNotFoundException("Filial não encontrada para exclusão.");
        }
        filialRepository.deleteById(id);
    }

    // ==================================
    // MÉTODOS AUXILIARES
    // ==================================

    private FilialResponse toResponse(Filial entity) {
        Empresa empresa = entity.getEmpresa();
        EnderecoEmpresa endereco = entity.getEndereco();

        return new FilialResponse(
                entity.getId(),
                entity.getNome(),
                entity.getCnpj(),
                entity.getAtivo(),
                empresa != null
                        ? new EmpresaListDTO(empresa.getId(), empresa.getNomeFantasia(), empresa.getCnpj())
                        : null,
                endereco != null
                        ? new EnderecoEmpresaResponse(
                        endereco.getId(),
                        endereco.getLogradouro(),
                        endereco.getNumero(),
                        endereco.getComplemento(),
                        endereco.getBairro(),
                        endereco.getCidade(),
                        endereco.getEstado(),
                        endereco.getCep(),
                        endereco.getTipoEndereco())
                        : null
        );
    }

    private FilialListDTO toListDTO(Filial entity) {
        return new FilialListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCnpj(),
                entity.getAtivo()
        );
    }
}
