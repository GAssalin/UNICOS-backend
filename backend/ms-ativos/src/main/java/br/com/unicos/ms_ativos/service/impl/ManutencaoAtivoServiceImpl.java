package br.com.unicos.ms_ativos.service.impl;

import br.com.unicos.ms_ativos.dto.ManutencaoAtivoRequest;
import br.com.unicos.ms_ativos.dto.ManutencaoAtivoResponse;
import br.com.unicos.ms_ativos.enums.StatusManutencao;
import br.com.unicos.ms_ativos.enums.TipoManutencao;
import br.com.unicos.ms_ativos.model.ManutencaoAtivo;
import br.com.unicos.ms_ativos.repository.ManutencaoAtivoRepository;
import br.com.unicos.ms_ativos.service.ManutencaoAtivoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link ManutencaoAtivoService}.
 *
 * Contém as regras de negócio e interações com o repositório de ManutencaoAtivo.
 */
@Service
@RequiredArgsConstructor
public class ManutencaoAtivoServiceImpl implements ManutencaoAtivoService {

    private final ManutencaoAtivoRepository manutencaoAtivoRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ManutencaoAtivoResponse salvar(ManutencaoAtivoRequest request) {
        ManutencaoAtivo entity = modelMapper.map(request, ManutencaoAtivo.class);
        ManutencaoAtivo salvo = manutencaoAtivoRepository.save(entity);
        return modelMapper.map(salvo, ManutencaoAtivoResponse.class);
    }

    @Override
    @Transactional
    public ManutencaoAtivoResponse atualizar(Long id, ManutencaoAtivoRequest request) {
        ManutencaoAtivo existente = manutencaoAtivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manutenção não encontrada."));

        modelMapper.map(request, existente);
        ManutencaoAtivo atualizado = manutencaoAtivoRepository.save(existente);
        return modelMapper.map(atualizado, ManutencaoAtivoResponse.class);
    }

    @Override
    public Optional<ManutencaoAtivoResponse> buscarPorId(Long id) {
        return manutencaoAtivoRepository.findById(id)
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoResponse.class));
    }

    @Override
    public List<ManutencaoAtivoResponse> listarTodas() {
        return manutencaoAtivoRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoResponse.class))
                .toList();
    }

    @Override
    public List<ManutencaoAtivoResponse> buscarPorAtivo(Long ativoId) {
        return manutencaoAtivoRepository.findByAtivoId(ativoId)
                .stream()
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoResponse.class))
                .toList();
    }

    @Override
    public List<ManutencaoAtivoResponse> buscarPorTipo(TipoManutencao tipo) {
        return manutencaoAtivoRepository.findByTipo(tipo)
                .stream()
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoResponse.class))
                .toList();
    }

    @Override
    public List<ManutencaoAtivoResponse> buscarPorStatus(StatusManutencao status) {
        return manutencaoAtivoRepository.findByStatus(status)
                .stream()
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoResponse.class))
                .toList();
    }

    @Override
    public List<ManutencaoAtivoResponse> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return manutencaoAtivoRepository.findByDataManutencaoBetween(inicio, fim)
                .stream()
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!manutencaoAtivoRepository.existsById(id)) {
            throw new EntityNotFoundException("Manutenção não encontrada para exclusão.");
        }
        manutencaoAtivoRepository.deleteById(id);
    }
}