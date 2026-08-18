package br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCompra;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CompraResponse(Long id, String nome, BigDecimal valor, LocalDate dataCompra, StatusCompra statusCompra) {
}
