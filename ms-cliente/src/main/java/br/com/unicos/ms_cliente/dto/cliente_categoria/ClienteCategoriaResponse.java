package br.com.unicos.ms_cliente.dto.cliente_categoria;


import java.time.LocalDateTime;

public record ClienteCategoriaResponse(

    Long id,
    Long empresaId,
    String nome,
    String descricao,
    Boolean ativo,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) { }
