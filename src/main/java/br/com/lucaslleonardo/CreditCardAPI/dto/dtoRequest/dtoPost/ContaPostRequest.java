package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.ClienteEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusConta;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ContaPostRequest {

    private String numeroConta;

    private Integer agencia;

    private BigDecimal saldo;

    private StatusConta status;

    private ClienteEntity cliente;

    private CartaoEntity cartao;
}

