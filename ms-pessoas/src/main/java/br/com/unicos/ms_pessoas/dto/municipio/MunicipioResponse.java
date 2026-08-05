package br.com.unicos.ms_pessoas.dto.municipio;

import br.com.unicos.ms_pessoas.enums.Uf;

/**
 * DTO de retorno que representa um município completo.
 */
public record MunicipioResponse(
        Long id,
        String nome,
        Uf uf,
        String codigoIbge
) {}
