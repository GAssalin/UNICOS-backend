package br.com.erp.ms_empresa.dto;

import br.com.erp.ms_empresa.enums.TipoAmbiente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para listagem simplificada das configurações das empresas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoEmpresaListDTO {

    private Long id;
    private String empresaRazaoSocial;
    private String regimeTributario;
    private TipoAmbiente tipoAmbiente;
    private String descricaoAmbiente;
}