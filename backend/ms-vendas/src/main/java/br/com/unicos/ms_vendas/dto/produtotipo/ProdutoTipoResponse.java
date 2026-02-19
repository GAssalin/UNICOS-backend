package br.com.unicos.ms_vendas.dto.produtotipo;

import java.time.LocalDateTime;

public record ProdutoTipoResponse(
        Long id,
        String nome,
        String descricao,
        Boolean ativo,
        Long criadoPor,
        LocalDateTime criadoEm,
        Long atualizadoPor,
        LocalDateTime atualizadoEm
) {}
