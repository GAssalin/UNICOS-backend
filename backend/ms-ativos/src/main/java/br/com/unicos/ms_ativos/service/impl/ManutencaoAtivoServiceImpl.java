package br.com.unicos.ms_ativos.service.impl;

import br.com.unicos.ms_ativos.dto.ManutencaoAtivoListDTO;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link ManutencaoAtivoService}.
 * <p>
 * Responsável pela gestão das manutenções preventivas e corretivas dos ativos,
 * oferecendo métodos para controle, auditoria e análise de custos.
 */
@Service
@RequiredArgsConstructor
public class ManutencaoAtivoServiceImpl implements ManutencaoAtivoService {

    private final ManutencaoAtivoRepository manutencaoRepository;
    private final ModelMapper modelMapper;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    @Override
    @Transactional
    public ManutencaoAtivoResponse salvar(ManutencaoAtivoRequest request) {
        // Verifica duplicidade: manutenção idêntica na mesma data
        boolean existe = manutencaoRepository.findByAtivoId(request.ativoId())
                .stream()
                .anyMatch(m -> m.getDataManutencao().equals(request.dataManutencao())
                        && m.getTipo().equals(request.tipo()));

        if (existe) {
            throw new DataIntegrityViolationException("Já existe uma manutenção do mesmo tipo nesta data para o ativo informado.");
        }

        ManutencaoAtivo manutencao = modelMapper.map(request, ManutencaoAtivo.class);
        ManutencaoAtivo salvo = manutencaoRepository.save(manutencao);
        return modelMapper.map(salvo, ManutencaoAtivoResponse.class);
    }

    @Override
    @Transactional
    public ManutencaoAtivoResponse atualizar(Long id, ManutencaoAtivoRequest request) {
        ManutencaoAtivo existente = manutencaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manutenção não encontrada com ID: " + id));

        existente.setDataManutencao(request.dataManutencao());
        existente.setTipo(request.tipo());
        existente.setDescricaoServico(request.descricaoServico());
        existente.setCusto(request.custo());
        existente.setStatus(request.status());

        ManutencaoAtivo atualizado = manutencaoRepository.save(existente);
        return modelMapper.map(atualizado, ManutencaoAtivoResponse.class);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        ManutencaoAtivo manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manutenção não encontrada com ID: " + id));

        if (manutencao.getStatus() == StatusManutencao.EM_EXECUCAO ||
                manutencao.getStatus() == StatusManutencao.CONCLUIDA) {
            throw new DataIntegrityViolationException("Não é permitido excluir manutenções em execução ou concluídas.");
        }

        manutencaoRepository.delete(manutencao);
    }

    @Override
    public Optional<ManutencaoAtivoResponse> buscarPorId(Long id) {
        return manutencaoRepository.findById(id)
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoResponse.class));
    }

    @Override
    public List<ManutencaoAtivoListDTO> listarTodos() {
        return manutencaoRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    @Override
    public List<ManutencaoAtivoListDTO> buscarPorAtivo(Long ativoId) {
        return manutencaoRepository.findByAtivoId(ativoId)
                .stream()
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ManutencaoAtivoListDTO> buscarPorFornecedor(Long fornecedorId) {
        return manutencaoRepository.findByFornecedorId(fornecedorId)
                .stream()
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ManutencaoAtivoListDTO> buscarPorTipo(TipoManutencao tipo) {
        return manutencaoRepository.findByTipo(tipo)
                .stream()
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ManutencaoAtivoListDTO> buscarPorStatus(StatusManutencao status) {
        return manutencaoRepository.findByStatus(status)
                .stream()
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ManutencaoAtivoListDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return manutencaoRepository.findByDataManutencaoBetween(inicio, fim)
                .stream()
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ManutencaoAtivoListDTO> buscarAgendadasPara(LocalDate data) {
        return manutencaoRepository.findByDataManutencao(data)
                .stream()
                .filter(m -> m.getStatus() == StatusManutencao.ABERTA)
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 💰 RELATÓRIOS E ANÁLISES
    // ===========================================================

    @Override
    public BigDecimal calcularCustoTotalPorAtivo(Long ativoId) {
        return manutencaoRepository.calcularCustoTotalPorAtivo(ativoId) != null
                ? manutencaoRepository.calcularCustoTotalPorAtivo(ativoId)
                : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calcularCustoTotalPorPeriodo(LocalDate inicio, LocalDate fim) {
        return manutencaoRepository.findByDataManutencaoBetween(inicio, fim)
                .stream()
                .map(m -> m.getCusto() != null ? m.getCusto() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Long contarTotal() {
        return manutencaoRepository.count();
    }

    @Override
    public List<Object[]> contarPorTipo() {
        return manutencaoRepository.contarManutencoesPorTipo();
    }

    @Override
    public List<Object[]> contarPorStatus() {
        return manutencaoRepository.contarManutencoesPorStatus();
    }

    @Override
    public List<Object[]> buscarAtivosComManutencoesFrequentes() {
        return manutencaoRepository.contarManutencoesPorFornecedor()
                .stream()
                .filter(obj -> (Long) obj[1] > 1) // filtra ativos com mais de uma manutenção
                .collect(Collectors.toList());
    }

    @Override
    public List<ManutencaoAtivoListDTO> buscarRecentes(int limite) {
        return manutencaoRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getDataManutencao().compareTo(a.getDataManutencao()))
                .limit(limite)
                .map(entity -> modelMapper.map(entity, ManutencaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }
}
