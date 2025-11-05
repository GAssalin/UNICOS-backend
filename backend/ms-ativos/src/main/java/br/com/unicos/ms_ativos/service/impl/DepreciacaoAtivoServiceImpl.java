package br.com.unicos.ms_ativos.service.impl;

import br.com.unicos.ms_ativos.dto.DepreciacaoAtivoListDTO;
import br.com.unicos.ms_ativos.dto.DepreciacaoAtivoRequest;
import br.com.unicos.ms_ativos.dto.DepreciacaoAtivoResponse;
import br.com.unicos.ms_ativos.enums.TipoDepreciacao;
import br.com.unicos.ms_ativos.model.DepreciacaoAtivo;
import br.com.unicos.ms_ativos.repository.DepreciacaoAtivoRepository;
import br.com.unicos.ms_ativos.service.DepreciacaoAtivoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link DepreciacaoAtivoService}.
 * <p>
 * Contém as regras de negócio relacionadas às depreciações aplicadas aos ativos patrimoniais.
 * Inclui cálculos de totais, médias e saldos contábeis.
 */
@Service
@RequiredArgsConstructor
public class DepreciacaoAtivoServiceImpl implements DepreciacaoAtivoService {

    private final DepreciacaoAtivoRepository depreciacaoAtivoRepository;
    private final ModelMapper modelMapper;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DepreciacaoAtivoResponse salvar(DepreciacaoAtivoRequest request) {
        DepreciacaoAtivo depreciacao = modelMapper.map(request, DepreciacaoAtivo.class);

        // Impede duplicidade por ativo + data de competência
        boolean existe = depreciacaoAtivoRepository.existsByAtivoIdAndDataCompetencia(
                request.ativoId(), request.dataCompetencia()
        );

        if (existe) {
            throw new DataIntegrityViolationException(
                    "Já existe uma depreciação registrada para o ativo nesta competência."
            );
        }

        DepreciacaoAtivo salva = depreciacaoAtivoRepository.save(depreciacao);
        return modelMapper.map(salva, DepreciacaoAtivoResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DepreciacaoAtivoResponse atualizar(Long id, DepreciacaoAtivoRequest request) {
        DepreciacaoAtivo existente = depreciacaoAtivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Depreciação não encontrada com ID: " + id));

        existente.setTipo(request.tipo());
        existente.setDataCompetencia(request.dataCompetencia());
        existente.setValorDepreciado(request.valorDepreciado());
        existente.setSaldoContabil(request.saldoContabil());

        DepreciacaoAtivo atualizado = depreciacaoAtivoRepository.save(existente);
        return modelMapper.map(atualizado, DepreciacaoAtivoResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        DepreciacaoAtivo entity = depreciacaoAtivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Depreciação não encontrada com ID: " + id));
        depreciacaoAtivoRepository.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DepreciacaoAtivoResponse> buscarPorId(Long id) {
        return depreciacaoAtivoRepository.findById(id)
                .map(entity -> modelMapper.map(entity, DepreciacaoAtivoResponse.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DepreciacaoAtivoListDTO> listarTodos() {
        return depreciacaoAtivoRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, DepreciacaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DepreciacaoAtivoListDTO> buscarPorAtivo(Long ativoId) {
        return depreciacaoAtivoRepository.findByAtivoId(ativoId)
                .stream()
                .map(entity -> modelMapper.map(entity, DepreciacaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DepreciacaoAtivoListDTO> buscarPorTipo(TipoDepreciacao tipo) {
        return depreciacaoAtivoRepository.findByTipo(tipo)
                .stream()
                .map(entity -> modelMapper.map(entity, DepreciacaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DepreciacaoAtivoListDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return depreciacaoAtivoRepository.findByDataCompetenciaBetween(inicio, fim)
                .stream()
                .map(entity -> modelMapper.map(entity, DepreciacaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DepreciacaoAtivoResponse> buscarPorCompetencia(Long ativoId, LocalDate dataCompetencia) {
        return depreciacaoAtivoRepository.findByAtivoIdAndDataCompetencia(ativoId, dataCompetencia)
                .map(entity -> modelMapper.map(entity, DepreciacaoAtivoResponse.class));
    }

    // ===========================================================
    // 💰 CONSULTAS CONTÁBEIS E RELATÓRIOS
    // ===========================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal calcularValorTotalDepreciado(Long ativoId) {
        return depreciacaoAtivoRepository.sumValorDepreciadoByAtivoId(ativoId)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BigDecimal> buscarSaldoContabilAtual(Long ativoId) {
        return depreciacaoAtivoRepository.findTopByAtivoIdOrderByDataCompetenciaDesc(ativoId)
                .map(DepreciacaoAtivo::getSaldoContabil);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal calcularMediaDepreciacaoMensal(Long ativoId) {
        List<BigDecimal> valores = depreciacaoAtivoRepository.findValoresDepreciadosByAtivoId(ativoId);

        if (valores == null || valores.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal total = valores.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total
                .divide(BigDecimal.valueOf(valores.size()), 2, RoundingMode.HALF_UP);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DepreciacaoAtivoListDTO> buscarDepreciacoesDoMesAtual() {
        LocalDate inicio = LocalDate.now().withDayOfMonth(1);
        LocalDate fim = inicio.plusMonths(1).minusDays(1);

        return depreciacaoAtivoRepository.findByDataCompetenciaBetween(inicio, fim)
                .stream()
                .map(entity -> modelMapper.map(entity, DepreciacaoAtivoListDTO.class))
                .collect(Collectors.toList());
    }
}
