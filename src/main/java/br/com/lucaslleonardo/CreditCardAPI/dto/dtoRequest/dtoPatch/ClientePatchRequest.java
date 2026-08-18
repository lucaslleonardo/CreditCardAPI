package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCliente;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ClientePatchRequest {

    private String nome;

    private String email;

    private StatusCliente status;
}
