package br.com.unicos.ms_empresa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a empresa matriz do sistema.
 * <p>
 * Esta entidade contém as informações principais da organização,
 * servindo como ponto central de vínculo para filiais, departamentos,
 * endereços, contatos e configurações fiscais.
 */
@Entity
@Table(name = "empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String razaoSocial;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String nomeFantasia;

    @NotBlank
    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(length = 20)
    private String inscricaoEstadual;

    @Column(length = 20)
    private String inscricaoMunicipal;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Filial> filiais = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EnderecoEmpresa> enderecos = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Departamento> departamentos = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ContatoEmpresa> contatos = new ArrayList<>();

    @OneToOne(mappedBy = "empresa", cascade = CascadeType.ALL)
    private ConfiguracaoFiscal configuracaoFiscal;
}
