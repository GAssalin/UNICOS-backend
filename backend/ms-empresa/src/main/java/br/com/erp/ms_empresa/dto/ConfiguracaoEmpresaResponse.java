package br.com.erp.ms_empresa.dto;

import br.com.erp.ms_empresa.enums.TipoAmbiente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para retorno detalhado da configuração fiscal e administrativa da empresa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoEmpresaResponse {

    private Long id;
    private Long empresaId;
    private String empresaRazaoSocial;
    private String regimeTributario;
    private String certificadoDigital;
    private TipoAmbiente tipoAmbiente;
    private String descricaoAmbiente;
}