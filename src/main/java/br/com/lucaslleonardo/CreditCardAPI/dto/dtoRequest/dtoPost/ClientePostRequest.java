package br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.UsuarioEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCliente;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ClientePostRequest {

    private String nome;

    private String cpf;

    private String email;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    private StatusCliente status;

    private UsuarioEntity usuario;

}
