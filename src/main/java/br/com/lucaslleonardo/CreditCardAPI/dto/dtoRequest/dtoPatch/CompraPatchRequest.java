package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCompra;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CompraPatchRequest {

    private StatusCompra statusCompra;

}
