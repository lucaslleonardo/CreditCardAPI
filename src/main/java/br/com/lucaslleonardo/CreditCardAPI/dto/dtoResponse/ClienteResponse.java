package br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCliente;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record ClienteResponse(Long id, String nome, String cpf, String email, LocalDateTime dataCriacao, LocalDate dataNascimento, StatusCliente status) {
}
