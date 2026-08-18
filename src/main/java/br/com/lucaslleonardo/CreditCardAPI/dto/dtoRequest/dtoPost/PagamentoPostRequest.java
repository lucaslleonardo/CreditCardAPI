package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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
public class PagamentoPostRequest {

    @NotNull
    private BigDecimal valor;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy" )
    private LocalDate dataPagamento;

    @NotBlank
    private StatusFatura statusFatura;

    @NotBlank
    private FaturaEntity fatura;
}
