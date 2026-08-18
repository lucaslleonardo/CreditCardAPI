package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCompra;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CompraPostRequest {

    private String nome;

    private BigDecimal valor;

    @JsonFormat(pattern = "dd/MM/yyyy" )
    private LocalDate dataCompra;

    private StatusCompra statusCompra;

    private CartaoEntity cartao;

    private FaturaEntity fatura;

}
