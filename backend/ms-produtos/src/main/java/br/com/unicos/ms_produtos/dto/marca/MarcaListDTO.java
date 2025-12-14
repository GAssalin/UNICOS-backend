package br.com.unicos.ms_produtos.dto.marca;

/**
 * DTO utilizado em listagens de marcas,
 * contendo apenas informações essenciais para exibição.
 */
public record MarcaListDTO(
        Long id,
        String nome,
        String paisOrigem
) {}
