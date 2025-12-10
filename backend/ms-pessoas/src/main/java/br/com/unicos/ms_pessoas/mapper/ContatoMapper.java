package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.contato.ContatoListDTO;
import br.com.unicos.ms_pessoas.dto.contato.ContatoRequest;
import br.com.unicos.ms_pessoas.dto.contato.ContatoResponse;
import br.com.unicos.ms_pessoas.model.Contato;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContatoMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade para DTO de resposta detalhada.
     */
    public ContatoResponse toResponse(Contato entity) {
        return new ContatoResponse(
                entity.getId(),
                entity.getPessoa() != null ? entity.getPessoa().getId() : null,
                entity.getTipo(),
                entity.getValor(),
                entity.isPrincipal()
        );
    }

    /**
     * Converte a entidade para DTO de listagem.
     */
    public ContatoListDTO toListDTO(Contato entity) {
        return new ContatoListDTO(
                entity.getId(),
                entity.getTipo(),
                entity.getValor(),
                entity.isPrincipal()
        );
    }

    /**
     * Converte o DTO request para entidade.
     */
    public Contato toEntity(ContatoRequest request) {
        return mapper.map(request, Contato.class);
    }
}
