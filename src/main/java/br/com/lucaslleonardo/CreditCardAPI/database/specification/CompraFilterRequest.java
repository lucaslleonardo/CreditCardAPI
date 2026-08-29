package br.com.lucaslleonardo.CreditCardAPI.database.specification;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCompra;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraFilterRequest {

    private BigDecimal valor;

    private StatusCompra statusCompra;

    private LocalDate dataCompra;


}
