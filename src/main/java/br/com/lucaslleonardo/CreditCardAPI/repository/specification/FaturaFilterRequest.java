package br.com.lucaslleonardo.CreditCardAPI.repository.specification;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaturaFilterRequest {

    private BigDecimal valor;

    private StatusFatura statusFatura;
}
