package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

/**
 * Entidade que representa uma marca ou fabricante de produtos.
 *
 * <p>
 * As marcas organizam e classificam produtos dentro do catálogo,
 * permitindo agrupamentos e filtros utilizados em consultas e exibições.
 * </p>
 */
@Entity
@Table(
        name = "marca",
        indexes = {
                @Index(name = "idx_marca_nome", columnList = "nome"),
                @Index(name = "idx_marca_pais", columnList = "pais_origem")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome da marca (único).
     */
    @NotBlank(message = "O nome da marca é obrigatório.")
    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    /**
     * Descrição resumida da marca.
     */
    @Column(length = 255)
    private String descricao;

    /**
     * País de origem do fabricante.
     */
    @Column(name = "pais_origem", length = 100)
    private String paisOrigem;

    /**
     * Lista de produtos associados à marca (opcional).
     *
     * <p>
     * Relacionamento mantido com LAZY loading para evitar
     * carregamento desnecessário de produtos durante consultas.
     * </p>
     */
    @OneToMany(mappedBy = "marca", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Produto> produtos;

}
