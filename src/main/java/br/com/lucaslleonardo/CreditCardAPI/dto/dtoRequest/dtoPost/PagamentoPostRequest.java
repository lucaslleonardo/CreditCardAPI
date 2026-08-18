package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PagamentoPostRequest {

    private BigDecimal valor;

    private LocalDate dataPagamento;

    private StatusFatura statusFatura;

    private FaturaEntity fatura;
}
