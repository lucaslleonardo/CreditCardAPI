package br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse;


import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartaoResponse(Long id, String numeroCartao, String nomeImpresso, BigDecimal limiteDisponivel) {
}
