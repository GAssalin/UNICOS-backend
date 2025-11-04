package br.com.unicos.ms_empresa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a estrutura organizacional interna de uma empresa.
 * <p>
 * Pode ser utilizada para controle de permissões, gestão interna
 * ou definição de centros de custo. Cada departamento pode conter
 * diversos setores.
 */
@Entity
@Table(name = "departamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Setor> setores = new ArrayList<>();

    @NotNull
    @Column(nullable = false)
    private Boolean ativo = true;
}
