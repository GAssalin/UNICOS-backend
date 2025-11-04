package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.dto.PermissaoRequest;
import br.com.unicos.ms_auth.dto.PermissaoResponse;
import br.com.unicos.ms_auth.model.Permissao;
import br.com.unicos.ms_auth.repository.PermissaoRepository;
import br.com.unicos.ms_auth.service.PermissaoService;
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
 * Implementação da interface {@link PermissaoService}.
 * <p>
 * Contém as regras de negócio e interações com o repositório de Permissao.
 */
@Service
@RequiredArgsConstructor
public class PermissaoServiceImpl implements PermissaoService {

    private final PermissaoRepository permissaoRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public PermissaoResponse salvar(PermissaoRequest request) {
        if (permissaoRepository.existsByNome(request.nome())) {
            throw new DataIntegrityViolationException("Já existe uma permissão com este nome.");
        }

        Permissao permissao = modelMapper.map(request, Permissao.class);
        return modelMapper.map(permissaoRepository.save(permissao), PermissaoResponse.class);
    }

    @Override
    @Transactional
    public PermissaoResponse atualizar(Long id, PermissaoRequest request) {
        Permissao permissao = permissaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada."));

        permissao.setNome(request.nome());
        permissao.setDescricao(request.descricao());

        return modelMapper.map(permissaoRepository.save(permissao), PermissaoResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PermissaoResponse> buscarPorId(Long id) {
        return permissaoRepository.findById(id)
                .map(p -> modelMapper.map(p, PermissaoResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissaoResponse> listarTodas() {
        return permissaoRepository.findAll().stream()
                .map(p -> modelMapper.map(p, PermissaoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!permissaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Permissão não encontrada para exclusão.");
        }
        permissaoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNome(String nome) {
        return permissaoRepository.existsByNome(nome);
    }
}
