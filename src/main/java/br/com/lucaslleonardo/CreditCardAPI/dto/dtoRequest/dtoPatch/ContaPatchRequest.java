package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusConta;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ContaPatchRequest {

    private StatusConta status;
}
