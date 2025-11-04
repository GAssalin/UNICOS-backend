package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.ColaboradorRequest;
import br.com.unicos.ms_pessoas.dto.ColaboradorResponse;
import br.com.unicos.ms_pessoas.model.Cargo;
import br.com.unicos.ms_pessoas.model.Colaborador;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.model.PessoaFisica;
import br.com.unicos.ms_pessoas.repository.CargoRepository;
import br.com.unicos.ms_pessoas.repository.ColaboradorRepository;
import br.com.unicos.ms_pessoas.repository.PessoaFisicaRepository;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import br.com.unicos.ms_pessoas.service.ColaboradorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link ColaboradorService}.
 *
 * <p>Gerencia as regras de negócio e persistência da entidade {@link Colaborador},
 * garantindo integridade nos vínculos com Pessoa, Cargo e Empresa.</p>
 */
@Service
@RequiredArgsConstructor
public class ColaboradorServiceImpl implements ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final PessoaRepository pessoaRepository;
    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final CargoRepository cargoRepository;
    private final ModelMapper modelMapper;

    /**
     * Cria e salva um novo colaborador.
     *
     * @param request DTO com os dados do colaborador
     * @return colaborador criado
     * @throws EntityNotFoundException se a pessoa ou cargo não existirem
     * @throws DataIntegrityViolationException se já existir colaborador com a mesma matrícula
     */
    @Override
    @Transactional
    public ColaboradorResponse salvar(ColaboradorRequest request) {
        PessoaFisica pessoa = pessoaFisicaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa física não encontrada."));

        Cargo cargo = null;
        if (request.cargoId() != null) {
            cargo = cargoRepository.findById(request.cargoId())
                    .orElseThrow(() -> new EntityNotFoundException("Cargo não encontrado."));
        }

        if (request.matricula() != null &&
                colaboradorRepository.findByMatricula(request.matricula()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um colaborador com a matrícula informada.");
        }

        Colaborador colaborador = modelMapper.map(request, Colaborador.class);
        colaborador.setPessoa(pessoa);
        colaborador.setCargo(cargo);

        colaborador = colaboradorRepository.save(colaborador);
        return modelMapper.map(colaborador, ColaboradorResponse.class);
    }

    /**
     * Atualiza os dados de um colaborador existente.
     *
     * @param id      ID do colaborador
     * @param request DTO com os novos dados
     * @return colaborador atualizado
     * @throws EntityNotFoundException se o colaborador, pessoa ou cargo não existirem
     * @throws DataIntegrityViolationException se a matrícula já estiver em uso por outro colaborador
     */
    @Override
    @Transactional
    public ColaboradorResponse atualizar(Long id, ColaboradorRequest request) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado."));

        PessoaFisica pessoa = pessoaFisicaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa física não encontrada."));

        Cargo cargo = null;
        if (request.cargoId() != null) {
            cargo = cargoRepository.findById(request.cargoId())
                    .orElseThrow(() -> new EntityNotFoundException("Cargo não encontrado."));
        }

        // Verifica duplicidade de matrícula
        if (request.matricula() != null) {
            colaboradorRepository.findByMatricula(request.matricula()).ifPresent(outro -> {
                if (!outro.getId().equals(id)) {
                    throw new DataIntegrityViolationException("Já existe outro colaborador com a matrícula informada.");
                }
            });
        }

        modelMapper.map(request, colaborador);
        colaborador.setPessoa(pessoa);
        colaborador.setCargo(cargo);

        colaborador = colaboradorRepository.save(colaborador);
        return modelMapper.map(colaborador, ColaboradorResponse.class);
    }

    /**
     * Lista todos os colaboradores de uma empresa.
     *
     * <p>Os vínculos com Empresa e Departamento são realizados via ID,
     * pois as entidades correspondentes pertencem ao microserviço ms-empresa.</p>
     *
     * @param empresaId ID da empresa
     * @return lista de colaboradores associados
     */
    @Override
    @Transactional(readOnly = true)
    public List<ColaboradorResponse> listarPorEmpresa(Long empresaId) {
        return colaboradorRepository.findByEmpresaId(empresaId)
                .stream()
                .map(c -> modelMapper.map(c, ColaboradorResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca um colaborador pelo ID.
     *
     * @param id identificador do colaborador
     * @return colaborador (se encontrado)
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ColaboradorResponse> buscarPorId(Long id) {
        return colaboradorRepository.findById(id)
                .map(c -> modelMapper.map(c, ColaboradorResponse.class));
    }

    /**
     * Exclui um colaborador pelo ID.
     *
     * @param id identificador do colaborador
     * @throws EntityNotFoundException se o colaborador não for encontrado
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado."));
        colaboradorRepository.delete(colaborador);
    }
}
