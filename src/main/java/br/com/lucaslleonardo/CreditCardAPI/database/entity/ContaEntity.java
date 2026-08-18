package br.com.lucaslleonardo.CreditCardAPI.database.entity;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusConta;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="Conta")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false,length=10)
    private String numeroConta;

    @Column(nullable=false,length=4)
    private Integer agencia;

    @Column(nullable=false)
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    private StatusConta statusConta;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name= "cliente_id")
    private ClienteEntity cliente;

    @OneToMany(mappedBy = "conta")
    private List<CartaoEntity> cartao;
}
