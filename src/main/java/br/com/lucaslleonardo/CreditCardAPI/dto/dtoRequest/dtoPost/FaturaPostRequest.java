package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.PagamentoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class FaturaPostRequest {

    private LocalDate dataFechamento;

    private LocalDate dataVencimento;

    private BigDecimal valor;

    private StatusFatura statusFatura;

    private CartaoEntity cartao;

    private CompraEntity compra;

    private PagamentoEntity pagamento;
}
