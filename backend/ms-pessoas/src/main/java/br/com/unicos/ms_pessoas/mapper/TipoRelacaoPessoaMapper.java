package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaResponse;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TipoRelacaoPessoaMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade para DTO detalhado.
     */
    public TipoRelacaoPessoaResponse toResponse(TipoRelacaoPessoa entity) {
        return new TipoRelacaoPessoaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao()
        );
    }

    /**
     * Converte a entidade para DTO de listagem.
     */
    public TipoRelacaoPessoaListDTO toListDTO(TipoRelacaoPessoa entity) {
        return new TipoRelacaoPessoaListDTO(
                entity.getId(),
                entity.getNome()
        );
    }

    /**
     * Converte o DTO de requisição para entidade.
     */
    public TipoRelacaoPessoa toEntity(TipoRelacaoPessoaRequest request) {
        return mapper.map(request, TipoRelacaoPessoa.class);
    }
}
