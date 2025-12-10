package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaResponse;
import br.com.unicos.ms_pessoas.model.PessoaFisica;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PessoaFisicaMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade para DTO de resposta detalhada.
     */
    public PessoaFisicaResponse toResponse(PessoaFisica entity) {
        return new PessoaFisicaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getDataNascimento(),
                entity.getNomeSocial()
        );
    }

    /**
     * Converte a entidade para DTO de listagem.
     */
    public PessoaFisicaListDTO toListDTO(PessoaFisica entity) {
        return new PessoaFisicaListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCpf()
        );
    }

    /**
     * Converte o DTO de requisição para entidade.
     */
    public PessoaFisica toEntity(PessoaFisicaRequest request) {
        return mapper.map(request, PessoaFisica.class);
    }
}
