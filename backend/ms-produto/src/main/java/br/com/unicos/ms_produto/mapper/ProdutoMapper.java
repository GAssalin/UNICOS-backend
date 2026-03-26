package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.produto.ProdutoCreateRequest;
import br.com.unicos.ms_produto.dto.produto.ProdutoResponse;
import br.com.unicos.ms_produto.dto.produto.ProdutoResumoResponse;
import br.com.unicos.ms_produto.dto.produto.ProdutoUpdateRequest;
import br.com.unicos.ms_produto.model.Produto;
import br.com.unicos.ms_produto.model.UnidadeMedida;
import br.com.unicos.ms_produto.repository.UnidadeMedidaRepository;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    private final UnidadeMedidaRepository unidadeMedidaRepository;

    public ProdutoMapper(UnidadeMedidaRepository unidadeMedidaRepository) {
        this.unidadeMedidaRepository = unidadeMedidaRepository;
    }

    public Produto toEntity(ProdutoCreateRequest request, Long empresaId) {
        if (request == null)
            return null;

        UnidadeMedida unidade = unidadeMedidaRepository
                .findByIdAndEmpresaId(request.unidadeMedidaId(), empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Unidade de medida não encontrada para o tenant."));

        Produto entity = new Produto();
        entity.setCodigo(request.codigo());
        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
        entity.setTipoProduto(request.tipoProduto());
        entity.setUnidadeMedida(unidade.getCodigo());
        entity.setCategoriaId(request.categoriaId());
        entity.setMarcaId(request.marcaId());
        entity.setCodigoBarras(request.codigoBarras());
        entity.setPrecoBase(request.precoBase());
        entity.setPeso(request.peso());
        entity.setVolume(request.volume());
        entity.setEmpresaId(empresaId);

        return entity;
    }

    public void updateEntity(ProdutoUpdateRequest request, Produto entity, Long empresaId) {
        if (request == null || entity == null)
            return;

        UnidadeMedida unidade = unidadeMedidaRepository
                .findByIdAndEmpresaId(request.unidadeMedidaId(), empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Unidade de medida não encontrada para o tenant."));

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
        entity.setTipoProduto(request.tipoProduto());
        entity.setUnidadeMedida(unidade.getCodigo());
        entity.setCategoriaId(request.categoriaId());
        entity.setMarcaId(request.marcaId());
        entity.setCodigoBarras(request.codigoBarras());
        entity.setPrecoBase(request.precoBase());
        entity.setPeso(request.peso());
        entity.setVolume(request.volume());
    }

    public ProdutoResponse toResponse(Produto entity, Long empresaId) {
        if (entity == null)
            return null;

        Long unidadeMedidaId = unidadeMedidaRepository
                .findByCodigoAndEmpresaId(entity.getUnidadeMedida(), empresaId)
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
        if (entity == null)
            return null;

        return new ProdutoResumoResponse(
                entity.getId(),
                entity.getCodigo(),
                entity.getNome(),
                entity.getTipoProduto(),
                entity.getPrecoBase(),
                entity.getAtivo()
        );
    }
}