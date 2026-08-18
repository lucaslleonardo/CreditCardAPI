package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UsuarioPatchRequest {

    private String email;
    private String senha;
    private String nome;
    
}
