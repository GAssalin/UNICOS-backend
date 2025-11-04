package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.CargoRequest;
import br.com.unicos.ms_pessoas.dto.CargoResponse;
import br.com.unicos.ms_pessoas.model.Cargo;
import br.com.unicos.ms_pessoas.repository.CargoRepository;
import br.com.unicos.ms_pessoas.service.CargoService;
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
 * Implementação da interface {@link CargoService}.
 *
 * <p>Gerencia os cargos existentes no sistema, garantindo unicidade por nome e
 * fornecendo métodos de criação, atualização, listagem e exclusão.</p>
 */
@Service
@RequiredArgsConstructor
public class CargoServiceImpl implements CargoService {

    private final CargoRepository cargoRepository;
    private final ModelMapper modelMapper;

    /**
     * Cria e salva um novo cargo.
     *
     * @param request DTO com os dados do cargo
     * @return cargo criado
     * @throws DataIntegrityViolationException se já existir um cargo com o mesmo nome
     */
    @Override
    @Transactional
    public CargoResponse salvar(CargoRequest request) {
        if (cargoRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new DataIntegrityViolationException("Já existe um cargo com o nome informado.");
        }

        Cargo cargo = modelMapper.map(request, Cargo.class);
        cargo = cargoRepository.save(cargo);

        return modelMapper.map(cargo, CargoResponse.class);
    }

    /**
     * Atualiza os dados de um cargo existente.
     *
     * @param id      ID do cargo
     * @param request novos dados
     * @return cargo atualizado
     * @throws EntityNotFoundException         se o cargo não for encontrado
     * @throws DataIntegrityViolationException se o novo nome já estiver sendo usado
     */
    @Override
    @Transactional
    public CargoResponse atualizar(Long id, CargoRequest request) {
        Cargo cargo = cargoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo não encontrado."));

        // Evita duplicidade de nome
        if (!cargo.getNome().equalsIgnoreCase(request.nome())
                && cargoRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new DataIntegrityViolationException("Já existe outro cargo com o nome informado.");
        }

        modelMapper.map(request, cargo);
        cargo = cargoRepository.save(cargo);

        return modelMapper.map(cargo, CargoResponse.class);
    }

    /**
     * Lista todos os cargos cadastrados.
     *
     * @return lista de cargos
     */
    @Override
    @Transactional(readOnly = true)
    public List<CargoResponse> listarTodos() {
        return cargoRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CargoResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca um cargo pelo ID.
     *
     * @param id identificador do cargo
     * @return cargo (se encontrado)
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CargoResponse> buscarPorId(Long id) {
        return cargoRepository.findById(id)
                .map(c -> modelMapper.map(c, CargoResponse.class));
    }

    /**
     * Exclui um cargo pelo ID.
     *
     * @param id identificador do cargo
     * @throws EntityNotFoundException se o cargo não for encontrado
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        Cargo cargo = cargoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo não encontrado."));
        cargoRepository.delete(cargo);
    }
}
