package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.ContaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCartao;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.YearMonth;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CartaoPostRequest {

    @NotBlank
    private String numeroCartao;

    @NotBlank
    private String nomeImpresso;

    @JsonFormat(pattern = "MM/YYYY")
    @NotNull
    private YearMonth validade;

    @NotNull
    private Integer cvv;

    @NotNull
    private BigDecimal limite;

    @NotNull
    private BigDecimal limiteDisponivel;

    @NotBlank
    private StatusCartao statusCartao;

    @NotBlank
    private ContaEntity conta;

    @NotBlank
    private CompraEntity compra;

    @NotBlank
    private FaturaEntity fatura;

}
