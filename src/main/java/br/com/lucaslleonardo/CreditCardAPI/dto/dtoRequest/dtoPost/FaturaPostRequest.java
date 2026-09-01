package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.PagamentoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class FaturaPostRequest {

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy" )
    private LocalDate dataFechamento;

    @NotNull
    private BigDecimal valor;

    @NotBlank
    private StatusFatura statusFatura;

    @NotBlank
    private CartaoEntity cartao;

    @NotBlank
    private List<CompraEntity> compra;

    @NotBlank
    private List<PagamentoEntity> pagamento;
}
