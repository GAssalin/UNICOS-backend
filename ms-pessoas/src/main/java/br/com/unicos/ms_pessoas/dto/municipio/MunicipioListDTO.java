package br.com.unicos.ms_pessoas.dto.municipio;

import br.com.unicos.ms_pessoas.enums.Uf;

/**
 * DTO utilizado em listagens de municípios,
 * trazendo apenas informações essenciais para consultas rápidas.
 */
public record MunicipioListDTO(
        Long id,
        String nome,
        Uf uf
) {}
