package br.com.lucaslleonardo.CreditCardAPI.database.entity;

import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCliente;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="Cliente")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String nome;

    @Column(nullable=false, unique=true,length=14)
    private String cpf;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private StatusCliente status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuarioID", nullable = false)
    private UsuarioEntity usuario;

}
