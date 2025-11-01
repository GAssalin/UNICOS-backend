package br.com.erp.ms_ativos.service.impl;

import br.com.erp.ms_ativos.dto.LocalizacaoRequest;
import br.com.erp.ms_ativos.dto.LocalizacaoResponse;
import br.com.erp.ms_ativos.model.Localizacao;
import br.com.erp.ms_ativos.repository.LocalizacaoRepository;
import br.com.erp.ms_ativos.service.LocalizacaoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link LocalizacaoService}.
 *
 * Contém as regras de negócio e interações com o repositório de Localizacao.
 */
@Service
@RequiredArgsConstructor
public class LocalizacaoServiceImpl implements LocalizacaoService {

    private final LocalizacaoRepository localizacaoRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public LocalizacaoResponse salvar(LocalizacaoRequest request) {
        Localizacao entity = modelMapper.map(request, Localizacao.class);
        Localizacao salvo = localizacaoRepository.save(entity);
        return modelMapper.map(salvo, LocalizacaoResponse.class);
    }

    @Override
    @Transactional
    public LocalizacaoResponse atualizar(Long id, LocalizacaoRequest request) {
        Localizacao existente = localizacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada."));

        modelMapper.map(request, existente);
        Localizacao atualizada = localizacaoRepository.save(existente);
        return modelMapper.map(atualizada, LocalizacaoResponse.class);
    }

    @Override
    public Optional<LocalizacaoResponse> buscarPorId(Long id) {
        return localizacaoRepository.findById(id)
                .map(entity -> modelMapper.map(entity, LocalizacaoResponse.class));
    }

    @Override
    public List<LocalizacaoResponse> listarTodas() {
        return localizacaoRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, LocalizacaoResponse.class))
                .toList();
    }

    @Override
    public List<LocalizacaoResponse> buscarPorFilial(Long filialId) {
        return localizacaoRepository.findByFilialId(filialId)
                .stream()
                .map(entity -> modelMapper.map(entity, LocalizacaoResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!localizacaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Localização não encontrada para exclusão.");
        }
        localizacaoRepository.deleteById(id);
    }
}