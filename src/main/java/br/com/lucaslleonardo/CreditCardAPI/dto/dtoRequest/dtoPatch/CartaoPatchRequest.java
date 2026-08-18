package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCartao;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CartaoPatchRequest {

    private StatusCartao statusCartao;

}
