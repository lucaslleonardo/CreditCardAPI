package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.ContaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCartao;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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

    private String numeroCartao;

    private String nomeImpresso;

    @JsonFormat(pattern = "MM/YYYY")
    private YearMonth validade;

    private Integer cvv;

    private BigDecimal limite;

    private BigDecimal limiteDisponivel;

    private StatusCartao statusCartao;

    private ContaEntity conta;

    private CompraEntity compra;

    private FaturaEntity fatura;

}
