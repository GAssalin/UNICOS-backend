package br.com.unicos.ms_ativos.service.impl;

import br.com.unicos.ms_ativos.dto.LocalizacaoListDTO;
import br.com.unicos.ms_ativos.dto.LocalizacaoRequest;
import br.com.unicos.ms_ativos.dto.LocalizacaoResponse;
import br.com.unicos.ms_ativos.model.HistoricoAtivo;
import br.com.unicos.ms_ativos.model.Localizacao;
import br.com.unicos.ms_ativos.repository.LocalizacaoRepository;
import br.com.unicos.ms_ativos.service.LocalizacaoService;
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
 * Implementação da interface {@link LocalizacaoService}.
 * <p>
 * Responsável pela manipulação e consultas de localizações físicas dentro das filiais,
 * garantindo integridade dos dados e consistência no mapeamento dos ativos.
 */
@Service
@RequiredArgsConstructor
public class LocalizacaoServiceImpl implements LocalizacaoService {

    private final LocalizacaoRepository localizacaoRepository;
    private final ModelMapper modelMapper;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    @Override
    @Transactional
    public LocalizacaoResponse salvar(LocalizacaoRequest request) {
        boolean existe = localizacaoRepository.existsByDescricaoIgnoreCaseAndFilialId(request.descricao(), request.filialId());

        if (existe) {
            throw new DataIntegrityViolationException("Já existe uma localização com esta descrição nesta filial.");
        }

        Localizacao localizacao = modelMapper.map(request, Localizacao.class);
        Localizacao salva = localizacaoRepository.save(localizacao);
        return modelMapper.map(salva, LocalizacaoResponse.class);
    }

    @Override
    @Transactional
    public LocalizacaoResponse atualizar(Long id, LocalizacaoRequest request) {
        Localizacao existente = localizacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com ID: " + id));

        existente.setDescricao(request.descricao());
        existente.setAndar(request.andar());
        existente.setBloco(request.bloco());
        existente.setFilialId(request.filialId());

        Localizacao atualizada = localizacaoRepository.save(existente);
        return modelMapper.map(atualizada, LocalizacaoResponse.class);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Localizacao localizacao = localizacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com ID: " + id));

        boolean possuiAtivos = localizacaoRepository.existsAtivosByLocalizacaoId(id);
        if (possuiAtivos) {
            throw new DataIntegrityViolationException("Não é possível excluir a localização pois existem ativos vinculados a ela.");
        }

        localizacaoRepository.delete(localizacao);
    }

    @Override
    public Optional<LocalizacaoResponse> buscarPorId(Long id) {
        return localizacaoRepository.findById(id)
                .map(entity -> modelMapper.map(entity, LocalizacaoResponse.class));
    }

    @Override
    public List<LocalizacaoListDTO> listarTodos() {
        return localizacaoRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(entity -> modelMapper.map(entity, LocalizacaoListDTO.class))
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    @Override
    public List<LocalizacaoListDTO> buscarPorFilial(Long filialId) {
        return localizacaoRepository.findByFilialId(filialId)
                .stream()
                .map(entity -> modelMapper.map(entity, LocalizacaoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LocalizacaoListDTO> buscarPorDescricao(String descricao) {
        return localizacaoRepository.findByDescricaoContainingIgnoreCase(descricao)
                .stream()
                .map(entity -> modelMapper.map(entity, LocalizacaoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existePorDescricaoEFilial(String descricao, Long filialId) {
        return localizacaoRepository.existsByDescricaoIgnoreCaseAndFilialId(descricao, filialId);
    }

    @Override
    public List<LocalizacaoListDTO> buscarComMaisDeUmAtivo() {
        return localizacaoRepository.findComMaisDeUmAtivo()
                .stream()
                .map(entity -> modelMapper.map(entity, LocalizacaoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoricoAtivo> buscarEventosRecentes() {
        // OBS: este método retorna eventos do histórico (não de localização),
        // conforme definido na interface LocalizacaoService.
        return List.of(); // método placeholder, pois a consulta ocorre no repositório de histórico
    }

    // ===========================================================
    // 📊 RELATÓRIOS E ORGANIZAÇÃO
    // ===========================================================

    @Override
    public Long contarPorFilial(Long filialId) {
        return localizacaoRepository.countByFilialId(filialId);
    }

    @Override
    public List<LocalizacaoListDTO> buscarSemAtivos() {
        return localizacaoRepository.buscarLocalizacoesSemAtivos()
                .stream()
                .map(entity -> modelMapper.map(entity, LocalizacaoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LocalizacaoListDTO> buscarComAtivos() {
        return localizacaoRepository.findAll()
                .stream()
                .filter(loc -> loc.getAtivos() != null && !loc.getAtivos().isEmpty())
                .map(entity -> modelMapper.map(entity, LocalizacaoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long contarTotal() {
        return localizacaoRepository.count();
    }
}
