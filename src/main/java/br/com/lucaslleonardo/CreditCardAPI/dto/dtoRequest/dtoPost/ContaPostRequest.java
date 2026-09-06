package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.ClienteEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusConta;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ContaPostRequest {

    @NotBlank
    private String numeroConta;

    @NotNull
    private Integer agencia;

    @NotNull
    private BigDecimal saldo;


    @NotBlank
    private ClienteEntity cliente;

    @NotBlank
    private List<CartaoEntity> cartao;
}

