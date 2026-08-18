package br.com.lucaslleonardo.CreditCardAPI.database.entity;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCartao;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Entity
@Table(name="Cartao")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false,length=19, unique=true)
    private String numeroCartao;

    @Column(nullable = false)
    private String nomeImpresso;

    @JsonFormat(pattern = "MM/YYYY")
    @Column(nullable=false)
    private YearMonth validade;

    @Column(nullable=false,length=3)
    private Integer cvv;

    @Column(nullable=false)
    private BigDecimal limite;

    @Column(nullable=false)
    private BigDecimal limiteDisponivel;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private StatusCartao statusCartao;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "conta_id")
    private ContaEntity conta;

    @OneToMany(mappedBy = "cartao" )
    private List<CompraEntity> compra;

    @OneToMany(mappedBy = "cartao")
    private List<FaturaEntity> fatura;



}
