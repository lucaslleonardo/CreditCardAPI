package br.com.lucaslleonardo.CreditCardAPI.database.entity;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCompra;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="Compra")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String nome;

    @Column(nullable=false)
    private BigDecimal valor;

    @JsonFormat(pattern = "dd/MM/yyyy" )
    @Column(nullable=false)
    private LocalDate dataCompra;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private StatusCompra statusCompra;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cartao_id")
    private CartaoEntity cartao;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "fatura_id")
    private FaturaEntity fatura;

}
