package br.com.unicos.ms_vendas.mapper;

import br.com.unicos.ms_vendas.dto.produto.ProdutoCreateRequest;
import br.com.unicos.ms_vendas.dto.produto.ProdutoResponse;
import br.com.unicos.ms_vendas.dto.produto.ProdutoResumoResponse;
import br.com.unicos.ms_vendas.dto.produto.ProdutoUpdateRequest;
import br.com.unicos.ms_vendas.model.Produto;
import br.com.unicos.ms_vendas.model.UnidadeMedida;
import br.com.unicos.ms_vendas.repository.UnidadeMedidaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    private final ModelMapper modelMapper;
    private final UnidadeMedidaRepository unidadeMedidaRepository;

    public ProdutoMapper(ModelMapper modelMapper, UnidadeMedidaRepository unidadeMedidaRepository) {
        this.modelMapper = modelMapper;
        this.unidadeMedidaRepository = unidadeMedidaRepository;
    }

    public Produto toEntity(ProdutoCreateRequest request, Long empresaId) {
        Produto entity = modelMapper.map(request, Produto.class);

        entity.setEmpresaId(empresaId);

        UnidadeMedida unidade = unidadeMedidaRepository.findByIdAndEmpresaId(request.unidadeMedidaId(), empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Unidade de medida não encontrada para o tenant."));

        entity.setUnidadeMedida(unidade.getCodigo());

        return entity;
    }

    public void updateEntity(ProdutoUpdateRequest request, Produto entity, Long empresaId) {
        modelMapper.map(request, entity);

        UnidadeMedida unidade = unidadeMedidaRepository.findByIdAndEmpresaId(request.unidadeMedidaId(), empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Unidade de medida não encontrada para o tenant."));

        entity.setUnidadeMedida(unidade.getCodigo());
    }

    public ProdutoResponse toResponse(Produto entity, Long empresaId) {
        ProdutoResponse base = modelMapper.map(entity, ProdutoResponse.class);

        Long unidadeMedidaId = unidadeMedidaRepository.findByCodigoAndEmpresaId(entity.getUnidadeMedida(), empresaId)
                .map(UnidadeMedida::getId)
                .orElse(null);

        return new ProdutoResponse(
                entity.getId(),
                entity.getCodigo(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getTipoProduto(),
                unidadeMedidaId,
                entity.getCategoriaId(),
                entity.getMarcaId(),
                entity.getCodigoBarras(),
                entity.getPrecoBase(),
                entity.getPeso(),
                entity.getVolume(),
                entity.getAtivo(),
                entity.getCriadoPor(),
                entity.getCriadoEm(),
                entity.getAtualizadoPor(),
                entity.getAtualizadoEm()
        );
    }

    public ProdutoResumoResponse toResumoResponse(Produto entity) {
        return modelMapper.map(entity, ProdutoResumoResponse.class);
    }
}
