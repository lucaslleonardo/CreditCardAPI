package br.com.lucaslleonardo.CreditCardAPI.database.entity;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="Fatura")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FaturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataFechamento;

    private LocalDate dataVencimento;

    private boolean jurosAplicado;

    private BigDecimal valor;

    private StatusFatura statusFatura;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "cartao_id")
    private CartaoEntity cartao;

    @OneToMany(mappedBy = "fatura")
    private List<CompraEntity> compra;

    @OneToMany(mappedBy = "fatura" )
    private List<PagamentoEntity> pagamento;


}
