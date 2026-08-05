package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_pessoas.dto.contato.ContatoListDTO;
import br.com.unicos.ms_pessoas.dto.contato.ContatoRequest;
import br.com.unicos.ms_pessoas.dto.contato.ContatoResponse;
import br.com.unicos.ms_pessoas.enums.TipoContato;
import br.com.unicos.ms_pessoas.mapper.ContatoMapper;
import br.com.unicos.ms_pessoas.model.Contato;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.repository.ContatoRepository;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContatoService extends BaseTenantService<Contato, Long> {

    private final ContatoRepository contatoRepository;
    private final PessoaRepository pessoaRepository;
    private final ContatoMapper contatoMapper;

    public ContatoService(ContatoRepository contatoRepository, PessoaRepository pessoaRepository, ContatoMapper contatoMapper) {
        super(contatoRepository);
        this.contatoRepository = contatoRepository;
        this.pessoaRepository = pessoaRepository;
        this.contatoMapper = contatoMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    public ContatoResponse salvar(ContatoRequest request) {
        Pessoa pessoa = buscarPessoa(request.pessoaId());
        validarContatoDuplicado(pessoa, request.valor());

        if (request.principal())
            removerContatoPrincipalAtual(pessoa);

        Contato contato = Contato.builder()
                .pessoa(pessoa)
                .tipo(request.tipo())
                .valor(request.valor())
                .principal(request.principal())
                .empresaId(TenantContext.getEmpresaId())
                .build();

        return contatoMapper.toResponse(contatoRepository.save(contato));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    public ContatoResponse atualizar(Long id, ContatoRequest request) {
        Contato contato = buscarContato(id);
        Pessoa pessoa = buscarPessoa(request.pessoaId());

        if (!contato.getValor().equalsIgnoreCase(request.valor())) {
            validarContatoDuplicado(pessoa, request.valor());
            contato.setValor(request.valor());
        }

        contato.setTipo(request.tipo());

        if (request.principal()) {
            removerContatoPrincipalAtual(pessoa);
            contato.setPrincipal(true);
        } else {
            contato.setPrincipal(false);
        }

        return contatoMapper.toResponse(contatoRepository.save(contato));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    public ContatoResponse buscarPorId(Long id) {
        return contatoMapper.toResponse(buscarContato(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    public Page<ContatoListDTO> listarPorPessoa(Long pessoaId, Pageable pageable) {
        return contatoRepository
                .findByPessoaAndEmpresaId(buscarPessoa(pessoaId), TenantContext.getEmpresaId(), pageable)
                .map(contatoMapper::toListDTO);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-contato-admin", fallbackMethod = "fallbackAdminPageTipo")
    public Page<ContatoListDTO> listarPorPessoaETipo(Long pessoaId, TipoContato tipo, Pageable pageable) {
        return contatoRepository
                .findByPessoaAndTipoAndEmpresaId(
                        buscarPessoa(pessoaId),
                        tipo,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(contatoMapper::toListDTO);
    }

    @Transactional
    public void deletar(Long id) {
        contatoRepository.delete(buscarContato(id));
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Contato buscarContato(Long id) {
        return contatoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado: " + id));
    }

    private Pessoa buscarPessoa(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada: " + pessoaId));
    }

    private void validarContatoDuplicado(Pessoa pessoa, String valor) {
        if (contatoRepository.existsByPessoaAndValorAndEmpresaId(pessoa, valor, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um contato com o valor informado para esta pessoa.");
    }

    private void removerContatoPrincipalAtual(Pessoa pessoa) {
        contatoRepository
                .findByPessoaAndPrincipalTrueAndEmpresaId(pessoa, TenantContext.getEmpresaId())
                .ifPresent(contato -> {
                    contato.setPrincipal(false);
                    contatoRepository.save(contato);
                });
    }
}
