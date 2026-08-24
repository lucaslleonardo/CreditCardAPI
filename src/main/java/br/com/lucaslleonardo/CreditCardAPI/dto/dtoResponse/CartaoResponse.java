package br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse;


import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCartao;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartaoResponse(Long id, String numeroCartao, String nomeImpresso, BigDecimal limiteDisponivel,BigDecimal limite , StatusCartao statusCartao) {
}
