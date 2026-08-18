package br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusConta;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ContaResponse(long id, String numeroConta, Integer agencia, BigDecimal saldo, StatusConta statusConta) {
}
