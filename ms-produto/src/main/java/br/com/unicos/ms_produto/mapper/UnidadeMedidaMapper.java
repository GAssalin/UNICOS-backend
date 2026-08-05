package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.unidademedida.UnidadeMedidaCreateRequest;
import br.com.unicos.ms_produto.dto.unidademedida.UnidadeMedidaResponse;
import br.com.unicos.ms_produto.dto.unidademedida.UnidadeMedidaResumoResponse;
import br.com.unicos.ms_produto.dto.unidademedida.UnidadeMedidaUpdateRequest;
import br.com.unicos.ms_produto.model.UnidadeMedida;
import org.springframework.stereotype.Component;

@Component
public class UnidadeMedidaMapper {

    public UnidadeMedida toEntity(UnidadeMedidaCreateRequest request, Long empresaId) {
        if (request == null)
            return null;

        UnidadeMedida entity = new UnidadeMedida();
        entity.setCodigo(request.codigo());
        entity.setDescricao(request.descricao());
        entity.setFracionavel(request.fracionavel());
        entity.setEmpresaId(empresaId);

        return entity;
    }

    public void updateEntity(UnidadeMedidaUpdateRequest request, UnidadeMedida entity) {
        if (request == null || entity == null)
            return;

        entity.setCodigo(request.codigo());
        entity.setDescricao(request.descricao());
        entity.setFracionavel(request.fracionavel());
    }

    public UnidadeMedidaResponse toResponse(UnidadeMedida entity) {
        if (entity == null)
            return null;

        return new UnidadeMedidaResponse(
                entity.getId(),
                entity.getCodigo(),
                entity.getDescricao(),
                entity.getFracionavel(),
                entity.getAtivo(),
                entity.getCriadoPor(),
                entity.getCriadoEm(),
                entity.getAtualizadoPor(),
                entity.getAtualizadoEm()
        );
    }

    public UnidadeMedidaResumoResponse toResumoResponse(UnidadeMedida entity) {
        if (entity == null)
            return null;

        return new UnidadeMedidaResumoResponse(
                entity.getId(),
                entity.getCodigo(),
                entity.getDescricao(),
                entity.getFracionavel(),
                entity.getAtivo()
        );
    }
}