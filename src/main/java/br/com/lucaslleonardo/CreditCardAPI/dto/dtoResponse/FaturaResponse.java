package br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record FaturaResponse(Long id, LocalDate dataFechamento, LocalDate dataVencimento, BigDecimal valor, StatusFatura statusFatura) {
}
