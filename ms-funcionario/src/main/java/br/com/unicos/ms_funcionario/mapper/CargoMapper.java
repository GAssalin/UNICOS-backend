package br.com.unicos.ms_funcionario.mapper;

import br.com.unicos.ms_funcionario.dto.cargo.CargoRequest;
import br.com.unicos.ms_funcionario.dto.cargo.CargoResponse;
import br.com.unicos.ms_funcionario.model.Cargo;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link Cargo} e seus DTOs.
 */
@Component
public class CargoMapper {

    public CargoResponse toResponse(Cargo entity) {
        if (entity == null)
            return null;

        return new CargoResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getPapel(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public Cargo toEntity(CargoRequest request) {
        if (request == null)
            return null;

        return Cargo.builder()
                .nome(request.nome().trim())
                .descricao(request.descricao())
                .papel(request.papel())
                .build();
    }

    public void updateEntity(Cargo entity, CargoRequest request) {
        if (request == null || entity == null)
            return;

        entity.setNome(request.nome().trim());
        entity.setDescricao(request.descricao());
        entity.setPapel(request.papel());
    }
}
