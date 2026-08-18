package br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse;


import lombok.Builder;

import java.math.BigDecimal;
import java.time.YearMonth;

@Builder
public record CartaoResponse(Long id, String numeroCartao, String nomeImpresso, YearMonth validade, Integer cvv, BigDecimal limite, BigDecimal limiteDisponivel) {
}
