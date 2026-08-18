package br.com.lucaslleonardo.CreditCardAPI.database.entity;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="Pagamento")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valor;

    private LocalDate dataPagamento;

    private StatusFatura statusFatura;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "fatura_id")
    private FaturaEntity fatura;
}
