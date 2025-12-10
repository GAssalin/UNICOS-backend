package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaResponse;
import br.com.unicos.ms_pessoas.model.Pessoa;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PessoaMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade para DTO detalhado.
     */
    public PessoaResponse toResponse(Pessoa entity) {
        return new PessoaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getTipoPessoa()
        );
    }

    /**
     * Converte a entidade para DTO de listagem.
     */
    public PessoaListDTO toListDTO(Pessoa entity) {
        PessoaListDTO p = null;

        p = new PessoaListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getTipoPessoa()
        );

        return p;
    }

    /**
     * Converte o DTO de requisição para entidade.
     * Observação: como Pessoa é abstrata, este método funciona
     * apenas para subclasses (PessoaFisica, PessoaJuridica).
     */
    public <T extends Pessoa> T toEntity(PessoaRequest request, Class<T> pessoaClass) {
        return mapper.map(request, pessoaClass);
    }
}
