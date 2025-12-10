package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaResponse;
import br.com.unicos.ms_pessoas.model.PessoaJuridica;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PessoaJuridicaMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade para DTO de resposta detalhada.
     */
    public PessoaJuridicaResponse toResponse(PessoaJuridica entity) {
        return new PessoaJuridicaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getCnpj(),
                entity.getRazaoSocial(),
                entity.getNomeFantasia()
        );
    }

    /**
     * Converte a entidade para DTO de listagem.
     */
    public PessoaJuridicaListDTO toListDTO(PessoaJuridica entity) {
        return new PessoaJuridicaListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCnpj()
        );
    }

    /**
     * Converte o DTO de requisição para entidade.
     */
    public PessoaJuridica toEntity(PessoaJuridicaRequest request) {
        return mapper.map(request, PessoaJuridica.class);
    }
}
