package br.com.erp.ms_empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO usado para retorno detalhado das informações de uma empresa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponse {

    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String inscricaoEstadual;
    private String inscricaoMunicipal;

    private List<FilialListDTO> filiais;
    private List<EnderecoEmpresaListDTO> enderecos;
    private List<ContatoEmpresaListDTO> contatos;
    private List<DepartamentoEmpresaListDTO> departamentos;
}