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
 * Entidade que representa a empresa matriz do sistema.
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
    @Column(nullable = false, length = 18, unique = true)
    private String cnpj;

    @Column(length = 20)
    private String inscricaoEstadual;

    @Column(length = 20)
    private String inscricaoMunicipal;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Filial> filiais = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<EnderecoEmpresa> enderecos = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ContatoEmpresa> contatos = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DepartamentoEmpresa> departamentos = new ArrayList<>();
}