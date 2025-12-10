package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoRequest;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoResponse;
import br.com.unicos.ms_pessoas.model.PessoaRelacao;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PessoaRelacaoMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade para DTO detalhado.
     */
    public PessoaRelacaoResponse toResponse(PessoaRelacao entity) {
        return new PessoaRelacaoResponse(
                entity.getId(),
                entity.getPessoa() != null ? entity.getPessoa().getId() : null,
                entity.getRelacionado() != null ? entity.getRelacionado().getId() : null,
                entity.getTipoRelacao() != null ? entity.getTipoRelacao().getId() : null
        );
    }

    /**
     * Converte a entidade para DTO de listagem.
     */
    public PessoaRelacaoListDTO toListDTO(PessoaRelacao entity) {
        return new PessoaRelacaoListDTO(
                entity.getId(),
                entity.getPessoa() != null ? entity.getPessoa().getId() : null,
                entity.getRelacionado() != null ? entity.getRelacionado().getId() : null,
                entity.getTipoRelacao() != null ? entity.getTipoRelacao().getId() : null
        );
    }

    /**
     * Converte o DTO de requisição para entidade.
     * As associações (Pessoa pessoa, Pessoa relacionado, TipoRelacaoPessoa tipoRelacao)
     * serão definidas no service antes do save().
     */
    public PessoaRelacao toEntity(PessoaRelacaoRequest request) {
        return mapper.map(request, PessoaRelacao.class);
    }
}
