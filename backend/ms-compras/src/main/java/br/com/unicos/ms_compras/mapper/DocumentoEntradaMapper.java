package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.DocumentoEntradaDto;
import br.com.unicos.ms_compras.model.DocumentoEntrada;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre {@link DocumentoEntrada}
 * e {@link DocumentoEntradaDto}.
 *
 * <p>
 * Padrão UniCoS:
 * - Não resolve relacionamento automaticamente (RecebimentoCompra é resolvido no service)
 * - Não altera tenant/auditoria
 * - Não contém regras de negócio
 * </p>
 */
@Component
@RequiredArgsConstructor
public class DocumentoEntradaMapper {

    private final ModelMapper mapper;

    /**
     * Converte entidade para DTO.
     */
    public DocumentoEntradaDto toResponse(DocumentoEntrada entity) {
        if (entity == null) return null;

        Long recebimentoCompraId = null;
        if (entity.getRecebimentoCompra() != null) {
            recebimentoCompraId = entity.getRecebimentoCompra().getId();
        }

        return new DocumentoEntradaDto(
                entity.getId(),
                recebimentoCompraId,
                entity.getTipoDocumento(),
                entity.getNumero(),
                entity.getSerie(),
                entity.getChaveAcesso(),
                entity.getDataEmissao(),
                entity.getArquivoRef(),
                entity.getObservacao()
        );
    }

    /**
     * Converte DTO para entidade.
     *
     * <p>
     * NÃO seta {@code recebimentoCompra}.
     * O service deve buscar o RecebimentoCompra por id e associar corretamente.
     * </p>
     */
    public DocumentoEntrada toEntity(DocumentoEntradaDto dto) {
        if (dto == null) return null;

        DocumentoEntrada entity = mapper.map(dto, DocumentoEntrada.class);
        entity.setRecebimentoCompra(null); // relacionamento resolvido no service
        return entity;
    }

    /**
     * Atualiza entidade existente.
     *
     * <p>
     * Não permite trocar o vínculo com {@code recebimentoCompra}.
     * Caso necessário, trate como operação de negócio no service.
     * </p>
     */
    public void updateEntity(DocumentoEntradaDto dto, DocumentoEntrada entity) {
        if (dto == null || entity == null) return;

        entity.setTipoDocumento(dto.tipoDocumento());
        entity.setNumero(dto.numero());
        entity.setSerie(dto.serie());
        entity.setChaveAcesso(dto.chaveAcesso());
        entity.setDataEmissao(dto.dataEmissao());
        entity.setArquivoRef(dto.arquivoRef());
        entity.setObservacao(dto.observacao());
    }
}