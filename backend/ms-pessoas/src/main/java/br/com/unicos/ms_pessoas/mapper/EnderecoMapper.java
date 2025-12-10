package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.endereco.EnderecoListDTO;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoRequest;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoResponse;
import br.com.unicos.ms_pessoas.model.Endereco;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnderecoMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade para DTO de resposta detalhada.
     */
    public EnderecoResponse toResponse(Endereco entity) {
        return new EnderecoResponse(
                entity.getId(),
                entity.getPessoa() != null ? entity.getPessoa().getId() : null,
                entity.getTipo(),
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getMunicipio() != null ? entity.getMunicipio().getId() : null,
                entity.getCep(),
                entity.isPrincipal()
        );
    }

    /**
     * Converte a entidade para DTO de listagem.
     */
    public EnderecoListDTO toListDTO(Endereco entity) {
        return new EnderecoListDTO(
                entity.getId(),
                entity.getTipo(),
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getBairro(),
                entity.getCep(),
                entity.isPrincipal()
        );
    }

    /**
     * Converte o DTO de requisição para entidade.
     */
    public Endereco toEntity(EnderecoRequest request) {
        return mapper.map(request, Endereco.class);
    }
}
