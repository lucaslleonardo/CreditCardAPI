package br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.Roles;
import lombok.Builder;

@Builder
public record UsuarioResponse(Long id, String nome, String email, Roles cargo) {
}
