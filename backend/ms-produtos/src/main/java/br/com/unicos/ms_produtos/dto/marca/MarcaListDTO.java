package br.com.unicos.ms_produtos.dto.marca;

/**
 * DTO utilizado em listagens de marcas,
 * trazendo somente os dados essenciais.
 */
public record MarcaListDTO(
        Long id,
        String nome,
        String paisOrigem
) {}
