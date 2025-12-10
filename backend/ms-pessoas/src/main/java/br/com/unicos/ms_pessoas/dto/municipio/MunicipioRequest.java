package br.com.unicos.ms_pessoas.dto.municipio;

import br.com.unicos.ms_pessoas.enums.Uf;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de municípios.
 * <p>
 * Permite definir nome, UF e código IBGE, essenciais para normalização
 * de endereços e integrações geográficas.
 */
public record MunicipioRequest(

        @NotBlank(message = "O nome do município é obrigatório.")
        @Size(max = 120, message = "O nome do município deve ter no máximo 120 caracteres.")
        String nome,

        @NotNull(message = "A UF é obrigatória.")
        Uf uf,

        @Size(max = 10, message = "O código IBGE deve ter no máximo 10 caracteres.")
        String codigoIbge
) {}
