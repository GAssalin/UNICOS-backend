package br.com.unicos.ms_ativos.service.impl;

import br.com.unicos.ms_ativos.dto.HistoricoAtivoListDTO;
import br.com.unicos.ms_ativos.dto.HistoricoAtivoRequest;
import br.com.unicos.ms_ativos.dto.HistoricoAtivoResponse;
import br.com.unicos.ms_ativos.model.HistoricoAtivo;
import br.com.unicos.ms_ativos.repository.HistoricoAtivoRepository;
import br.com.unicos.ms_ativos.service.HistoricoAtivoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link HistoricoAtivoService}.
 * <p>
 * Responsável pela manipulação dos registros de histórico de eventos
 * dos ativos, incluindo criação, atualização, exclusão e consultas
 * específicas para auditoria e relatórios.
 */
@Service
@RequiredArgsConstructor
public class HistoricoAtivoServiceImpl implements HistoricoAtivoService {

    private final HistoricoAtivoRepository historicoRepository;
    private final ModelMapper modelMapper;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    @Override
    @Transactional
    public HistoricoAtivoResponse salvar(HistoricoAtivoRequest request) {
        boolean existe = historicoRepository.existsByAtivoIdAndDescricaoEventoAndDataEventoBetween(
                request.ativoId(),
                request.descricaoEvento(),
                request.dataEvento().minusSeconds(2),
                request.dataEvento().plusSeconds(2)
        );

        if (existe) {
            throw new DataIntegrityViolationException(
                    "Já existe um evento semelhante registrado nesse intervalo para o ativo informado."
            );
        }

        HistoricoAtivo entity = modelMapper.map(request, HistoricoAtivo.class);
        HistoricoAtivo salvo = historicoRepository.save(entity);
        return modelMapper.map(salvo, HistoricoAtivoResponse.class);
    }

    @Override
    @Transactional
    public HistoricoAtivoResponse atualizar(Long id, HistoricoAtivoRequest request) {
        HistoricoAtivo existente = historicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Histórico não encontrado com ID: " + id));

        existente.setDescricaoEvento(request.descricaoEvento());
        existente.setDataEvento(request.dataEvento());
        existente.setUsuarioResponsavelId(request.usuarioResponsavelId());

        HistoricoAtivo atualizado = historicoRepository.save(existente);
        return modelMapper.map(atualizado, HistoricoAtivoResponse.class);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        HistoricoAtivo entity = historicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Histórico não encontrado com ID: " + id));
        historicoRepository.delete(entity);
    }

    @Override
    public Optional<HistoricoAtivoResponse> buscarPorId(Long id) {
        return historicoRepository.findById(id)
                .map(entity -> modelMapper.map(entity, HistoricoAtivoResponse.class));
    }

    @Override
    public List<HistoricoAtivoListDTO> listarTodos() {
        return historicoRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, HistoricoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    @Override
    public List<HistoricoAtivoListDTO> buscarPorAtivo(Long ativoId) {
        return historicoRepository.findByAtivoIdOrderByDataEventoDesc(ativoId)
                .stream()
                .map(entity -> modelMapper.map(entity, HistoricoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoricoAtivoListDTO> buscarPorUsuarioResponsavel(Long usuarioId) {
        return historicoRepository.findByUsuarioResponsavelId(usuarioId)
                .stream()
                .map(entity -> modelMapper.map(entity, HistoricoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoricoAtivoListDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return historicoRepository.findByDataEventoBetweenOrderByDataEventoDesc(inicio, fim)
                .stream()
                .map(entity -> modelMapper.map(entity, HistoricoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<HistoricoAtivoResponse> buscarUltimoEventoPorAtivo(Long ativoId) {
        return historicoRepository.findTopByAtivoIdOrderByDataEventoDesc(ativoId)
                .map(entity -> modelMapper.map(entity, HistoricoAtivoResponse.class));
    }

    @Override
    public List<HistoricoAtivoListDTO> buscarEventosDeStatus(Long ativoId) {
        return historicoRepository.buscarEventosDeAlteracaoDeStatus()
                .stream()
                .filter(evento -> evento.getAtivo().getId().equals(ativoId))
                .map(entity -> modelMapper.map(entity, HistoricoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 📊 RELATÓRIOS E ANÁLISE
    // ===========================================================

    @Override
    public Long contarTotalEventos() {
        return historicoRepository.count();
    }

    @Override
    public Long contarEventosPorAtivo(Long ativoId) {
        return historicoRepository.findByAtivoId(ativoId).stream().count();
    }

    @Override
    public List<Object[]> contarEventosPorUsuario() {
        return historicoRepository.contarEventosPorUsuario();
    }

    @Override
    public List<HistoricoAtivoListDTO> buscarEventosRecentes(int limite) {
        return historicoRepository.buscarEventosRecentes()
                .stream()
                .limit(limite)
                .map(entity -> modelMapper.map(entity, HistoricoAtivoListDTO.class))
                .collect(Collectors.toList());
    }
}
