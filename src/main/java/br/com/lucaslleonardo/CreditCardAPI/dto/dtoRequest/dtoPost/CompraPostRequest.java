package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCompra;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String nome;

    @NotNull
    private BigDecimal valor;

    @JsonFormat(pattern = "dd/MM/yyyy" )
    @NotNull
    private LocalDate dataCompra;

    @NotBlank
    private StatusCompra statusCompra;

    @NotBlank
    private CartaoEntity cartao;

    @NotBlank
    private FaturaEntity fatura;

}
