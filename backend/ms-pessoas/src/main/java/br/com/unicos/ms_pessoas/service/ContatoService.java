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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementação das regras de negócio relacionadas aos contatos de pessoas.
 *
 * <p>
 * Responsável por garantir:
 * <ul>
 *     <li>Isolamento multi-tenant</li>
 *     <li>Validação de duplicidade</li>
 *     <li>Gerenciamento de contato principal</li>
 *     <li>Controle de permissões</li>
 * </ul>
 * </p>
 */
@Service
@Transactional
public class ContatoService extends BaseTenantService<Contato, Long> {

    private final ContatoRepository contatoRepository;
    private final PessoaRepository pessoaRepository;
    private final ContatoMapper contatoMapper;
    private final PermissionCheckService permissionCheckService;

    public ContatoService(
            ContatoRepository contatoRepository,
            PessoaRepository pessoaRepository,
            ContatoMapper contatoMapper,
            PermissionCheckService permissionCheckService
    ) {
        super(contatoRepository);
        this.contatoRepository = contatoRepository;
        this.pessoaRepository = pessoaRepository;
        this.contatoMapper = contatoMapper;
        this.permissionCheckService = permissionCheckService;
    }

    // ============================================================
    // CRUD
    // ============================================================

    /**
     * Cadastra um novo contato para uma pessoa.
     */
    public ContatoResponse salvar(ContatoRequest request) {
        if (!permissionCheckService.hasPermission("CONTATO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para criar contatos.");

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

    /**
     * Atualiza um contato existente.
     */
    @Transactional
    public ContatoResponse atualizar(Long id, ContatoRequest request) {
        if (!permissionCheckService.hasPermission("CONTATO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para editar contatos.");

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

    /**
     * Busca um contato pelo identificador.
     */
    @Transactional(readOnly = true)
    public ContatoResponse buscarPorId(Long id) {
        if (!permissionCheckService.hasPermission("CONTATO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar contatos.");
        return contatoMapper.toResponse(buscarContato(id));
    }

    // ============================================================
    // LISTAGENS
    // ============================================================

    /**
     * Lista contatos de uma pessoa de forma paginada.
     */
    @Transactional(readOnly = true)
    public Page<ContatoListDTO> listarPorPessoa(Long pessoaId, Pageable pageable) {
        if (!permissionCheckService.hasPermission("CONTATO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar contatos.");

        Pessoa pessoa = buscarPessoa(pessoaId);
        return contatoRepository
                .findByPessoaAndEmpresaId(pessoa, TenantContext.getEmpresaId(), pageable)
                .map(contatoMapper::toListDTO);
    }

    /**
     * Lista contatos de uma pessoa filtrando pelo tipo.
     */
    @Transactional(readOnly = true)
    public Page<ContatoListDTO> listarPorPessoaETipo(
            Long pessoaId,
            TipoContato tipo,
            Pageable pageable
    ) {
        if (!permissionCheckService.hasPermission("CONTATO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar contatos.");

        Pessoa pessoa = buscarPessoa(pessoaId);
        return contatoRepository
                .findByPessoaAndTipoAndEmpresaId(
                        pessoa,
                        tipo,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(contatoMapper::toListDTO);
    }

    // ============================================================
    // EXCLUSÃO
    // ============================================================

    /**
     * Remove um contato.
     */
    @Transactional
    public void deletar(Long id) {
        if (!permissionCheckService.hasPermission("CONTATO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir contatos.");
        contatoRepository.delete(buscarContato(id));
    }

    // ============================================================
    // MÉTODOS AUXILIARES
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
        if (contatoRepository.existsByPessoaAndValorAndEmpresaId(
                pessoa,
                valor,
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Já existe um contato com o valor informado para esta pessoa.");
        }
    }

    /**
     * Garante que exista apenas um contato principal por pessoa.
     */
    private void removerContatoPrincipalAtual(Pessoa pessoa) {
        contatoRepository
                .findByPessoaAndPrincipalTrueAndEmpresaId(pessoa, TenantContext.getEmpresaId())
                .ifPresent(contato -> {
                    contato.setPrincipal(false);
                    contatoRepository.save(contato);
                });
    }
}
